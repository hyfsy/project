package com.hyf.proxyserver.extensions;

import com.hyf.proxyserver.server.ProxyUtils;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static java.nio.file.StandardWatchEventKinds.*;

public class DefaultRuleStore implements DefaultTrafficFilter.RuleStore {

    private final AtomicReference<List<DefaultTrafficFilter.TrafficRule>> rules = new AtomicReference<>(Collections.emptyList());

    public DefaultRuleStore() {
        String filePath = ProxyUtils.HOME + File.separator + "rule.config";
        Callback callback = new Callback() {
            @Override
            public void onFileCreated(String config) {
                // ignored
            }

            @Override
            public void onFileModified(String config) {

                // block,port,ip,domain
                // block,port,ip,domain
                String[] rows = config.split("\r\n");

                List<DefaultTrafficFilter.TrafficRule> ruleList = new ArrayList<>();
                for (String row : rows) {
                    if (row.startsWith("#")) continue;
                    if (row.trim().isEmpty()) continue;
                    String[] items = row.split(";");
                    if (items.length < 3) {
                        throw new RuntimeException("config format error");
                    }
                    boolean block = "".equals(items[0]) || "true".equals(items[0]);
                    List<Integer> ports = "".equals(items[1]) ? Collections.emptyList() : Arrays.stream(items[1].split(",")).map(Integer::parseInt).collect(Collectors.toList());
                    List<String> ips = "".equals(items[2]) ? Collections.emptyList() : Arrays.stream(items[2].split(",")).collect(Collectors.toList());
                    List<String> domains = items.length == 3 ? Collections.emptyList() : Arrays.stream(items[3].split(",")).collect(Collectors.toList());
                    DefaultTrafficFilter.TrafficRule rule = new DefaultTrafficFilter.TrafficRule();
                    rule.setBlock(block);
                    rule.setPorts(ports);
                    rule.setIps(ips);
                    rule.setDomains(domains);
                    ruleList.add(rule);
                }
                rules.set(ruleList);
            }

            @Override
            public void onFileDeleted() {
                rules.set(Collections.emptyList());
            }
        };
        try {
            startWatch(filePath, callback);
        } catch (IOException e) {
        }
    }

    @Override
    public List<DefaultTrafficFilter.TrafficRule> getRules() {
        return rules.get();
    }

    private void startWatch(String filePath, Callback callback) throws IOException {
        Path watchFile = prepareFile(filePath).toPath();
        Path watchDirectory = watchFile.getParent(); // 仅能监视目录
        WatchService watchService = FileSystems.getDefault().newWatchService();
        watchDirectory.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

        AtomicBoolean stopped = new AtomicBoolean(false);
        Thread t = new Thread(() -> {
            while (!stopped.get()) {
                try {
                    WatchKey watchKey = watchService.take();
                    List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                    watchKey.reset();

                    if (watchEvents.isEmpty()) {
                        continue;
                    }

                    for (WatchEvent<?> event : watchEvents) {
                        WatchEvent.Kind<?> kind = event.kind();
                        String childPath = event.context().toString();
                        if (watchFile.equals(watchDirectory.resolve(childPath))) {
                            if (ENTRY_CREATE.equals(kind)) {
                                // ignored
                                callback.onFileCreated(getFileContent(watchFile.toFile().getAbsolutePath()));
                            } else if (ENTRY_MODIFY.equals(kind)) {
                                callback.onFileModified(getFileContent(watchFile.toFile().getAbsolutePath()));
                            } else if (ENTRY_DELETE.equals(kind)) {
                                callback.onFileDeleted();
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.interrupted();
                } catch (Throwable ignored) {
                }
            }
        });
        t.setName("traffic-rule-watcher");
        t.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            stopped.set(true);
            try {
                watchService.close();
            } catch (IOException ignored) {
            }
        }));

        // 手动初始化
        callback.onFileModified(getFileContent(watchFile.toFile().getAbsolutePath()));
    }

    private static String getFileContent(String path) {
        try (Reader in = new FileReader(path); StringWriter out = new StringWriter()) {
            char[] buffer = new char[2048];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            return out.toString();
        } catch (FileNotFoundException ignored) {
            return null;
        } catch (IOException e) {
            throw new IllegalStateException("read file failed");
        }
    }

    private static File prepareFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            file.createNewFile();
        }
        return file;
    }

    public interface Callback {
        void onFileCreated(String config);

        void onFileModified(String config);

        void onFileDeleted();
    }
}

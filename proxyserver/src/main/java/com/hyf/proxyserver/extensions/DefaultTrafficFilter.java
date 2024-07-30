package com.hyf.proxyserver.extensions;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.List;

public class DefaultTrafficFilter implements TrafficFilter {

    private final RuleStore store;

    public DefaultTrafficFilter(RuleStore store) {
        this.store = store;
    }

    @Override
    public boolean filter(Channel inboundChannel, Channel outboundChannel, Object msg, boolean fromClient) {

        InetSocketAddress addr;
        if (fromClient) {
            addr = (InetSocketAddress) outboundChannel.remoteAddress();
        } else {
            addr = (InetSocketAddress) inboundChannel.remoteAddress();
        }

        String ip;
        int port;
        String domain = null;
        if (addr.isUnresolved()) {
            ip = addr.getHostString();
            port = addr.getPort();
        } else {
            ip = addr.getAddress().getHostAddress();
            port = addr.getPort();
            domain = addr.getHostName();
            // dns解析后的ip无域名
            if (ip.equals(domain)) {
                domain = null;
            }
        }

        boolean match = true;
        List<TrafficRule> rules = store.getRules();
        if (rules != null) {
            for (TrafficRule rule : rules) {
                if (!rule.match(ip, port, domain)) {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }

    public interface RuleStore {
        List<TrafficRule> getRules();
    }

    public static class TrafficRule {

        private boolean block = true;
        private List<Integer> ports;
        private List<String> ips;
        private List<String> domains;

        public boolean match(String ip, int port, String domain) {
            boolean hit = false;
            if (!hit && ip != null && ips != null && !ips.isEmpty() && ips.contains(ip)) {
                hit = true;
            }
            if (!hit && port != -1 && ports != null && !ports.isEmpty() && ports.contains(port)) {
                hit = true;
            }
            if (!hit && domain != null && domains != null && !domains.isEmpty() && domains.contains(domain)) {
                hit = true;
            }
            if (!hit) {
                return true;
            }
            return !this.block;
        }

        public boolean isBlock() {
            return block;
        }

        public void setBlock(boolean block) {
            this.block = block;
        }

        public List<Integer> getPorts() {
            return ports;
        }

        public void setPorts(List<Integer> ports) {
            this.ports = ports;
        }

        public List<String> getIps() {
            return ips;
        }

        public void setIps(List<String> ips) {
            this.ips = ips;
        }

        public List<String> getDomains() {
            return domains;
        }

        public void setDomains(List<String> domains) {
            this.domains = domains;
        }
    }
}

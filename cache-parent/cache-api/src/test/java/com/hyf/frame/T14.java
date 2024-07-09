package com.hyf.frame;

import com.hyf.cache.enums.CacheType;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author baB_hyf
 * @date 2022/03/03
 */
public class T14 {

    public static void main(String[] args) {
        CacheType custom = CacheType.CUSTOM;
        String s = custom.name().toLowerCase(Locale.ROOT);
        List<String> stringList = Arrays.asList("1", "2", "3", "4");
        Optional<String> reduce = stringList.stream()
                .collect(Collectors.reducing((s1, s2) -> null));

        System.out.println();

    }
}

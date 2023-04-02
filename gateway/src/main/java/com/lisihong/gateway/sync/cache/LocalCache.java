package com.lisihong.gateway.sync.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public record LocalCache() {

    public static final Map<String, Map<Integer, Set<Integer>>>
            apiLicenseMap;
    public static final Map<Integer, Set<Integer>>
            roleLicenseMap;

    static {
        apiLicenseMap = new HashMap<>();
        roleLicenseMap = new HashMap<>();
    }
}

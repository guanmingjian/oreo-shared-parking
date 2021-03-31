package cn.oreo.gateway.enhance.utils;

import cn.oreo.common.core.entity.constant.OreoConstant;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 */
public class RouteEnhanceCacheUtil {

    private static final String BLACKLIST_CHACHE_KEY_PREFIX = "oreo:route:blacklist:";
    private static final String RATELIMIT_CACHE_KEY_PREFIX = "oreo:route:ratelimit:";
    private static final String RATELIMIT_COUNT_KEY_PREFIX = "oreo:route:ratelimit:cout:";

    public static String getBlackListCacheKey(String ip) {
        if (OreoConstant.LOCALHOST.equalsIgnoreCase(ip)) {
            ip = OreoConstant.LOCALHOST_IP;
        }
        return String.format("%s%s", BLACKLIST_CHACHE_KEY_PREFIX, ip);
    }

    public static String getBlackListCacheKey() {
        return String.format("%sall", BLACKLIST_CHACHE_KEY_PREFIX);
    }

    public static String getRateLimitCacheKey(String uri, String method) {
        return String.format("%s%s:%s", RATELIMIT_CACHE_KEY_PREFIX, uri, method);
    }

    public static String getRateLimitCountKey(String uri, String ip) {
        return String.format("%s%s:%s", RATELIMIT_COUNT_KEY_PREFIX, uri, ip);
    }
}

package top.vchar.wechat.config;

/**
 * <p> 缓存key配置 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/8/7
 */
public final class EntWxSuiteCacheKey {

    /**
     * suite_ticket 缓存key
     */
    public static String suiteTicketCacheKey(String suiteId){
        return "ent_wx_suite_config:suite_ticket:"+suiteId;
    }

    /**
     * suite_access_token 缓存key
     */
    public static String suiteTokenCacheKey(String suiteId) {
        return "ent_wx_suite_config:suite_token:"+suiteId;
    }

    /**
     * 获取suite_access_token 锁
     */
    public static String suiteTokenLockKey(String suiteId) {
        return "ent_wx_suite_config:suite_token_lock:"+suiteId;
    }
}

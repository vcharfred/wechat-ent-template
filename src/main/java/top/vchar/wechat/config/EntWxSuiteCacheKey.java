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

    public static String suiteTokenCacheKey(String suiteId) {
        return "ent_wx_suite_config:suite_token:"+suiteId;
    }
}

package top.vchar.wechat.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.vchar.wechat.bean.EntWxSuite;
import top.vchar.wechat.feign.EntWxClient;
import top.vchar.wechat.util.WxBizMsgCrypt;

import java.util.List;
import java.util.Optional;

/**
 * <p> 企业微信第三方应用配置 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/18
 */
@Component
@ConfigurationProperties(prefix = "wx.ent")
public class EntWxSuiteConfig {

    @Autowired
    private RedisHelper redisHelper;
    @Autowired
    private EntWxClient entWxClient;

    private List<EntWxSuite> suites;

    public EntWxSuite getEntWxSuite(String suiteId){
        if(StringUtils.isBlank(suiteId)){
            return null;
        }
        Optional<EntWxSuite> optional = suites.stream().filter(p->p.getSuiteId().equals(suiteId)).findAny();
        return optional.orElse(null);
    }

    public WxBizMsgCrypt getWxBizMsgCrypt(String suiteId){
        return new WxBizMsgCrypt(getEntWxSuite(suiteId), "");
    }

    public WxBizMsgCrypt getWxBizMsgCrypt(String suiteId, String receiveId){
        return new WxBizMsgCrypt(getEntWxSuite(suiteId), receiveId);
    }

    public void setSuites(List<EntWxSuite> suites) {
        this.suites = suites;
    }

    /**
     * 更新应用的suite_ticket
     * @param suiteId 应用ID
     * @param suiteTicket 企业微信推送的suite_ticket
     */
    public void updateSuiteTicket(String suiteId, String suiteTicket) {
        if(StringUtils.isNotBlank(suiteId)){
            redisHelper.set(EntWxSuiteCacheKey.suiteTicketCacheKey(suiteId), suiteTicket, 60*60);
        }
    }

    public String getSuiteAccessToken(String suiteId) {
        String suiteAccessToken = redisHelper.get(EntWxSuiteCacheKey.suiteTicketCacheKey(suiteId));
        if(null==suiteAccessToken){

        }
    }

    public String getSuiteToken(String suiteId){
        redisHelper.lock(EntWxSuiteCacheKey.suiteTokenCacheKey(suiteId));
    }
}

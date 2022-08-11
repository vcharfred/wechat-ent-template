package top.vchar.wechat.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.vchar.wechat.bean.EntWxSuite;
import top.vchar.wechat.enums.ApiCode;
import top.vchar.wechat.feign.EntWxClient;
import top.vchar.wechat.util.WxBizMsgCrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p> 企业微信第三方应用配置 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/18
 */
@Slf4j
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
     * 更新应用的 suite_ticket
     * @param suiteId 应用ID
     * @param suiteTicket 企业微信推送的 suite_ticket
     */
    public void updateSuiteTicket(String suiteId, String suiteTicket) {
        if(StringUtils.isNotBlank(suiteId)){
            redisHelper.set(EntWxSuiteCacheKey.suiteTicketCacheKey(suiteId), suiteTicket, 60*60);
        }
    }

    /**
     * 获取 suite_access_token
     * @param suiteId  应用ID
     * @return 企业微信推送的 suite_access_token
     */
    public String getSuiteAccessToken(String suiteId) {
        String cacheKey = EntWxSuiteCacheKey.suiteTokenCacheKey(suiteId);
        String suiteAccessToken = redisHelper.get(cacheKey);
        if(null==suiteAccessToken){
            boolean lock = redisHelper.lock(EntWxSuiteCacheKey.suiteTokenLockKey(suiteId), 60);
            if(lock){
                EntWxSuite entWxSuite = getEntWxSuite(suiteId);
                Map<String, String> map = new HashMap<>(3);
                map.put("suite_id", suiteId);
                map.put("suite_secret", entWxSuite.getSecret());
                map.put("suite_ticket", redisHelper.get(EntWxSuiteCacheKey.suiteTicketCacheKey(suiteId)));
                String res = entWxClient.getSuiteToken(map);
                JSONObject resJson = JSON.parseObject(res);
                suiteAccessToken = resJson.getString("suite_access_token");
                if(null==suiteAccessToken){
                    throw new BizException(ApiCode.ENT_WX_ACCESS_TOKEN_ERROR, resJson.getString("errmsg"));
                }
                redisHelper.set(cacheKey, suiteAccessToken, 60*59*2);
            }
        }
        return suiteAccessToken;
    }
}

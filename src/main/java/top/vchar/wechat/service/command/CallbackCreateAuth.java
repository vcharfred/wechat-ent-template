package top.vchar.wechat.service.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import top.vchar.wechat.config.EntWxSuiteConfig;
import top.vchar.wechat.enums.CommandCallbackType;
import top.vchar.wechat.feign.EntWxClient;
import top.vchar.wechat.util.XMLParse;

import java.util.HashMap;
import java.util.Map;

/**
 * <p> 从企业微信应用市场发起授权时，企业微信后台会推送授权成功通知 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/8/7
 */
@Service
public class CallbackCreateAuth implements EntWxCommandCallback {

    @Autowired
    private EntWxClient entWxClient;
    @Autowired
    private EntWxSuiteConfig entWxSuiteConfig;

    @Override
    public CommandCallbackType getType() {
        return CommandCallbackType.SUITE_TICKET;
    }

    @Override
    public void dealCallback(String body) {
        Element element = XMLParse.coverXml(body);
        String suitId = XMLParse.getXmlValue(element, "SuiteId");
        String authCode = XMLParse.getXmlValue(element, "AuthCode");

        Map<String, String> map = new HashMap<>(1);
        map.put("auth_code", authCode);
        this.entWxClient.getPermanentCode(entWxSuiteConfig.getSuiteAccessToken(suitId), map);
    }
}

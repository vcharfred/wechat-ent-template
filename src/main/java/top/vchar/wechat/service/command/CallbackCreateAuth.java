package top.vchar.wechat.service.command;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        // 获取企业永久授权码
        Map<String, String> map = new HashMap<>(1);
        map.put("auth_code", authCode);
        String res = this.entWxClient.getPermanentCode(entWxSuiteConfig.getSuiteAccessToken(suitId), map);
        log.info("企业授权信息：{}", res);
    }
}

package top.vchar.wechat.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import top.vchar.wechat.enums.CommandCallbackType;
import top.vchar.wechat.util.XMLParse;

/**
 * <p> 企业对应用的授权发生变更 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/8/7
 */
@Slf4j
@Service
public class CallbackChangeAuth implements EntWxCommandCallback {

    @Override
    public CommandCallbackType getType() {
        return CommandCallbackType.CHANGE_AUTH;
    }

    @Override
    public void dealCallback(String body) {
        Element element = XMLParse.coverXml(body);
        String suitId = XMLParse.getXmlValue(element, "SuiteId");
        String authCorpId = XMLParse.getXmlValue(element, "AuthCorpId");
        log.info("企业微信应用[{}]下企业[{}]授权发生变更", suitId, authCorpId);
    }
}

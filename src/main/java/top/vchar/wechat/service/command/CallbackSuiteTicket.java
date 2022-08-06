package top.vchar.wechat.service.command;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import top.vchar.wechat.config.EntWxSuiteConfig;
import top.vchar.wechat.enums.CommandCallbackType;
import top.vchar.wechat.util.XMLParse;

/**
 * <p> suite_ticket 指令回调 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/24
 */
@Slf4j
@Service
public class CallbackSuiteTicket implements EntWxCommandCallback {

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
        String suiteTicket = XMLParse.getXmlValue(element, "SuiteTicket");
        this.entWxSuiteConfig.updateSuiteTicket(suitId, suiteTicket);
    }

}

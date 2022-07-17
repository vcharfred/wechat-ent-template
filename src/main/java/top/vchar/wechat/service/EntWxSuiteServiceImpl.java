package top.vchar.wechat.service;

import org.springframework.stereotype.Service;
import top.vchar.wechat.bean.EntWxSuite;
import top.vchar.wechat.config.EntWxSuiteConfig;
import top.vchar.wechat.util.WxBizMsgCrypt;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 企业微信第三方应用业务逻辑 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/15
 */
@Service
public class EntWxSuiteServiceImpl implements IEntWxSuiteService{

    private final EntWxSuiteConfig entWxSuiteConfig;

    public EntWxSuiteServiceImpl(EntWxSuiteConfig entWxSuiteConfig) {
        this.entWxSuiteConfig = entWxSuiteConfig;
    }

    /**
     * 回调验证
     * @param suitId 应用ID
     * @param request 请求对象
     * @return 返回需要返回的字符串
     */
    @Override
    public String verifyCallback(String suitId, HttpServletRequest request) {
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echoStr = request.getParameter("echostr").replace(" ", "=");

        EntWxSuite entWxSuite = entWxSuiteConfig.getEntWxSuite(suitId);
        WxBizMsgCrypt wxBizMsgCrypt = new WxBizMsgCrypt(entWxSuite);
        return wxBizMsgCrypt.verifyUrl(msgSignature, timestamp, nonce, echoStr);
    }

    @Override
    public void dataCallback(String suitId, HttpServletRequest request) {

    }

    @Override
    public void commandCallback(String suitId, HttpServletRequest request) {

    }
}

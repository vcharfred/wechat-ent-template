package top.vchar.wechat.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.vchar.wechat.bean.CallbackMsg;
import top.vchar.wechat.config.BizException;
import top.vchar.wechat.config.EntWxSuiteConfig;
import top.vchar.wechat.enums.ApiCode;
import top.vchar.wechat.feign.EntWxClient;
import top.vchar.wechat.service.command.EntWxCommandCallbackFactory;
import top.vchar.wechat.util.WxBizMsgCrypt;
import top.vchar.wechat.util.XMLParse;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * <p> 企业微信第三方应用业务逻辑 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/15
 */
@Slf4j
@Service
public class EntWxSuiteServiceImpl implements IEntWxSuiteService{

    private final EntWxSuiteConfig entWxSuiteConfig;
    private final EntWxCommandCallbackFactory entWxCommandCallbackFactory;
    private final EntWxClient entWxClient;

    public EntWxSuiteServiceImpl(EntWxSuiteConfig entWxSuiteConfig, EntWxCommandCallbackFactory entWxCommandCallbackFactory, EntWxClient entWxClient) {
        this.entWxSuiteConfig = entWxSuiteConfig;
        this.entWxCommandCallbackFactory = entWxCommandCallbackFactory;
        this.entWxClient = entWxClient;
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
        String echoStr = request.getParameter("echostr").replace(" ", "+");

        log.info("收到回调验证通知: msgSignature:{}, timestamp:{}, nonce:{}, echostr:{}", msgSignature, timestamp, nonce, echoStr);
        WxBizMsgCrypt wxBizMsgCrypt = entWxSuiteConfig.getWxBizMsgCrypt(suitId);
        return wxBizMsgCrypt.verifyUrl(msgSignature, timestamp, nonce, echoStr);
    }

    @Override
    public void dataCallback(String suitId, HttpServletRequest request) {
        CallbackMsg callbackMsg = getCallbackMsg(request);
        log.info("收到企业微信数据回调:{}", callbackMsg);
        String toUserName = XMLParse.extract(callbackMsg.getBody(), "ToUserName");
        WxBizMsgCrypt wxBizMsgCrypt = entWxSuiteConfig.getWxBizMsgCrypt(suitId, toUserName);
        String body = wxBizMsgCrypt.decryptMsg(callbackMsg);
        log.info("企业微信数据回调解密结果：{}", body);
    }

    @Override
    public void commandCallback(String suitId, HttpServletRequest request) {
        CallbackMsg callbackMsg = getCallbackMsg(request);
        log.info("收到企业微信指令回调：{}", callbackMsg);
        String toUserName = XMLParse.extract(callbackMsg.getBody(), "ToUserName");
        WxBizMsgCrypt wxBizMsgCrypt = entWxSuiteConfig.getWxBizMsgCrypt(suitId, toUserName);
        String body = wxBizMsgCrypt.decryptMsg(callbackMsg);
        log.info("企业微信指令回调解密结果：{}", body);
        // 根据消息类型处理对应的消息
        String infoType = XMLParse.extract(body, "InfoType");
        this.entWxCommandCallbackFactory.getCommandCallback(infoType).dealCallback(body);
    }

    @Override
    public String getUserinfo3rd(String suiteId, String code) {
        return this.entWxClient.getUserinfo3rd(suiteId, code);
    }

    /**
     * 解析请求报文
     * @param request 请求对象
     * @return 返回请求报文
     */
    private CallbackMsg getCallbackMsg(HttpServletRequest request){
        String msgSignature = request.getParameter("msg_signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        StringBuilder body = new StringBuilder();
        try(BufferedReader br=new BufferedReader(request.getReader())){
            String str;
            while ((str= br.readLine())!=null){
                body.append(str);
            }
        }catch (Exception e){
            log.error("读取企业微信数据回调body异常", e);
            throw new BizException(ApiCode.SERVER_ERROR, "读取请求body异常");
        }
        return CallbackMsg.builder()
                .msgSignature(msgSignature)
                .timestamp(timestamp)
                .nonce(nonce)
                .body(body.toString())
                .build();
    }

}

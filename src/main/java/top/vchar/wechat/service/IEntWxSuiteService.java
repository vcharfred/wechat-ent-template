package top.vchar.wechat.service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 企业微信第三方应用业务逻辑接口 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/15
 */
public interface IEntWxSuiteService {

    /**
     * 回调验证
     * @param suitId 应用ID
     * @param request 请求对象
     * @return 返回需要返回的字符串
     */
    String verifyCallback(String suitId, HttpServletRequest request);

    /**
     * 数据回调处理
     * @param suitId 应用ID
     * @param request 请求对象
     */
    void dataCallback(String suitId, HttpServletRequest request);

    /**
     * 指令回调处理
     * @param suitId 应用ID
     * @param request 请求对象
     */
    void commandCallback(String suitId, HttpServletRequest request);

    String getUserinfo3rd(String suiteId, String code);
}

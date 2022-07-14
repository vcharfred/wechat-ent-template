package top.vchar.wechat.service;

import org.springframework.stereotype.Service;

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

    @Override
    public String verifyCallback(String suitId, HttpServletRequest request) {
        return null;
    }

    @Override
    public void dataCallback(String suitId, HttpServletRequest request) {

    }

    @Override
    public void commandCallback(String suitId, HttpServletRequest request) {

    }
}

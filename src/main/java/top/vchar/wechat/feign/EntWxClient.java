package top.vchar.wechat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * <p> 企业微信请求对象 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/8/8
 */
@FeignClient(name = "entWxClient", path = "https://qyapi.weixin.qq.com")
public interface EntWxClient {

    /**
     * 获取第三方应用凭证
     *
     * @param map 参数
     * @return 返回获取第三方应用凭证
     */
    @PostMapping("/cgi-bin/service/get_suite_token")
    String getSuiteToken(@RequestBody Map<String, String> map);


    @PostMapping("/cgi-bin/service/get_permanent_code?suite_access_token={suiteToken}")
    String getPermanentCode(@PathVariable("suiteToken") String suiteToken, @RequestBody Map<String, String> params);


}

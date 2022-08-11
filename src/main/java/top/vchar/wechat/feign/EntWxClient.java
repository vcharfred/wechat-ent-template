package top.vchar.wechat.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 获取企业永久授权码
     * @param suiteToken 应用token
     * @param params 参数
     * @return 返回永久授权码
     */
    @PostMapping("/cgi-bin/service/get_permanent_code?suite_access_token={suiteToken}")
    String getPermanentCode(@PathVariable("suiteToken") String suiteToken, @RequestBody Map<String, String> params);

    /**
     * 通过授权code拉取用户信息
     * @param suiteToken 应用token
     * @param code 授权code
     * @return 返回用户信息
     */
    @GetMapping("/cgi-bin/service/getuserinfo3rd?suite_access_token={suiteToken}&code={code}")
    String getUserinfo3rd(@PathVariable("suiteToken") String suiteToken, @PathVariable("code") String code);
}

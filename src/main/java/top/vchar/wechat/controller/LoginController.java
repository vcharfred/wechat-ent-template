package top.vchar.wechat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import top.vchar.wechat.service.IEntWxSuiteService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * <p> 登陆 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/8/11
 */
@Controller
public class LoginController {

    @Autowired
    private IEntWxSuiteService entWxSuiteService;

    private final static String DOMAIN = "https://blog.vchar.top";

    @GetMapping("/login")
    public ModelAndView login(@RequestParam(value = "suiteId", required = false) String suiteId, @RequestParam(value = "code", required = false) String code){
        ModelAndView mv=new ModelAndView();
        mv.addObject("suiteId",suiteId);
        mv.addObject("code", code);
        mv.addObject("login", DOMAIN+"/wechat/doLogin?suiteId="+suiteId+"&code="+code);
        mv.setViewName("index.html");
        return mv;
    }

    /**
     * 企业微信授权链接拼接
     */
    @GetMapping("/oauth2")
    public ModelAndView oauth2(String suiteId){
        String redirectUri = URLEncoder.encode(DOMAIN+"/wechat/login?suiteId="+suiteId, StandardCharsets.UTF_8);
        String authUrl = String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"
                , suiteId, redirectUri);

        ModelAndView mv=new ModelAndView();
        mv.addObject("auth_url", authUrl);
        mv.setViewName("login.html");
        return mv;
    }

    @ResponseBody
    @GetMapping("/doLogin")
    public String getUserinfo3rd(@RequestParam("suiteId") String suiteId, @RequestParam("code") String code){
        return entWxSuiteService.getUserinfo3rd(suiteId, code);
    }

}

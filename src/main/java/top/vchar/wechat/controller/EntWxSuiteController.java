package top.vchar.wechat.controller;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.vchar.wechat.service.IEntWxSuiteService;

import javax.servlet.http.HttpServletRequest;

/**
 * <p> 企业微信第三方应用 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/14
 */
@RequestMapping("/ent/suite")
@RestController
public class EntWxSuiteController {

    private final IEntWxSuiteService entWxSuiteService;

    public EntWxSuiteController(IEntWxSuiteService entWxSuiteService) {
        this.entWxSuiteService = entWxSuiteService;
    }

    /**
     * 数据回调
     * @param suitId 应用ID
     * @return 返回结果
     */
    @RequestMapping("/callback/data/{suitId}")
    public String dataCallback(@PathVariable String suitId, HttpServletRequest request){
        if(HttpMethod.GET.matches(request.getMethod())){
            return this.entWxSuiteService.verifyCallback(suitId, request);
        }else if(HttpMethod.POST.matches(request.getMethod())){
            this.entWxSuiteService.dataCallback(suitId, request);
            return "success";
        }else {
            return "success";
        }
    }

    /**
     * 指令回调
     * @param suitId 应用ID
     * @return 返回结果
     */
    @RequestMapping("/callback/command/{suitId}")
    public String commandCallback(@PathVariable String suitId, HttpServletRequest request){
        if(HttpMethod.GET.matches(request.getMethod())){
            return this.entWxSuiteService.verifyCallback(suitId, request);
        }else if(HttpMethod.POST.matches(request.getMethod())){
            this.entWxSuiteService.commandCallback(suitId, request);
            return "success";
        }else {
            return "success";
        }
    }

}

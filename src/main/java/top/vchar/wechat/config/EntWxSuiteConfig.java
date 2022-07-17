package top.vchar.wechat.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import top.vchar.wechat.bean.EntWxSuite;

import java.util.List;
import java.util.Optional;

/**
 * <p> 企业微信第三方应用配置 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/18
 */
@Component
@ConfigurationProperties(prefix = "wx.ent")
public class EntWxSuiteConfig {

    private List<EntWxSuite> suites;

    public EntWxSuite getEntWxSuite(String suiteId){
        if(StringUtils.isBlank(suiteId)){
            return null;
        }
        Optional<EntWxSuite> optional = suites.stream().filter(p->p.getSuiteId().equals(suiteId)).findAny();
        return optional.orElse(null);
    }
}

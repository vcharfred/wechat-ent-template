package top.vchar.wechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * <p> 启动类 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/10
 */
@EnableFeignClients
@SpringBootApplication
public class WechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
    }
}

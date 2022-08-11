package top.vchar;

import org.junit.jupiter.api.Test;
import top.vchar.wechat.bean.EntWxSuite;
import top.vchar.wechat.config.EntWxSuiteConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>  TODO 功能描述 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/8/7
 */
public class MyTest {

    @Test
    public void demo(){
        List<EntWxSuite> list = new ArrayList<>();
        EntWxSuite config1 = new EntWxSuite();
        config1.setSuiteId("1");
        config1.setSuiteTicket("100");
        list.add(config1);

        EntWxSuite config2 = new EntWxSuite();
        config2.setSuiteId("2");
        config2.setSuiteTicket("200");
        list.add(config2);

        System.out.println(list);


        System.out.println(list);
    }
}

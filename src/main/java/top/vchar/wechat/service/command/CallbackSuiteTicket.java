package top.vchar.wechat.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.vchar.wechat.enums.CommandCallbackType;

/**
 * <p> suite_ticket 指令回调 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/24
 */
@Slf4j
@Service
public class CallbackSuiteTicket implements EntWxCommandCallback {

    @Override
    public CommandCallbackType getType() {
        return CommandCallbackType.SUITE_TICKET;
    }

    @Override
    public void dealCallback(String body) {

    }

}

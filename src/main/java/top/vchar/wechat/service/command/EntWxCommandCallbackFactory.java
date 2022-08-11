package top.vchar.wechat.service.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.vchar.wechat.config.BizException;
import top.vchar.wechat.enums.ApiCode;
import top.vchar.wechat.enums.CommandCallbackType;

/**
 * <p> 指令回调工程类 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/24
 */
@Slf4j
@Service
public class EntWxCommandCallbackFactory {

    @Autowired
    private EntWxCommandCallback[] commandCallbacks;

    public EntWxCommandCallback getCommandCallback(String type){
        CommandCallbackType callbackType = CommandCallbackType.valueOf(type);
        for(EntWxCommandCallback commandCallback:commandCallbacks){
            if(callbackType==commandCallback.getType()){
                return commandCallback;
            }
        }
        log.info("未知的通知回调类型:{} ", type);
        throw new BizException(ApiCode.ENT_WX_COMMAND_ERROR);
    }


}

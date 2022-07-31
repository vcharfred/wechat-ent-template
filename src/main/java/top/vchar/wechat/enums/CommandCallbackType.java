package top.vchar.wechat.enums;

import top.vchar.wechat.config.BizException;

/**
 * <p> 指令回调类型 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/24
 */
public enum CommandCallbackType {

    /**
     * suite_ticket回调
     */
    SUITE_TICKET("suite_ticket");

    private final String command;

    CommandCallbackType(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static CommandCallbackType parse(String command){
        CommandCallbackType[] callbackTypes = CommandCallbackType.values();
        for(CommandCallbackType callbackType:callbackTypes){
            if(callbackType.getCommand().equals(command)){
                return callbackType;
            }
        }
        throw new BizException(ApiCode.ENT_WX_COMMAND_ERROR);
    }
}

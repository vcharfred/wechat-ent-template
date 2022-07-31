package top.vchar.wechat.service.command;

import top.vchar.wechat.enums.CommandCallbackType;

/**
 * <p> 企业微信指令回调 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/24
 */
public interface EntWxCommandCallback {

    /**
     * 回调消息类型
     *
     * @return 返回回调类型
     */
    CommandCallbackType getType();

    /**
     * 处理指令回调信息
     * @param body 消息内容
     */
    void dealCallback(String body);
}

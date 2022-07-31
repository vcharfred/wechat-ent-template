package top.vchar.wechat.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> 企业微信回调信息 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/24
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CallbackMsg {

    /**
     * 签名
     */
     private String msgSignature;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 随机字符
     */
    private String nonce;

    /**
     * 请求body
     */
    private String body;

    @Override
    public String toString() {
        return "{" +
                "msgSignature: " + msgSignature +
                ", timestamp: " + timestamp +
                ", nonce: " + nonce +
                ", body: " + body +
                '}';
    }
}

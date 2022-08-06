package top.vchar.wechat.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * <p> 企业微信第三方应用配置 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/18
 */
@Data
public class EntWxSuite implements Serializable {

    /**
     * 应用ID
     */
    private String suiteId;

    /**
     * 应用secret
     */
    private String secret;

    /**
     * 应用配置的token
     */
    private String token;

    /**
     * 应用配置的aesKey
     */
    private String encodingAesKey;

    /**
     * 所属企业ID
     */
    private String corpId;

    /**
     * 应用的ticket
     */
    private String suiteTicket;

}

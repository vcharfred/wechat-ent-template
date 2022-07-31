package top.vchar.wechat.enums;

import org.springframework.http.HttpStatus;

/**
 * <p> api response code </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/14
 */
public enum ApiCode {

    /**
     * success
     */
    SUCCESS("S000", "成功", HttpStatus.OK),

    /**
     * server error
     */
    SERVER_ERROR("W001", "服务异常", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * 参数不正确
     */
    PARAM_ERROR("W002", "参数不正确", HttpStatus.NOT_ACCEPTABLE),

    /**
     * 企业微信 签名验证错误
     */
    ENT_WX_VALIDATE_SIGNATURE_ERROR("W003", "签名验证错误", HttpStatus.NOT_ACCEPTABLE),
    /**
     * 企业微信 xml解析失败
     */
    ENT_WX_PARSE_XML_ERROR("W004", "xml解析失败", HttpStatus.BAD_REQUEST),
    /**
     * 企业微信 sha加密生成签名失败
     */
    ENT_WX_COMPUTE_SIGNATURE_ERROR("W005", "sha加密生成签名失败", HttpStatus.BAD_REQUEST),
    /**
     * 企业微信 SymmetricKey非法
     */
    ENT_WX_ILLEGAL_AES_KEY("W006", "SymmetricKey非法", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 企业微信 corpid校验失败
     */
    ENT_WX_VALIDATE_CORPID_ERROR("W007", "corpid校验失败", HttpStatus.NOT_ACCEPTABLE),
    /**
     * 企业微信 aes加密失败
     */
    ENT_WX_ENCRYPT_AES_ERROR("W008", "aes加密失败", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 企业微信 aes解密失败
     */
    ENT_WX_DECRYPT_AES_ERROR("W009", "aes解密失败", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 企业微信 解密后得到的buffer非法
     */
    ENT_WX_ILLEGAL_BUFFER("W010", "解密后得到的buffer非法", HttpStatus.BAD_REQUEST),
    /**
     * 企业微信 base64加密错误
     */
    ENT_WX_ENCODE_BASE64_ERROR("W011", "base64加密错误", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 企业微信 base64解密错误
     */
    ENT_WX_DECODE_BASE64_ERROR("W012", "base64解密错误", HttpStatus.INTERNAL_SERVER_ERROR),
    /**
     * 企业微信 xml生成失败
     */
    ENT_WX_GEN_RETURN_XML_ERROR("W013", "xml生成失败", HttpStatus.INTERNAL_SERVER_ERROR),

    /**
     * 企业微信 指令回调消息类型处理方式未实现
     */
    ENT_WX_COMMAND_ERROR("W014", "不支持的消息类型", HttpStatus.NOT_ACCEPTABLE),
    ;


    private final String code;

    private final String msg;

    private final HttpStatus status;


    ApiCode(String code, String msg, HttpStatus status){
        this.code = code;
        this.msg = msg;
        this.status = status;
    }

    /**
     * Return the  string value of this api code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Return the string value of this api msg.
     */
    public String getMsg() {
        return msg;
    }

    public HttpStatus getStatus() {
        return status;
    }

    /**
     * Resolve the given api code to an {@code ApiCode}, if possible.
     *
     * @param codeValue the Api code (potentially non-standard)
     * @return the corresponding {@code ApiCode}, or {@code null} if not found
     */
    public static ApiCode resolve(String codeValue) {
        for (ApiCode code : values()) {
            if (code.code.equals(codeValue)) {
                return code;
            }
        }
        return null;
    }
}

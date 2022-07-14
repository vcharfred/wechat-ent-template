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
    PARAM_ERROR("W002", "参数不正确", HttpStatus.BAD_REQUEST);


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

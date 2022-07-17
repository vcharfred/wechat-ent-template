package top.vchar.wechat.config;

import org.springframework.http.HttpStatus;
import top.vchar.wechat.enums.ApiCode;

/**
 * <p> 自定义异常 </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/14
 */
public class BizException extends RuntimeException{

    /**
     * 默认响应状态码
     */
    private ApiCode code = ApiCode.SUCCESS;

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(ApiCode apiCode, Throwable cause) {
        super(apiCode.getMsg(), cause);
    }

    public BizException(ApiCode apiCode) {
        super(apiCode.getMsg());
        this.code = apiCode;
    }

    public BizException(ApiCode apiCode, String message) {
        super(message);
        this.code = apiCode;
    }

    public HttpStatus getStatus() {
        return code.getStatus();
    }

    public ApiCode getCode() {
        return code;
    }
}

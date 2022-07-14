package top.vchar.wechat.vo;

import lombok.Data;
import top.vchar.wechat.config.BizException;
import top.vchar.wechat.enums.ApiCode;

/**
 * <p> api response pojo </p>
 *
 * @author vchar fred
 * @version 1.0
 * @create_date 2022/7/14
 */
@Data
public class ApiResponse<T> {

    private String code = ApiCode.SUCCESS.getCode();

    private String message = ApiCode.SUCCESS.getMsg();

    private T data;

    public ApiResponse() {
    }

    public ApiResponse(T data) {
        this.data = data;
    }

    public ApiResponse(ApiCode code, String message, T data) {
        this.code = code.getCode();
        this.message = message;
        this.data = data;
    }

    public ApiResponse(ApiCode code, String message) {
        this.code = code.getCode();
        this.message = message;
    }

    public ApiResponse(BizException e) {
        this.code = e.getCode().getCode();
        this.message = e.getMessage();
    }
}

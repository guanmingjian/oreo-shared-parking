package cn.oreo.common.util.exception;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author GuanMingJian
 * @since 2021/2/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PentiumException extends RuntimeException{

    @ApiModelProperty("状态码")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String message;

}

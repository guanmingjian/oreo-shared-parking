package cn.oreo.auth.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @NotBlank(message="{XXX}") 中XXX关联到
 * ValidationMessages.properties中的XXX
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
public class BindUser implements Serializable {

    private static final long serialVersionUID = -3890998115990166651L;

    @NotBlank(message = "{required}")
    private String bindUsername;
    @NotBlank(message = "{required}")
    private String bindPassword;
}

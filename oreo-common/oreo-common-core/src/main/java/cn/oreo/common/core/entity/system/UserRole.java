package cn.oreo.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@TableName("t_user_role")
public class UserRole implements Serializable {

    private static final long serialVersionUID = -3166012934498268403L;

    @TableField(value = "USER_ID")
    private Long userId;

    @TableField(value = "ROLE_ID")
    private Long roleId;

}
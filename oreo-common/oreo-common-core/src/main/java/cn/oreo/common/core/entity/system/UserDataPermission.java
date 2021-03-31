package cn.oreo.common.core.entity.system;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@TableName("t_user_data_permission")
public class UserDataPermission {

    //@TableName(value = "USER_ID")
    @TableField("USER_ID")
    private Long userId;

    @TableField("DEPT_ID")
    private Long deptId;

}
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
@TableName("t_role_menu")
@Data
public class RoleMenu implements Serializable {

    private static final long serialVersionUID = -7573904024872252113L;

    @TableField(value = "ROLE_ID")
    private Long roleId;
    @TableField(value = "MENU_ID")
    private Long menuId;
}
package cn.oreo.common.core.entity;


import cn.oreo.common.core.entity.system.Dept;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DeptTree extends Tree<Dept> {

    private Integer orderNum;
}

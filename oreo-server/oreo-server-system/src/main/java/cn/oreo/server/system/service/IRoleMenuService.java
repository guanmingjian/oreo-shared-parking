package cn.oreo.server.system.service;

import cn.oreo.common.core.entity.system.RoleMenu;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 删除角色菜单关联数据
     *
     * @param roleIds 角色id
     */
    void deleteRoleMenusByRoleId(String[] roleIds);

    /**
     * 删除角色菜单关联数据
     *
     * @param menuIds 菜单id
     */
    void deleteRoleMenusByMenuId(String[] menuIds);

    /**
     * 获取角色对应的菜单列表
     *
     * @param roleId 角色id
     * @return 菜单列表
     */
    List<RoleMenu> getRoleMenusByRoleId(String roleId);
}

package cn.oreo.server.system.mapper;


import cn.oreo.common.core.entity.system.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取用户权限集
     *
     * @param username 用户名
     * @return 用户权限集
     */
    List<Menu> findUserPermissions(String username);

    /**
     * 获取用户菜单
     *
     * @param username 用户名
     * @return 用户菜单
     */
    List<Menu> findUserMenus(String username);
}
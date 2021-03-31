package cn.oreo.auth.mapper;

import cn.oreo.common.core.entity.system.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 获取用户权限集
     *
     * @param username 用户名
     * @return 权限集合
     */
    List<Menu> findUserPermissions(String username);
}
package cn.oreo.server.system.mapper;

import cn.oreo.common.core.entity.system.SystemUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */

// 继承BaseMapper，传入对象类型
public interface UserMapper extends BaseMapper<SystemUser> {

    /**
     * 查找用户详细信息
     *
     * @param page 分页对象
     * @param user 用户对象，用于传递查询条件
     * @param <T>  type
     * @return Ipage
     */
    // 他这里负责很多关联查询，所以复杂查询的时候可以手写mapper.xml文件
    <T> IPage<SystemUser> findUserDetailPage(Page<T> page, @Param("user") SystemUser user);

    /**
     * 查找用户详细信息
     *
     * @param user 用户对象，用于传递查询条件
     * @return List<User>
     */
    List<SystemUser> findUserDetail(@Param("user") SystemUser user);

}

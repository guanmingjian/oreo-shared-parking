package cn.oreo.common.service.mapper;

import cn.oreo.common.model.entity.po.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {
}

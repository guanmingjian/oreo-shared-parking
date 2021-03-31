package cn.oreo.server.pay.mapper;

import cn.oreo.common.model.entity.po.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderMapper extends BaseMapper<Order> {
}

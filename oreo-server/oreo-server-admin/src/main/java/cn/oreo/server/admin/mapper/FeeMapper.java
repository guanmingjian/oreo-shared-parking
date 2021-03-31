package cn.oreo.server.admin.mapper;

import cn.oreo.common.model.entity.vo.FeeVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * @author GuanMingJian
 * @since 2021/3/1
 */
public interface FeeMapper {

    /**
     * 自定义多表查询
     */

    IPage<FeeVo> selectFeeVo(Page<?> page, Long communityId);
}

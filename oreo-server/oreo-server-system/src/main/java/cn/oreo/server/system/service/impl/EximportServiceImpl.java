package cn.oreo.server.system.service.impl;

import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.system.Eximport;
import cn.oreo.server.system.mapper.EximportMapper;
import cn.oreo.server.system.properties.OreoServerSystemProperties;
import cn.oreo.server.system.service.IEximportService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class EximportServiceImpl extends ServiceImpl<EximportMapper, Eximport> implements IEximportService {

    private final OreoServerSystemProperties properties;

    @Override
    public IPage<Eximport> findEximports(QueryRequest request, Eximport eximport) {
        Page<Eximport> page = new Page<>(request.getPageNum(), request.getPageSize());
        return this.page(page, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchInsert(List<Eximport> list) {
        this.saveBatch(list, properties.getBatchInsertMaxNum());
    }

}

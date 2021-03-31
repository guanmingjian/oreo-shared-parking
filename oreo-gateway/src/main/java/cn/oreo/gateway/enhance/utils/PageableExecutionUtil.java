package cn.oreo.gateway.enhance.utils;

import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.constant.OreoConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Flux;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 */
public class PageableExecutionUtil {

    public static <OREO> Flux<OREO> getPages(Query query, QueryRequest request, Class<OREO> clazz,
                                             ReactiveMongoTemplate template) {
        Sort sort = Sort.by("id").descending();
        if (StringUtils.isNotBlank(request.getField()) && StringUtils.isNotBlank(request.getOrder())) {
            sort = OreoConstant.ORDER_ASC.equals(request.getOrder()) ?
                    Sort.by(request.getField()).ascending() :
                    Sort.by(request.getField()).descending();
        }
        Pageable pageable = PageRequest.of(request.getPageNum(), request.getPageSize(), sort);
        return template.find(query.with(pageable), clazz);
    }
}

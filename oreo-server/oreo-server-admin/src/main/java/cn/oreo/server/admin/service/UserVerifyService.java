package cn.oreo.server.admin.service;

import cn.oreo.common.model.entity.po.UserVerify;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
public interface UserVerifyService extends IService<UserVerify> {

    /**
     * 查看审核列表
     * @param queryRequest
     * @param userVerify
     * @return
     */
    OreoResponse userVerifyList(QueryRequest queryRequest, UserVerify userVerify);

    /**
     * 审核
     * @param userVerifyId
     * @param isPass
     * @return
     */
    OreoResponse userVerifyHandle(Long userVerifyId, Boolean isPass);
}

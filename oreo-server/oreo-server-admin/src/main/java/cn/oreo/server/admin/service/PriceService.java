package cn.oreo.server.admin.service;

import cn.oreo.common.model.entity.dto.server.admin.TimeFeeDto;
import cn.oreo.common.model.entity.dto.server.admin.UnitFeeDto;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PriceService {

    OreoResponse setEarnestPrice(BigDecimal earnestPrice, Long communityId);

    OreoResponse editEarnestPrice(BigDecimal earnestPrice, Long communityId);

    OreoResponse setTimeFee(TimeFeeDto timeFeeDto);

    OreoResponse editTimeFee(TimeFeeDto timeFeeDto);

    OreoResponse setUnitFee(UnitFeeDto unitFeeDto);

    OreoResponse editUnitFee(UnitFeeDto unitFeeDto);

    OreoResponse queryFee(QueryRequest queryRequest, Long communityId);
}

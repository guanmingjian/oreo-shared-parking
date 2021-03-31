package cn.oreo.server.admin.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.oreo.common.model.entity.dto.server.admin.TimeFeeDto;
import cn.oreo.common.model.entity.dto.server.admin.UnitFeeDto;
import cn.oreo.common.model.entity.po.CommunityEarnestPrice;
import cn.oreo.common.model.entity.po.TimeFee;
import cn.oreo.common.model.entity.po.UnitFee;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.model.entity.vo.FeeVo;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.mapper.EarnestPriceMapper;
import cn.oreo.server.admin.mapper.FeeMapper;
import cn.oreo.server.admin.mapper.TimeFeeMapper;
import cn.oreo.server.admin.mapper.UnitFeeMapper;
import cn.oreo.server.admin.service.PriceService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PriceServiceServiceImpl implements PriceService {

    @Autowired
    private EarnestPriceMapper earnestPriceMapper;

    @Autowired
    private TimeFeeMapper timeFeeMapper;

    @Autowired
    private UnitFeeMapper unitFeeMapper;

    @Resource
    private FeeMapper feeMapper;

    @Override
    public OreoResponse setEarnestPrice(BigDecimal price, Long communityId) {
        QueryWrapper<CommunityEarnestPrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id",communityId);
        CommunityEarnestPrice earnestPrice = earnestPriceMapper.selectOne(queryWrapper);
        if(earnestPrice != null) {
            return new OreoResponse().message("社区已存在，请编辑他!");
        }
        // 不存在则插入数据库
        System.out.println("存入数据库.....");
        CommunityEarnestPrice communityEarnestPrice = new CommunityEarnestPrice();
        communityEarnestPrice.setAddTime(new Date());
        communityEarnestPrice.setUpdateTime(new Date());
        communityEarnestPrice.setEarnestPrice(price);
        communityEarnestPrice.setCommunityId(communityId);
        earnestPriceMapper.insert(communityEarnestPrice);
        return new OreoResponse().message("存入数据库成功").data(communityEarnestPrice);
    }

    @Override
    public OreoResponse editEarnestPrice(BigDecimal earnestPrice, Long communityId) {
        QueryWrapper<CommunityEarnestPrice> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("community_id",communityId);
        CommunityEarnestPrice communityEarnestPrice = earnestPriceMapper.selectOne(queryWrapper);
        communityEarnestPrice.setUpdateTime(new Date());
        communityEarnestPrice.setEarnestPrice(earnestPrice);
        earnestPriceMapper.updateById(communityEarnestPrice);
        return new OreoResponse().message("修改成功").data(communityEarnestPrice);
    }

    @Override
    public OreoResponse setTimeFee(@Valid TimeFeeDto timeFeeDto) {
        QueryWrapper<TimeFee> timeFeeQueryWrapper = new QueryWrapper<>();
        timeFeeQueryWrapper.eq("community_id",timeFeeDto.getCommunityId());
        TimeFee timeFee = timeFeeMapper.selectOne(timeFeeQueryWrapper);
        if(timeFee != null){
            return new OreoResponse().message("社区的收费标准已存在，请编辑它");
        }
        System.out.println("插入数据库....");
        TimeFee timeFee1 = new TimeFee();
        timeFee1.setAddTime(new Date());
        timeFee1.setUpdateTime(new Date());
        timeFee1.setDeleted(0);
        timeFee1.setCommunityId(timeFeeDto.getCommunityId());
        if(StrUtil.isNotBlank(timeFeeDto.getTimeDescription())){
            timeFee1.setTimeDescription(timeFeeDto.getTimeDescription());
        }
        timeFee1.setTimeFee(timeFeeDto.getTimeFee());
        timeFee1.setTime(timeFeeDto.getTime());
        // unitFee
        if(timeFeeDto.getExtraUnitFee()!=null){
            timeFee1.setExtraUnitFee(timeFeeDto.getExtraUnitFee());
        }
        timeFeeMapper.insert(timeFee1);
        return new OreoResponse().message("存入数据库成功").data(timeFee1);
    }

    @Override
    public OreoResponse editTimeFee(@Valid TimeFeeDto timeFeeDto) {
        QueryWrapper<TimeFee> timeFeeQueryWrapper = new QueryWrapper<>();
        timeFeeQueryWrapper.eq("community_id",timeFeeDto.getCommunityId());
        TimeFee timeFee = timeFeeMapper.selectOne(timeFeeQueryWrapper);

        timeFee.setUpdateTime(new Date());
        if(timeFeeDto.getTimeDescription()!=null){
            timeFee.setTimeDescription(timeFeeDto.getTimeDescription());
        }
        timeFee.setTimeFee(timeFeeDto.getTimeFee());
        timeFee.setTime(timeFeeDto.getTime());
        // unitFee
        if(timeFeeDto.getExtraUnitFee()!=null){
            timeFee.setExtraUnitFee(timeFeeDto.getExtraUnitFee());
        }
        timeFeeMapper.updateById(timeFee);
        return OreoResponse.success().data(timeFee);
    }

    @Override
    public OreoResponse setUnitFee(@Valid UnitFeeDto unitFeeDto) {

        QueryWrapper<UnitFee> unitFeeDtoQueryWrapper = new QueryWrapper<>();
        unitFeeDtoQueryWrapper.eq("community_id",unitFeeDto.getCommunityId());
        UnitFee unitFee = unitFeeMapper.selectOne(unitFeeDtoQueryWrapper);

        if(unitFee != null){
            return new OreoResponse().message("社区的收费标准已存在，请编辑它");
        }
        UnitFee unitFee1 = new UnitFee();
        unitFee1.setAddTime(new Date());
        unitFee1.setUpdateTime(new Date());
        unitFee1.setDeleted(0);
        unitFee1.setCommunityId(unitFeeDto.getCommunityId());
        if(StrUtil.isNotBlank(unitFeeDto.getUnitDescription())){
            unitFee1.setUnitDescription(unitFeeDto.getUnitDescription());
        }
        unitFee1.setUnitFee(unitFeeDto.getUnitFee());
        unitFeeMapper.insert(unitFee1);
        return new OreoResponse().message("存入数据库成功").data(unitFee1);
    }

    @Override
    public OreoResponse editUnitFee(@Valid UnitFeeDto unitFeeDto) {
        QueryWrapper<UnitFee> unitFeeQueryWrapper = new QueryWrapper<>();
        unitFeeQueryWrapper.eq("community_id",unitFeeDto.getCommunityId());
        UnitFee unitFee = unitFeeMapper.selectOne(unitFeeQueryWrapper);

        unitFee.setUpdateTime(new Date());
        if(unitFeeDto.getUnitDescription()!=null){
            unitFee.setUnitDescription(unitFeeDto.getUnitDescription());
        }
        unitFee.setUnitFee(unitFeeDto.getUnitFee());
        unitFeeMapper.updateById(unitFee);
        return OreoResponse.success().data(unitFee);
    }

    @Override
    public OreoResponse queryFee(QueryRequest queryRequest, Long communityId) {

        // 分页配置
        Page<FeeVo> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        IPage<FeeVo> feeVoIPage = feeMapper.selectFeeVo(page, communityId);

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(feeVoIPage);

        return new OreoResponse().data(dataTable);
    }
}

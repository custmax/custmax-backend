package com.custmax.officialsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.custmax.officialsite.entity.subscription.Plan;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PlanMapper extends BaseMapper<Plan> {
}
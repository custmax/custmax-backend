package com.custmax.officialsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.custmax.officialsite.entity.SubscriptionDiscountPolicy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DiscountPolicyMapper extends BaseMapper<SubscriptionDiscountPolicy> {

}
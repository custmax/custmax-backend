package com.custmax.officialsite.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.custmax.officialsite.entity.payment.PaymentHistory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PaymentHistoryMapper extends BaseMapper<PaymentHistory> {
}
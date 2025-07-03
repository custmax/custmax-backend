package com.custmax.officialsite.service;

import com.custmax.officialsite.dto.payment.CreatePaymentIntentRequest;
import com.custmax.officialsite.dto.payment.PaymentRecordDTO;
import com.custmax.officialsite.entity.payment.PaymentHistory;
import com.custmax.officialsite.dto.payment.ConfirmPaymentRequest;
import java.util.List;
import java.util.Map;

public interface PaymentService {
    // 创建支付意图（如 Stripe PaymentIntent）
    Map<String, Object> createPaymentIntent(CreatePaymentIntentRequest request);

    // 确认支付并激活订阅
    Map<String, Object> confirmPayment(ConfirmPaymentRequest request);

    // 获取当前用户的支付历史
    List<PaymentRecordDTO> getCurrentUserPaymentHistory(Long userId);

    // 获取指定支付记录详情
    PaymentHistory getPaymentDetails(String paymentId);
}
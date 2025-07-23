package com.custmax.officialsite.service.payment;
import com.custmax.officialsite.dto.payment.*;
import com.custmax.officialsite.entity.payment.PaymentHistory;

import java.util.List;

public interface  PaymentService {
    CreatePaymentIntentResponse createPaymentIntent(CreatePaymentIntentRequest request);

    ConfirmPaymentResponse confirmPayment(String sessionId);

    List<GetPaymentDetailsResponse> getCurrentUserPaymentHistory(Long userId);

}
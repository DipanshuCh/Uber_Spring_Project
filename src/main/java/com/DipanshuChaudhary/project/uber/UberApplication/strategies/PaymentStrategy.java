package com.DipanshuChaudhary.project.uber.UberApplication.strategies;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Payment;

public interface PaymentStrategy {

    Double PLATFORM_COMMISSION = 0.3;

    void processPayment(Payment payment);

}

package com.DipanshuChaudhary.project.uber.UberApplication.services;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Payment;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Ride;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentStatus;

public interface PaymentService {

    void processPayment(Ride ride);

    Payment createNewPayment(Ride ride);

    void updatePaymentStatus(Payment payment, PaymentStatus status);

}

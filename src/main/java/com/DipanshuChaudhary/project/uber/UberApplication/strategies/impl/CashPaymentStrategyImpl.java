package com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Payment;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.TransactionMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.PaymentRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.WalletService;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategyImpl implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();

        double platformCommission = payment.getAmount() * PLATFORM_COMMISSION;

        walletService.deductMoneyFromWallet(driver.getUser(), platformCommission, null, payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);

    }
}

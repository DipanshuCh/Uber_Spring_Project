package com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Driver;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Payment;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Rider;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentStatus;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.TransactionMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.PaymentRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.WalletService;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class WalletPaymentStrategyImpl implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {

        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();

        walletService.deductMoneyFromWallet(rider.getUser(),
                payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);

        double driverCut = payment.getAmount() * (1 - PLATFORM_COMMISSION);

        walletService.addMoneyToWallet(driver.getUser(),driverCut,null,
                payment.getRide(), TransactionMethod.RIDE);

        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}










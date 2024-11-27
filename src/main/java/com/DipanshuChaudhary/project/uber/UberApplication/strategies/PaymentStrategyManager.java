package com.DipanshuChaudhary.project.uber.UberApplication.strategies;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.PaymentMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl.CashPaymentStrategyImpl;
import com.DipanshuChaudhary.project.uber.UberApplication.strategies.impl.WalletPaymentStrategyImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStrategyManager {

    private final WalletPaymentStrategyImpl walletPaymentStrategy;
    private final CashPaymentStrategyImpl cashPaymentStrategy;

    public PaymentStrategy paymentStrategy(PaymentMethod paymentMethod){
        return switch (paymentMethod){
            case WALLET -> walletPaymentStrategy;
            case CASH -> cashPaymentStrategy;
        };
    }

}

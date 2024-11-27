package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.WalletTransaction;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.WalletTransactionRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletTransactionServiceImpl implements WalletTransactionService {

    private final WalletTransactionRepository walletTransactionRepository;
    private final ModelMapper modelMapper;

    @Override
    public void createNewWalletTransactions(WalletTransaction walletTransaction) {
        walletTransactionRepository.save(walletTransaction);
    }
}

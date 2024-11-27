package com.DipanshuChaudhary.project.uber.UberApplication.services.implementation;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.Ride;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.User;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.Wallet;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.WalletTransaction;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.TransactionMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.TransactionType;
import com.DipanshuChaudhary.project.uber.UberApplication.exceptions.ResourceNotFoundException;
import com.DipanshuChaudhary.project.uber.UberApplication.repositories.WalletRepository;
import com.DipanshuChaudhary.project.uber.UberApplication.services.WalletService;
import com.DipanshuChaudhary.project.uber.UberApplication.services.WalletTransactionService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionService walletTransactionService;
    private final ModelMapper modelMapper;


    @Override
    @Transactional
    public Wallet addMoneyToWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() + amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                        .transactionId(transactionId)
                        .ride(ride)
                        .wallet(wallet)
                        .transactionType(TransactionType.CREDIT)
                        .transactionMethod(transactionMethod)
                        .build();

        walletTransactionService.createNewWalletTransactions(walletTransaction);

        walletRepository.save(wallet);
        return wallet;

    }

    @Override
    @Transactional
    public Wallet deductMoneyFromWallet(User user, Double amount, String transactionId, Ride ride, TransactionMethod transactionMethod) {
        Wallet wallet = findByUser(user);
        wallet.setBalance(wallet.getBalance() - amount);

        WalletTransaction walletTransaction = WalletTransaction.builder()
                .transactionId(transactionId)
                .ride(ride)
                .wallet(wallet)
                .transactionType(TransactionType.DEBIT)
                .transactionMethod(transactionMethod)
                .build();

//        walletTransactionService.createNewWalletTransactions(walletTransaction);

        wallet.getTransactions().add(walletTransaction);

        walletRepository.save(wallet);
        return wallet;
    }

    @Override
    public void withdrawAllMyMoneyFromWallet() {

    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found with id: "+walletId));
    }

    @Override
    public Wallet createNewWallet(User user) {
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        return walletRepository.save(wallet);

    }

    @Override
    public Wallet findByUser(User user) {
        return walletRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Wallet not found for user with id: +user.getId()"));
    }
}

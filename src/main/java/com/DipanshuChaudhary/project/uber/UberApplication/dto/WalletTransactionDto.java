package com.DipanshuChaudhary.project.uber.UberApplication.dto;

import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.TransactionMethod;
import com.DipanshuChaudhary.project.uber.UberApplication.entities.enums.TransactionType;
import lombok.Data;

import java.time.LocalDateTime;


@Data
public class WalletTransactionDto {

    private Long id;

    private Double amount;

    private TransactionType transactionType;

    private TransactionMethod transactionMethod;

    private RideDto ride;

    // if it is a banking related transaction
    private String transactionId;

    private WalletDto wallet;

    private LocalDateTime timeStamp;

}

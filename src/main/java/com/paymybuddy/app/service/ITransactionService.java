package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Transaction;

import java.util.List;

public interface ITransactionService {
    List<Transaction> findAllTransactionOfUser(Long userId);
    Transaction save(Transaction transaction);
}

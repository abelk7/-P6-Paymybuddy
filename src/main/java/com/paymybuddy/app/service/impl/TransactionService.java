package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.repository.TransactionRepository;
import com.paymybuddy.app.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service("transactionService")
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;

    @Override
    public List<Transaction> findAllTransactionOfUser(Long userId) {
        return transactionRepository.findAllTransactionOfUser(userId);
    }

    @Override
    public Transaction save(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}

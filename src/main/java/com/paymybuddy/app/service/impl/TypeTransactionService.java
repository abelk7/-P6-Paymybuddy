package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.TypeTransaction;
import com.paymybuddy.app.repository.TypeTransactionRepository;
import com.paymybuddy.app.service.ITypeTransactionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("typeTransactionService")
public class TypeTransactionService implements ITypeTransactionService {
    private static final Logger LOG = LoggerFactory.getLogger(TypeTransactionService.class);
    private final TypeTransactionRepository typeTransactionRepository;

    @Override
    public TypeTransaction findByLibelle(String libelle) {
        return typeTransactionRepository.findByLibelle(libelle);
    }

    @Override
    public TypeTransaction save(TypeTransaction typeTransaction) {
        return typeTransactionRepository.save(typeTransaction);
    }
}

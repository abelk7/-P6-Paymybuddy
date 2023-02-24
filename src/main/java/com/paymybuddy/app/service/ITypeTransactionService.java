package com.paymybuddy.app.service;

import com.paymybuddy.app.model.TypeTransaction;

public interface ITypeTransactionService {

    TypeTransaction findByLibelle(String libelle);
    TypeTransaction save(TypeTransaction typeTransaction);
}

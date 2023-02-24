package com.paymybuddy.app.repository;


import com.paymybuddy.app.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transactions t WHERE t.compte_emetteur_id=?1 ORDER BY id DESC", nativeQuery = true)
    List<Transaction> findAllTransactionOfUser(Long id);
    Transaction save(Transaction transaction);
}

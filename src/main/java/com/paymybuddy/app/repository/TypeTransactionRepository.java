package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeTransactionRepository extends JpaRepository<TypeTransaction, Long> {
    @Query(value = "SELECT * FROM typestransactions t WHERE t.libelle=?1", nativeQuery = true)
    TypeTransaction findByLibelle(String libelle);
    TypeTransaction save(TypeTransaction typeTransaction);
}

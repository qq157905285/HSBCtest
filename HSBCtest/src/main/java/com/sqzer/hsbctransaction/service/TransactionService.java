package com.sqzer.hsbctransaction.service;

import com.sqzer.hsbctransaction.model.Transaction;
import java.util.List;
import java.util.Optional;

public interface TransactionService {
    public List<Transaction> findPaginated(int page, int size, Transaction.TransactionType type);

    public Transaction create(Transaction tx);

    public Optional<Transaction> get(String id);
    public Optional<Transaction> update(String id, Transaction updated);

    public boolean delete(String id);


}

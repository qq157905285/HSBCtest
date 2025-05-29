package com.sqzer.hsbctransaction.service.impl;
import com.sqzer.hsbctransaction.model.Transaction;
import com.sqzer.hsbctransaction.service.TransactionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
@Service
public class TransactionServiceImpl implements TransactionService {

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public Transaction create(Transaction tx) {
        transactions.put(tx.getId(), tx);
        return tx;
    }

    @Override
    public Optional<Transaction> get(String id) {
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public Optional<Transaction> update(String id, Transaction updated) {
        if (transactions.containsKey(id)) {
            updated.setId(id);
            transactions.put(id, updated);
            return Optional.of(updated);
        }
        return Optional.empty();
    }

    @Override
    @CacheEvict(value = "transactions", allEntries = true)
    public boolean delete(String id) {
        return transactions.remove(id) != null;
    }

    @Override
    @Cacheable(value = "transactions", key = "#page + '-' + #size + '-' + #type")
    public List<Transaction> findPaginated(int page, int size, Transaction.TransactionType type) {
        List<Transaction> filtered = transactions.values().stream()
                .filter(tx -> type == null || tx.getType() == type)
                .sorted(Comparator.comparing(Transaction::getTimestamp).reversed())
                .toList();

        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, filtered.size());
        if (fromIndex >= filtered.size()) return Collections.emptyList();
        return filtered.subList(fromIndex, toIndex);
    }
}

package com.sqzer.hsbctransaction;

import com.sqzer.hsbctransaction.model.Transaction;
import com.sqzer.hsbctransaction.service.TransactionService;
import com.sqzer.hsbctransaction.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {

    private TransactionService service;

    @BeforeEach
    public void setup() {
        service = new TransactionServiceImpl();
    }

    @Test
    public void testCreateAndGetTransaction() {
        Transaction tx = new Transaction("Salary", new BigDecimal("5000"), Transaction.TransactionType.INCOME);
        service.create(tx);
        assertTrue(service.get(tx.getId()).isPresent());
    }

    @Test
    public void testUpdateTransaction() {
        Transaction tx = new Transaction("Bonus", new BigDecimal("300"), Transaction.TransactionType.INCOME);
        service.create(tx);
        tx.setAmount(new BigDecimal("400"));
        service.update(tx.getId(), tx);
        assertEquals("400", service.get(tx.getId()).get().getAmount().toPlainString());
    }

    @Test
    public void testDeleteTransaction() {
        Transaction tx = new Transaction("Expense", new BigDecimal("100"), Transaction.TransactionType.EXPENSE);
        service.create(tx);
        assertTrue(service.delete(tx.getId()));
        assertFalse(service.get(tx.getId()).isPresent());
    }

    @Test
    public void testPagination() {
        for (int i = 0; i < 20; i++) {
            service.create(new Transaction("Tx" + i, new BigDecimal(i), Transaction.TransactionType.TRANSFER));
        }
        List<Transaction> page = service.findPaginated(1, 5, null);
        assertEquals(5, page.size());
    }
}
package com.sqzer.hsbctransaction;

import com.sqzer.hsbctransaction.enums.TransactionType;
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
        Transaction tx = new Transaction("Salary", new BigDecimal("5000"), TransactionType.INCOME);
        service.create(tx);
        assertTrue(service.get(tx.getId()).isPresent(), "Transaction should exist after creation");
    }

    @Test
    public void testUpdateTransaction() {
        Transaction tx = new Transaction("Bonus", new BigDecimal("300"), TransactionType.INCOME);
        service.create(tx);
        tx.setAmount(new BigDecimal("400"));
        service.update(tx.getId(), tx);
        assertEquals("400", service.get(tx.getId()).get().getAmount().toPlainString(), "Amount should be updated");
    }

    @Test
    public void testDeleteTransaction() {
        Transaction tx = new Transaction("Expense", new BigDecimal("100"), TransactionType.EXPENSE);
        service.create(tx);
        assertTrue(service.delete(tx.getId()), "Transaction should be deleted successfully");
        assertFalse(service.get(tx.getId()).isPresent(), "Deleted transaction should not be found");
    }

    @Test
    public void testDeleteNonExistingTransaction() {
        assertFalse(service.delete("non-existing-id"), "Deleting non-existing transaction should return false");
    }

    @Test
    public void testPagination() {
        for (int i = 0; i < 20; i++) {
            service.create(new Transaction("Tx" + i, new BigDecimal(i), TransactionType.TRANSFER));
        }
        List<Transaction> page = service.findPaginated(1, 5, null); // 第二页
        assertEquals(5, page.size(), "Page size should be 5");
    }

    @Test
    public void testPaginationOutOfRange() {
        for (int i = 0; i < 3; i++) {
            service.create(new Transaction("Tx" + i, BigDecimal.ONE, TransactionType.TRANSFER));
        }
        List<Transaction> page = service.findPaginated(2, 5, null); // 超出页数
        assertTrue(page.isEmpty(), "Out-of-range page should return empty list");
    }

    @Test
    public void testPaginationWithFilter() {
        service.create(new Transaction("Salary", new BigDecimal("1000"), TransactionType.INCOME));
        service.create(new Transaction("Rent", new BigDecimal("800"), TransactionType.EXPENSE));
        service.create(new Transaction("Investment", new BigDecimal("2000"), TransactionType.INCOME));

        List<Transaction> incomeTxs = service.findPaginated(0, 10, TransactionType.INCOME);
        assertEquals(2, incomeTxs.size(), "Should return 2 INCOME transactions");
        assertTrue(incomeTxs.stream().allMatch(tx -> tx.getType() == TransactionType.INCOME),
                "All transactions should be INCOME type");
    }
}

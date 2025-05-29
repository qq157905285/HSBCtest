package com.sqzer.hsbctransaction.model;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import jakarta.validation.constraints.*;

@Data
public class Transaction {
    private String id;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Amount cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be positive")
    private BigDecimal amount;

    private LocalDateTime timestamp;

    @NotNull(message = "Type is required")
    private TransactionType type; // 新增类型字段

    public enum TransactionType {
        INCOME, EXPENSE, TRANSFER
    }

    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(String description, BigDecimal amount, TransactionType transactionType) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }
}

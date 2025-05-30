package com.sqzer.hsbctransaction.controller;

import com.sqzer.hsbctransaction.enums.TransactionType;
import com.sqzer.hsbctransaction.exception.ResourceNotFoundException;
import com.sqzer.hsbctransaction.model.ApiResponse;
import com.sqzer.hsbctransaction.model.Transaction;
import com.sqzer.hsbctransaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ApiResponse<List<Transaction>> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) TransactionType type
    ) {
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100;
        return ApiResponse.success(service.findPaginated(page, size, type));
    }

    @PostMapping
    public ApiResponse<Transaction> create(@Valid @RequestBody Transaction tx) {
        return ApiResponse.success(service.create(tx));
    }

    @PutMapping("/{id}")
    public ApiResponse<Transaction> update(@PathVariable String id, @Valid @RequestBody Transaction tx) {
        return ApiResponse.success(service.update(id, tx)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found")));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable String id) {
        if (!service.delete(id)) {
            throw new ResourceNotFoundException("Transaction not found");
        }
        return ApiResponse.success();
    }
}

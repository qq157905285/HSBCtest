package com.sqzer.hsbctransaction.controller;
import com.sqzer.hsbctransaction.model.Transaction;
import com.sqzer.hsbctransaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public List<Transaction> getPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Transaction.TransactionType type
    ) {
        return service.findPaginated(page, size, type);
    }

    @PostMapping
    public ResponseEntity<Transaction> create(@Valid @RequestBody Transaction tx) {
        return ResponseEntity.ok(service.create(tx));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody Transaction tx) {
        return service.update(id, tx)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        return service.delete(id)
                ? ResponseEntity.ok().build()
                : ResponseEntity.notFound().build();
    }
}

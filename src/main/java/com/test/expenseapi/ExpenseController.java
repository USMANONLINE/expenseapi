package com.test.expenseapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/api")
public class ExpenseController {
    @Autowired
    private ExpenseRepo repo;
    
    @GetMapping("expenses")
    public ResponseEntity<Iterable<Expense>> list() {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }
    
    @GetMapping("expense/{id}")
    public ResponseEntity<Expense> get(@PathVariable Long id) {
        Optional<Expense> expense = repo.findById(id);
        if (expense.isPresent()) {
            return new ResponseEntity<>(expense.get(), HttpStatus.OK);
        } 
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
    
    @PutMapping("expense/{id}")
    public ResponseEntity<Expense> put(@PathVariable Long id, @RequestBody Expense expense) {
        return new ResponseEntity<>(repo.save(expense), HttpStatus.OK);
    }
    
    @PostMapping("expense")
    public ResponseEntity<Expense> post(@RequestBody Expense expense) {
        return new ResponseEntity<>(repo.save(expense), HttpStatus.CREATED);
    }
    
    @PatchMapping("expense/{id}")
    public ResponseEntity<Expense> patch (@PathVariable Long id, @RequestBody Expense patch) {
        Expense expense = repo.findById(id).get();
        if (patch.getId() != null) {
            expense.setId(patch.getId());
        }
        if (patch.getAmount() != null) {
            expense.setAmount(patch.getAmount());
        }
        if (patch.getReason() != null) {
            expense.setReason(patch.getReason());
        }
        if (patch.getDateCreated() != null) {
            expense.setDateCreated(patch.getDateCreated());
        }
        return new ResponseEntity<>(repo.save(expense), HttpStatus.OK);
    }
    
    @DeleteMapping("expense/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Error message")
    public void handleError() {
    }
}

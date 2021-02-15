package com.test.expenseapi;

import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepo extends CrudRepository<Expense, Long> {
    
}

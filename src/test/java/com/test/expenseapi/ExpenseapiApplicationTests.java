package com.test.expenseapi;

import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest
@AutoConfigureMockMvc
class ExpenseapiApplicationTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ExpenseController controller;
    @Autowired
    private ExpenseRepo repo;

    @Test
    void contextLoads() {
        assertThat(controller).isNotNull();
    }
    
    @Test
    public void validateRestCOntroller () {
        mockMvc = standaloneSetup(controller)
                .alwaysExpect(status().isOk())
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .build();
    }
    
    @Test
    public void postExpense () throws Exception {
        mockMvc.perform(post("/api/expense")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":500,\"reason\":\"Test\"}")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.amount").value("500.0"))
                .andExpect(jsonPath("$.reason").value("Test"));
    }
    
    @Test
    public void getExpense () throws Exception {
        Expense entry = new Expense();
        entry.setAmount(450.78);
        entry.setReason("Getting Expense");
        
        Expense e = repo.save(entry);
        
        Optional<Expense> expense = repo.findById(entry.getId());

        String id = e.getId().toString();
                        
        mockMvc.perform(get("/api/expense/"+id)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.amount").value("450.78"))
                .andExpect(jsonPath("$.reason").value("Getting Expense"));
    }
    
    @Test
    public void nonExistableExpense () throws Exception {
        mockMvc.perform(get("/api/expense/100")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
    
    @Test
    public void deleteExpense () throws Exception {
        Expense entry = new Expense();
        entry.setAmount(902.34);
        entry.setReason("Testing delete endpoint");
        
        Expense e = repo.save(entry);
        
        Optional<Expense> expense = repo.findById(e.getId());
                
        String id = e.getId().toString();
        
        mockMvc.perform(delete("/api/expense/"+id)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

}

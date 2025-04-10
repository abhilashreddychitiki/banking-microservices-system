package com.banking.demo.controller;

import com.banking.demo.model.Account;
import com.banking.demo.model.AccountType;
import com.banking.demo.model.Transaction;
import com.banking.demo.model.User;
import com.banking.demo.service.AccountService;
import com.banking.demo.service.TransactionService;
import com.banking.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Account> accounts = accountService.getAccountsByUser(user);
        
        model.addAttribute("user", user);
        model.addAttribute("accounts", accounts);
        
        return "dashboard";
    }

    @GetMapping("/accounts/create")
    public String createAccountForm(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("accountTypes", AccountType.values());
        return "create-account";
    }

    @PostMapping("/accounts/create")
    public String createAccount(Authentication authentication, @ModelAttribute Account account) {
        User user = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        account.setUser(user);
        accountService.createAccount(account);
        
        return "redirect:/dashboard";
    }

    @GetMapping("/accounts/{id}")
    public String viewAccount(@PathVariable Long id, Model model) {
        Account account = accountService.getAccountById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        
        List<Transaction> transactions = transactionService.getTransactionsByAccount(account);
        
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);
        
        return "account-details";
    }

    @GetMapping("/transactions/deposit")
    public String depositForm(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Account> accounts = accountService.getAccountsByUser(user);
        
        model.addAttribute("accounts", accounts);
        return "deposit";
    }

    @PostMapping("/transactions/deposit")
    public String deposit(@RequestParam String accountNumber, @RequestParam BigDecimal amount, @RequestParam String description) {
        transactionService.deposit(accountNumber, amount, description);
        return "redirect:/dashboard";
    }

    @GetMapping("/transactions/withdraw")
    public String withdrawForm(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Account> accounts = accountService.getAccountsByUser(user);
        
        model.addAttribute("accounts", accounts);
        return "withdraw";
    }

    @PostMapping("/transactions/withdraw")
    public String withdraw(@RequestParam String accountNumber, @RequestParam BigDecimal amount, @RequestParam String description) {
        transactionService.withdraw(accountNumber, amount, description);
        return "redirect:/dashboard";
    }

    @GetMapping("/transactions/transfer")
    public String transferForm(Authentication authentication, Model model) {
        User user = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        List<Account> accounts = accountService.getAccountsByUser(user);
        
        model.addAttribute("accounts", accounts);
        return "transfer";
    }

    @PostMapping("/transactions/transfer")
    public String transfer(@RequestParam String sourceAccountNumber, @RequestParam String destinationAccountNumber, 
                          @RequestParam BigDecimal amount, @RequestParam String description) {
        transactionService.transfer(sourceAccountNumber, destinationAccountNumber, amount, description);
        return "redirect:/dashboard";
    }
}

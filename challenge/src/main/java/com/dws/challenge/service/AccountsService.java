package com.dws.challenge.service;

import com.dws.challenge.domain.Account;
import com.dws.challenge.repository.AccountsRepository;
import lombok.Getter;
import lombok.Synchronized;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountsService {

  @Getter
  private final AccountsRepository accountsRepository;

  @Autowired
  public AccountsService(AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  public void createAccount(Account account) {
    this.accountsRepository.createAccount(account);
  }

  public Account getAccount(String accountId) {
    return this.accountsRepository.getAccount(accountId);
  }
  
  @Synchronized
  public void transfer(String accountId,String desAccountId, BigDecimal amount) {
	  
	  //TODO: validation that both accounts exists, the balance dont go below 0
	  // for each validation check should throw an Exception that Rest Controller catch and maps to the proper HTTP code
	  
	  accountsRepository.getAccount(accountId).setBalance(accountsRepository.getAccount(accountId).getBalance().subtract(amount));
	  accountsRepository.getAccount(desAccountId).setBalance(accountsRepository.getAccount(desAccountId).getBalance().add(amount));
	  
	  
  }
}

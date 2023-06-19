package com.dws.challenge.web;

import com.dws.challenge.domain.Account;
import com.dws.challenge.exception.DuplicateAccountIdException;
import com.dws.challenge.service.AccountsService;
import com.dws.challenge.service.EmailNotificationService;
import com.dws.challenge.service.NotificationService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/accounts")
@Slf4j
public class AccountsController {

  private final AccountsService accountsService;
  private final EmailNotificationService emailNotificationService;
 
  @Autowired
  public AccountsController(AccountsService accountsService, EmailNotificationService emailNotificationService) {
    this.accountsService = accountsService;
	this.emailNotificationService = emailNotificationService;
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
    log.info("Creating account {}", account);

    try {
    this.accountsService.createAccount(account);
    } catch (DuplicateAccountIdException daie) {
      return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @GetMapping(path = "/{accountId}")
  public Account getAccount(@PathVariable String accountId) {
    log.info("Retrieving account for id {}", accountId);
    return this.accountsService.getAccount(accountId);
  }

  @PutMapping(path= "/{accountId}/transfer/{destAccountId}/{amount}")
	public ResponseEntity<Object> transferAmount(@PathVariable String accountId, @PathVariable String destAccountId, @PathVariable BigDecimal amount) {
	  
	  log.info("Transfer from account {} to account {} the amount {}", accountId, destAccountId, amount);
	  
	  accountsService.transfer(accountId, destAccountId, amount);
	  emailNotificationService.notifyAboutTransfer(this.accountsService.getAccount(accountId), "Transfers the amount "+amount+"€ to Account " + destAccountId);
	  return new ResponseEntity<>(HttpStatus.OK);
  }
  
  
}

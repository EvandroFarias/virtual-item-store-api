package com.app.backend.controllers;

import com.app.backend.customException.PendingTransactionException;
import com.app.backend.customException.RedundancyExcpetion;
import com.app.backend.customException.UnreachableTransactionException;
import com.app.backend.dtos.transaction.TransactionCreationDTO;
import com.app.backend.dtos.transaction.TransactionViewDTO;
import com.app.backend.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/propose")
    public ResponseEntity<Object> proposeTransaction(@RequestBody TransactionCreationDTO transactionCreationDTO) {
        try {
            return ResponseEntity.ok()
                    .body(transactionService.proposeTransaction(transactionCreationDTO));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .body(e.getMessage());
        } catch (PendingTransactionException e) {
            return ResponseEntity.status(409)
                    .body(e.getMessage());
        } catch (RedundancyExcpetion e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(400)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(e.getMessage());
        }
    }

    @PatchMapping("/proccess/{sellerr}")
    public ResponseEntity<Object> proccessTransaction(@RequestParam("transaction") UUID transactionId,
                                                      @RequestParam("status") String action,
                                                      @PathVariable("seller") UUID sellerId) {
        try {
            return ResponseEntity.ok()
                    .body(transactionService.proccessTransaction(sellerId, transactionId, action));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404)
                    .body(e.getMessage());
        } catch (IllegalArgumentException | UnreachableTransactionException e) {
            return ResponseEntity.status(400)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(e.getMessage());
        }
    }


}
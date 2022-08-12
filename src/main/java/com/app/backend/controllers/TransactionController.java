package com.app.backend.controllers;

import com.app.backend.customException.PendingTransactionException;
import com.app.backend.customException.RedundancyExcpetion;
import com.app.backend.customException.UnreachableTransactionException;
import com.app.backend.dtos.transaction.TransactionCreationDTO;
import com.app.backend.services.TokenService;
import com.app.backend.services.TransactionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping(value = "/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/propose")
    @ApiOperation(value = "Creates a transaction on pending status. If buyer has no balance, then auto-rejects.")
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

    @PatchMapping("/proccess/{seller}")
    @ApiOperation(value = "Proccesses the transaction by parameter action, it can be either reject or approve.")
    public ResponseEntity<Object> proccessTransaction(@RequestParam("transaction") UUID transactionId,
                                                      @RequestParam("status") String action,
                                                      @PathVariable("seller") UUID sellerId,
                                                      HttpServletRequest request) {
        try {
            if (TokenService.isValidToken(request.getHeader("TOKEN"))) {
                return ResponseEntity.ok()
                        .body(transactionService.proccessTransaction(sellerId, transactionId, action));
            }
            return ResponseEntity.status(403).body("Token Expired");
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
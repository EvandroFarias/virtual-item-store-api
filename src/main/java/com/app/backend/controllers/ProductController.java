package com.app.backend.controllers;

import com.app.backend.customException.NotActiveException;
import com.app.backend.dtos.product.ProductCreationDTO;
import com.app.backend.models.Product;
import com.app.backend.services.ProductService;
import com.app.backend.services.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody ProductCreationDTO productDto,
                                    @RequestParam("userId") UUID userId,
                                    HttpServletRequest request) {
        log.info("Reached Product Controller endpoint");
        try {
            if (TokenService.validToken(request.getHeader("TOKEN"))) {
                return ResponseEntity
                        .status(201)
                        .body(productService.create(ProductCreationDTO.dtoToModel(productDto), userId));
            }
            return ResponseEntity.status(403).body("Token Expired");
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (NotActiveException ex) {
            return ResponseEntity.status(417).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestParam("productId") UUID productId,
                                    Product product,
                                    HttpServletRequest request) {
        log.info("Reached Product Controller endpoint");
        try {
            if (TokenService.validToken(request.getHeader("TOKEN"))) {
                return ResponseEntity.status(200).body(productService.update(productId, product));
            }
            return ResponseEntity.status(403).body("Token Expired");
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        }  catch (NotActiveException ex) {
            return ResponseEntity.status(417).body(ex.getMessage());
        }   catch (IllegalArgumentException ex) {
            return ResponseEntity.status(400).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }

    }

    @PatchMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("productId") UUID productId,
                                    HttpServletRequest request) {
        log.info("Reached Product Controller endpoint");
        try {
            if (TokenService.validToken(request.getHeader("TOKEN"))) {
                return ResponseEntity.status(200).body(productService.delete(productId));
            }
            return ResponseEntity.status(403).body("Token Expired");
        } catch (NoSuchElementException ex) {
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (NotActiveException ex) {
            return ResponseEntity.status(417).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(ex.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAllOrFindById(
            @RequestParam(value = "userId", required = false) UUID userId,
            HttpServletRequest request) {
        log.info("Reached Product Controller endpoint");

        if (userId != null && TokenService.validToken(request.getHeader("TOKEN"))) {
            try {
                return ResponseEntity.status(200).body(productService.findByUser(userId));
            } catch (Exception ex) {
                return ResponseEntity.status(500).body(ex.getMessage());
            }
        } else {
            try {
                if (TokenService.validToken(request.getHeader("TOKEN"))) {
                    return ResponseEntity.status(200).body(productService.getAll());
                }
                return ResponseEntity.status(403).body("Token Expired");
            } catch (Exception ex) {
                return ResponseEntity.status(500).body(ex.getMessage());
            }
        }
    }
}
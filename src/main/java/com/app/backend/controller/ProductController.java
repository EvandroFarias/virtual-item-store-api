package com.app.backend.controller;

import com.app.backend.dto.product.ProductCreationDTO;
import com.app.backend.model.Product;
import com.app.backend.service.ProductService;
import com.app.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> create(ProductCreationDTO productDto,
                                    @RequestParam("userId") UUID userId,
                                    HttpServletRequest request) {
        try {
            if (TokenService.validToken(request.getHeader("TOKEN"))) {
                return new ResponseEntity<>(productService.create(ProductCreationDTO.dtoToModel(productDto), userId),
                        HttpStatus.CREATED);
            }
            return new ResponseEntity<>("Token expired", HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<?> update(@RequestParam("productId") UUID productId,
                                    Product product,
                                    HttpServletRequest request) {

        try {
            if (TokenService.validToken(request.getHeader("TOKEN"))) {
                return new ResponseEntity<>(productService.update(productId, product), HttpStatus.OK);
            }
            return new ResponseEntity<>("Token expired", HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PatchMapping("/delete")
    public ResponseEntity<?> delete(@RequestParam("productId") UUID productId,
                                    HttpServletRequest request) {
        try {
            if (TokenService.validToken(request.getHeader("TOKEN"))) {
                return new ResponseEntity<>(productService.delete(productId), HttpStatus.OK);
            }
            return new ResponseEntity<>("Token expired", HttpStatus.FORBIDDEN);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> findAllOrFindById(
            @RequestParam(value = "userId", required = false) UUID userId,
            HttpServletRequest request) {

        if (userId != null && TokenService.validToken(request.getHeader("TOKEN"))) {
            try {
                return new ResponseEntity<>(productService.findByUser(userId), HttpStatus.OK);
            } catch (Exception ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        } else {
            try {
                if (TokenService.validToken(request.getHeader("TOKEN"))) {
                    return new ResponseEntity<>(productService.getAll(), HttpStatus.OK);
                }
                return new ResponseEntity<>("Token expired", HttpStatus.FORBIDDEN);
            } catch (Exception ex) {
                return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
    }
}

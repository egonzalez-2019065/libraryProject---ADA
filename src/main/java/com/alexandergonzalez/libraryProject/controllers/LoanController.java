package com.alexandergonzalez.libraryProject.controllers;

import com.alexandergonzalez.libraryProject.dto.loan.LoanDto;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanFactory;
import com.alexandergonzalez.libraryProject.factory.loanFactory.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/v1/loan")
public class LoanController {

    private final LoanFactory loanFactory;

    public LoanController(LoanFactory loanFactory) {
        this.loanFactory = loanFactory;
    }

    @PostMapping
    public Object saveLoan(@RequestBody LoanDto loanDto){
        LoanService loanService = loanFactory.getLoanService();
        HashMap<String, Object> response = new HashMap<>();
        LoanDto savedLoan = loanService.saveLoan(loanDto);
        if(savedLoan != null){
            response.put("Libro prestado", savedLoan);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        response.put("message", "No se pudo realizar el prestamo, verifica que el libro esté disponible");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllLoans() {
        LoanService loanService = loanFactory.getLoanService();
        List<LoanDto> loanDto = loanService.getLoans();

        Map<String, Object> response = new HashMap<>();
        response.put("Préstamos encontrados", loanDto);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Object> returnBook(@PathVariable("id") String bookId) {
        LoanService loanService = loanFactory.getLoanService();
        HashMap<String, Object> response = new HashMap<>();

        LoanDto returnedLoan = loanService.returnBook(bookId);
        if (returnedLoan != null) {
            response.put("Libro devuelto", returnedLoan);
            return ResponseEntity.ok().body(response);
        } else {
            response.put("Error", "No se encontró el préstamo para este libro.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }


}

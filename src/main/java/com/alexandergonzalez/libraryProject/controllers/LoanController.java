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
        System.out.println(loanDto);
        LoanService loanService = loanFactory.getLoanService();
        HashMap<String, Object> response = new HashMap<>();
        LoanDto savedLoan = loanService.saveLoan(loanDto);
        response.put("Libro prestado", savedLoan);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping()
    public ResponseEntity<Object> getAllLoans() {
        LoanService loanService = loanFactory.getLoanService();
        List<LoanDto> loanDtos = loanService.getLoans();

        Map<String, Object> response = new HashMap<>();
        response.put("Préstamos encontrados", loanDtos);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}

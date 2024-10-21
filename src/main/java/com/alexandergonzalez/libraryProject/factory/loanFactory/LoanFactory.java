package com.alexandergonzalez.libraryProject.factory.loanFactory;


import com.alexandergonzalez.libraryProject.factory.bookFactory.BookService;
import com.alexandergonzalez.libraryProject.services.loan.LoanJPAService;
import com.alexandergonzalez.libraryProject.services.loan.LoanMongoService;
import org.flywaydb.core.internal.database.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class LoanFactory {

    private final LoanMongoService loanMongoService;
    private final LoanJPAService loanJPAService;


    @Value("${databaseType}")
    private String databaseType;


    public LoanFactory(LoanMongoService loanMongoService, LoanJPAService loanJPAService) {
        this.loanMongoService = loanMongoService;
        this.loanJPAService = loanJPAService;
    }

    public LoanService getLoanService(){
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return loanJPAService;
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return loanMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);

    }
}

package com.alexandergonzalez.libraryProject.factory.auth;

import com.alexandergonzalez.libraryProject.services.auth.AuthJPAService;
import com.alexandergonzalez.libraryProject.services.auth.AuthMongoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AuthFactory {

    private final AuthMongoService authMongoService;
    private final AuthJPAService authJPAService;

    @Value("${databaseType}")
    private String databaseType;

    public AuthFactory(AuthMongoService authMongoService, AuthJPAService authJPAService) {
        this.authMongoService = authMongoService;
        this.authJPAService = authJPAService;
    }


    public AuthService getAuthService(){
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return authJPAService;
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return authMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);

    }
}

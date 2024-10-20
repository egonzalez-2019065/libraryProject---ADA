package com.alexandergonzalez.libraryProject.factory;

import com.alexandergonzalez.libraryProject.services.user.UserJPAService;
import com.alexandergonzalez.libraryProject.services.user.UserMongoService;
import org.flywaydb.core.internal.database.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    private final UserJPAService userJPAService;
    private final UserMongoService userMongoService;
    @Value("${databaseType}")
    private String databaseType;

    public UserFactory(UserJPAService userJPAService, UserMongoService userMongoService) {
        this.userJPAService = userJPAService;
        this.userMongoService = userMongoService;
    }

    public UserService getUserService() {
        if ("JPA".equalsIgnoreCase(databaseType)) {
            return userJPAService;
        } else if ("MONGO".equalsIgnoreCase(databaseType)) {
            return userMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + databaseType);
    }
}

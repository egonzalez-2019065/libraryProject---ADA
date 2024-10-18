package com.alexandergonzalez.libraryProject.factory;

import com.alexandergonzalez.libraryProject.services.user.UserJPAService;
import com.alexandergonzalez.libraryProject.services.user.UserMongoService;

public class UserFactory {

    private final UserJPAService userJPAService;
    private final UserMongoService userMongoService;

    public UserFactory(UserJPAService userJPAService, UserMongoService userMongoService) {
        this.userJPAService = userJPAService;
        this.userMongoService = userMongoService;
    }

    public UserService getUserService(String type) {
        if ("jpa".equalsIgnoreCase(type)) {
            return userJPAService;
        } else if ("mongo".equalsIgnoreCase(type)) {
            return userMongoService;
        }
        throw new IllegalArgumentException("Invalid database type: " + type);
    }
}

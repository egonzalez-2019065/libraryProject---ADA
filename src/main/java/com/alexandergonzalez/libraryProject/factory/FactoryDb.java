package com.alexandergonzalez.libraryProject.factory;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FactoryDb {

    private final ApplicationContext context;
    private final String databaseType;


    public FactoryDb(ApplicationContext context) {
        this.context = context;
        this.databaseType = "jpa";
    }

    public <T> T getService(Class<T> serviceInterface) {

        String serviceName = serviceInterface.getSimpleName();

        String beanName = databaseType.toLowerCase() + serviceName;

        return context.getBean(beanName, serviceInterface);

    }
}


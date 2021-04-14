package org.nikita;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

    private static final String CONTROLLER_BEAN = "controller";
    private static final String XML_CONFIGURATION = "spring.xml";

    public static void main(String[] args) {
        ApplicationContext factory = new ClassPathXmlApplicationContext(XML_CONFIGURATION);
        ((Controller)factory.getBean(CONTROLLER_BEAN)).run(args);
    }
}

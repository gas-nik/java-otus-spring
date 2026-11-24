package ru.otus.hw;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.hw.config.HwConfig;
import ru.otus.hw.service.TestRunnerService;

public class Application {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(HwConfig.class)) {
            var testRunnerService = context.getBean(TestRunnerService.class);
            testRunnerService.run();
        }
    }
}
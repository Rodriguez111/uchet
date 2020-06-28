package uchet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;

@SpringBootApplication
public class Application {
    private final static String RESOURCES_DIR = "c:/uchet";

    private final static String DB_DIR = RESOURCES_DIR + "/db";

    private static void checkAndCreateDirs() {
        new File(RESOURCES_DIR).mkdirs();
        new File(DB_DIR).mkdirs();
    }

    public static void main(String[] args) {
        checkAndCreateDirs();
        SpringApplication.run(Application.class, args);
    }
}

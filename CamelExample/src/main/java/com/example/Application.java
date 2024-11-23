package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.addListeners((event) -> {
            if (event.toString().contains("ContextClosedEvent")) {
                cleanUpDirectories();
            }
        });
        app.run(args);
    }

    private static void cleanUpDirectories() {
        try {
            deleteFilesInDirectory("out/bar");
            deleteFilesInDirectory("out/kitchen");
            deleteFilesInDirectory("out/checkout");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFilesInDirectory(String directory) throws IOException {
        Path dirPath = Paths.get(directory);
        if (Files.exists(dirPath)) {
            Files.walk(dirPath)
                 .filter(Files::isRegularFile)
                 .map(Path::toFile)
                 .forEach(File::delete);
        }
    }
}

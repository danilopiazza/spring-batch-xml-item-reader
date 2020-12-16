package io.github.danilopiazza.spring.batch.xml;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class XMLReaderBatchApplication {
    public static void main(String[] args) {
        System.exit(SpringApplication.exit(SpringApplication.run(XMLReaderBatchApplication.class, args)));
    }
}

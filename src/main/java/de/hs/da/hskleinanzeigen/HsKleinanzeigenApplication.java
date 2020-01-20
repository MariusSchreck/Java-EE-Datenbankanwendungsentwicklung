package de.hs.da.hskleinanzeigen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = {"de.hs.da"})
@EntityScan(basePackages = {"de.hs.da.hskleinanzeigen.entity"})  // scan JPA entities

public class HsKleinanzeigenApplication {

    public static void main(String[] args) {
        SpringApplication.run(HsKleinanzeigenApplication.class, args);
    }
}

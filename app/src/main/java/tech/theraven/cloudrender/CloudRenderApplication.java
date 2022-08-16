package tech.theraven.cloudrender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CloudRenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudRenderApplication.class, args);
    }

}

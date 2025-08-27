package com.bash.unitrack;

import com.bash.unitrack.email.EmailService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class miscellaneous {

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.
                baseUrl("https://routes.googleapis.com")
                .build();
    }
}

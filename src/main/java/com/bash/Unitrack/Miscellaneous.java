package com.bash.Unitrack;

import com.bash.Unitrack.email.EmailService;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Miscellaneous {

    private final EmailService emailService;

    public Miscellaneous(EmailService emailService) {
        this.emailService = emailService;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Unitrack API")
                        .version("1.0")
                        .description("API documentation for Unitrack")
                );
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.
                baseUrl("https://routes.googleapis.com")
                .build();
    }

    @Bean
    public CommandLineRunner sendmessage(){
        return args -> {
            System.out.println("Yello");
            emailService.passwordResetEmail( "Jane", "bzakariyya6@gmail.com","This message is used to tst the smtp server");
            System.out.println("Tea time");
        };
    }
}

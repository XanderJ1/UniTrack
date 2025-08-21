package com.bash.Unitrack.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

@Slf4j
@Service
public class EmailTemplateService {

        public String getVerificationEmailContent(String name, String verificationLink, String expirationTime) throws IOException {
            return
                    loadTemplate("/templates/verification.html")
                            .replace("{{name}}", name)
                            .replace("{{token}}", verificationLink)
                            .replace("{{expirationTime}}", expirationTime);


        }
        public String getPasswordResetEmailContent(String name, String resetLink, String expirationTime) throws IOException {
            return
                    loadTemplate("/templates/password-reset.html")
                            .replace("{{name}}", name)
                            .replace("{{token}}", resetLink)
                            .replace("{{expirationTime}}", expirationTime);


        }

        public String loadTemplate(String source) throws IOException{
            ClassPathResource path = new ClassPathResource(source);
            try(Reader reader = new InputStreamReader(path.getInputStream())){
               return FileCopyUtils.copyToString(reader);
            }catch (Exception e){
                log.error("Error reading file");
                return e.getMessage();
            }
        }
}

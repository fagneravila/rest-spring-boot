package br.com.avila.service;

import br.com.avila.config.EmailConfig;
import br.com.avila.data.dto.request.EmailRequestDto;
import br.com.avila.mail.EmailSender;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class EmailService {

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private EmailConfig emailConfig;

    public void sendSimpleEmail(EmailRequestDto emailRequestDto){
          emailSender
                  .To(emailRequestDto.getTo())
                  .withSubject(emailRequestDto.getSubject())
                  .withMessage(emailRequestDto.getBody())
                  .send(emailConfig);

    }

    public void sendEmailWithAttachment(String emailRequestJson, MultipartFile attachment){
        File tempFile = null;

        try {
            EmailRequestDto emailRequestDto = new ObjectMapper().readValue(emailRequestJson, EmailRequestDto.class);
             tempFile = File.createTempFile("attachment", attachment.getOriginalFilename());
            attachment.transferTo(tempFile);
            emailSender
                    .To(emailRequestDto.getTo())
                    .withSubject(emailRequestDto.getSubject())
                    .withMessage(emailRequestDto.getBody())
                    .attach(tempFile.getAbsolutePath())
                    .send(emailConfig);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error parsing email request ", e);
        } catch (IOException e) {
            throw new RuntimeException("Error processing the attachment ", e);
        }finally {
            if (tempFile != null && tempFile.exists()){
                 tempFile.delete();
            }
        }
    }


}

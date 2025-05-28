package br.com.avila.controllers;

import br.com.avila.controllers.docs.EmailControllerDocs;
import br.com.avila.data.dto.request.EmailRequestDto;
import br.com.avila.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/email/v1")
public class EmailController implements EmailControllerDocs {

    @Autowired
    private EmailService emailService;

    @PostMapping
    @Override
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto emailRequest) {
        emailService.sendSimpleEmail(emailRequest);
        return new ResponseEntity<>("E-mail sent with success ", HttpStatus.OK);
    }

    @PostMapping(value = "/withAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Override
    public ResponseEntity<String> sendEmailWithAttachment(@RequestParam("emailRequest")  String emailRequest
            , @RequestParam("attachment")  MultipartFile attachment) {
        emailService.sendEmailWithAttachment(emailRequest, attachment);
        return new ResponseEntity<>("E-mail sent with success with attachment ", HttpStatus.OK);
    }

}

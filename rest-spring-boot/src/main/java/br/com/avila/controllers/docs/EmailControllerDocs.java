package br.com.avila.controllers.docs;

import br.com.avila.data.dto.request.EmailRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;


public interface EmailControllerDocs {


    @Operation(summary = "Send an email",
                description = "Sends an e-mail by providing details, subject and body",
                tags = {"Email"},
                responses = {
                        @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

                }
    )
    ResponseEntity<String> sendEmail(EmailRequestDto emailRequestDto);


    @Operation(summary = "Send an email with attachment",
            description = "Sends an e-mail with attachment by providing details, subject and body",
            tags = {"Email"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    ResponseEntity<String> sendEmailWithAttachment(String emailRequestJson, MultipartFile multipartFile);

}

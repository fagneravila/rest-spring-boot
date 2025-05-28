package br.com.avila.controllers.docs;

import br.com.avila.data.dto.V1.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File Endpoints")
public interface FileControllerDocs {

      @Operation(summary = "Upload a file to the server",
            description = "Upload a file to the server",
            tags = {"File Endpoints"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
       UploadFileResponseDTO uploadFile(MultipartFile file);

       @Operation(summary = "Upload multiple files to the server",
             description = "Upload multiple files to the server",
             tags = {"File Endpoints"},
             responses = {
                     @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content),
                     @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                     @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                     @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                     @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                     @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
             })
       List<UploadFileResponseDTO> uploadMultipleFiles(MultipartFile[] files);

       @Operation(summary = "Download a file from the server",
             description = "Download a file from the server",
             tags = {"File Endpoints"},
             responses = {
                     @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content),
                     @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                     @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                     @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                     @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                     @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
             })
       ResponseEntity<Resource> downloadFile(String fileName, HttpServletRequest request);

}

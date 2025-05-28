package br.com.avila.controllers.docs;

import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.data.dto.V2.PersonDTOV2;
import br.com.avila.file.exporter.MediaTypes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PersonControllerDocs {
    
    @Operation(summary = "Find all people",
            description = "Find all people",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                    )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    /*List<PersonDTO> findAll();*/
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );


    @Operation(summary = "Find by name people",
            description = "Find by name people",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200", content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))
                    )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
        /*List<PersonDTO> findAll();*/
    ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPersonByName(
            @PathVariable("firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    );



    @Operation(summary = "Find one person",
            description = "Find one person",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    PersonDTO findById(@PathVariable("id") Long id);


    @Operation(summary = "Create one person",
            description = "Create one person",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    PersonDTO create(@RequestBody PersonDTO personDTO);


    PersonDTOV2 create(@RequestBody PersonDTOV2 personDTO);


    @Operation(summary = "Update one person",
            description = "Update one person",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    PersonDTO update(@RequestBody PersonDTO personDTO);


    @Operation(summary = "Delete one person",
            description = "Delete one person",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "204", content = @Content),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)}
    )
    ResponseEntity<?> delete(@PathVariable("id") Long id);


    @Operation(summary = "Disable one person",
            description = "Disable one person",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = PersonDTO.class)
                            )),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    PersonDTO disablePerson(@PathVariable("id") Long id);


    @Operation(summary = "Massive people creation",
            description = "Massive people creation which upliads of XLSX or CSV",
            tags = {"Person"},
            responses = {
                    @ApiResponse(
                            description = "Successful Operation",
                            responseCode = "200",
                            content = {
                                    @Content( schema = @Schema(implementation = PersonDTO.class))
                            }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    List<PersonDTO> massCreation(MultipartFile file);



    @Operation(summary = "export  people page",
            description = "export  a page of people in XLSX and CSV files",
            tags = {"Person"},
            responses = {
                    @ApiResponse(description = "Successful Operation", responseCode = "200", content = {
                            @Content(mediaType = MediaTypes.APPLICATION_XLSX_VALUE),
                            @Content(mediaType = MediaTypes.APPLICATION_CSV_VALUE)
                    }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content),

            }
    )
    ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    );



    @Operation(summary = "Export person data as PDF ",
            description = "Export a specific Person data as PDF by your ID ",
            tags = {"Person"},
            responses = {
                    @ApiResponse(
                            description = "Successful Operation",
                            responseCode = "200",
                            content = @Content(mediaType = MediaTypes.APPLICATION_PDF_VALUE)
                            ),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            })
    ResponseEntity<Resource> export(@PathVariable("id") Long id,  HttpServletRequest request);



}

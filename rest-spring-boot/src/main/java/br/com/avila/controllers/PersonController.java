package br.com.avila.controllers;

import br.com.avila.controllers.docs.PersonControllerDocs;
import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.data.dto.V2.PersonDTOV2;
import br.com.avila.file.exporter.MediaTypes;
import br.com.avila.service.PersonService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

//@CrossOrigin//(origins = "http://localhost:8080") nivel de controller
@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "Person", description = "Endpoints for Managing Person")
public class PersonController implements PersonControllerDocs {

    @Autowired
    private PersonService service;
    // private PersonServices service = new PersonServices();

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    //public List<PersonDTO> findAll() {
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection,"firstName"));
        return ResponseEntity.ok(service.findAll(pageable));
    }

     @GetMapping(value = "/findPersonByName/{firstName}",
             produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    //public List<PersonDTO> findAll() {
    public ResponseEntity<PagedModel<EntityModel<PersonDTO>>> findPersonByName(
            @PathVariable(value = "firstName") String firstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection,"firstName"));
        return ResponseEntity.ok(service.findByName(firstName,pageable));
    }



    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    @CrossOrigin(origins = {"http://localhost:8080", "http://fagner.avila.com.br:7070"})
    public PersonDTO findById(@PathVariable("id") Long id) {
        var person = service.findById(id);
        //usado para testes json serialization
        //person.setBirthDay(new Date());
        //person.setPhoneNumber("+55 (61) 992950977");
        //person.setPhoneNumber("");
        //person.setLastName(null);
        //person.setSensitiveData("Foo Bar");
        return person;
    }

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTO create(@RequestBody PersonDTO personDTO) {
        return service.create(personDTO);
    }



    @PostMapping(value = "/v2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTOV2 create(@RequestBody PersonDTOV2 personDTO) {
        return service.createV2(personDTO);
    }

    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTO update(@RequestBody PersonDTO personDTO) {
        return service.update(personDTO);
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PatchMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public PersonDTO disablePerson(@PathVariable("id") Long id) {
        var person = service.disablePerson(id);
        return person;
    }

    @PostMapping(value = "/massCreation",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Override
    public List<PersonDTO> massCreation(@RequestParam("file") MultipartFile file) {
        return service.massCreation(file);
    }

    @GetMapping(value = "/exportPage",
            produces = {MediaTypes.APPLICATION_XLSX_VALUE, MediaTypes.APPLICATION_CSV_VALUE, MediaTypes.APPLICATION_PDF_VALUE})
    @Override
     public ResponseEntity<Resource> exportPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            HttpServletRequest request
    ){

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection,"firstName"));

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = service.exportPage(pageable, acceptHeader);

        Map<String, String> extensioMap = Map.of(MediaTypes.APPLICATION_XLSX_VALUE, ".xlsx", MediaTypes.APPLICATION_CSV_VALUE, ".csv", MediaTypes.APPLICATION_PDF_VALUE, ".pdf");
    var fileExtension = extensioMap.getOrDefault(acceptHeader,"");
         var  contentType = acceptHeader != null ? acceptHeader : "application/octet-stream";

         var filename = "people_export " + fileExtension;

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(file);
    }


    @GetMapping(value = "/export/{id}",
            produces = {MediaType.APPLICATION_PDF_VALUE})
    @Override
    public ResponseEntity<Resource> export(Long id, HttpServletRequest request) {

        String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);

        Resource file = service.exportPerson(id, acceptHeader);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(acceptHeader))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=person.pdf")
                .body(file);
    }
}
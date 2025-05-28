package br.com.avila.controllers;

import br.com.avila.controllers.docs.BookControllerDocs;
import br.com.avila.data.dto.V1.BookDTO;
import br.com.avila.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/book/v1")
@Tag(name = "Book", description = "Endpoints for Managing Book")
public class BookController implements BookControllerDocs {

    @Autowired
    private BookService service;
    // private BookServices service = new BookServices();

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<PagedModel<EntityModel<BookDTO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection,"title"));

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping(value = "/{id}",
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public BookDTO findById(@PathVariable("id") Long id) {
        var Book = service.findById(id);
        //usado para testes json serialization
        //Book.setBirthDay(new Date());
        //Book.setPhoneNumber("+55 (61) 992950977");
        //Book.setPhoneNumber("");
        //Book.setLastName(null);
        //Book.setSensitiveData("Foo Bar");
        return Book;
    }

    @PostMapping(

            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public BookDTO create(@RequestBody BookDTO BookDTO) {
        return service.create(BookDTO);
    }


    @PutMapping(
            consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE },
            produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE,MediaType.APPLICATION_YAML_VALUE }
    )
    @Override
    public BookDTO update(@RequestBody BookDTO BookDTO) {
        return service.update(BookDTO);
    }


    @DeleteMapping(value = "/{id}")
    @Override
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
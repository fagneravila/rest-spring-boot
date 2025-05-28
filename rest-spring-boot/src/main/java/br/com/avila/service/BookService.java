package br.com.avila.service;


import br.com.avila.controllers.BookController;
import br.com.avila.data.dto.V1.BookDTO;
import br.com.avila.exception.RequiredObjectIsNullException;
import br.com.avila.exception.ResourceNotFoundException;
import br.com.avila.mapper.ObjectMapper;
import br.com.avila.model.Book;
import br.com.avila.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import static br.com.avila.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    private Logger logger = LoggerFactory.getLogger(BookService.class.getName());

    @Autowired
    BookRepository repository;

    @Autowired
    PagedResourcesAssembler<BookDTO> assembler;


    public PagedModel<EntityModel<BookDTO>> findAll(Pageable pegeable) {

        logger.info("Finding all Book!");
        var books = repository.findAll(pegeable);
        var booksWithLinks = books.map(book-> {
        var dto = ObjectMapper.parseObject(book, BookDTO.class);
        addHateoasLinks(dto);
            return dto;
        });
        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(BookController.class).findAll(
                pegeable.getPageNumber(), pegeable.getPageSize(), String.valueOf(pegeable.getSort()))).withSelfRel();
        return assembler.toModel(booksWithLinks, findAllLink);
    }

    public BookDTO findById(Long id) {
        logger.info("Finding one Book!");

        var entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        var dto =  parseObject(entity, BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO create(BookDTO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Book!");
        var entity = parseObject(book, Book.class);
        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public BookDTO update(BookDTO book) {

        if (book == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Book!");
        Book entity = repository.findById(book.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(book.getAuthor());
        entity.setLaunchDate(book.getLaunchDate());
        entity.setPrice(book.getPrice());
        entity.setTitle(book.getTitle());

        var dto = parseObject(repository.save(entity), BookDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one Book!");

        Book entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private void addHateoasLinks(BookDTO dto) {
        dto.add(linkTo(methodOn(BookController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).findAll(0,10,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(BookController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(BookController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(BookController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}
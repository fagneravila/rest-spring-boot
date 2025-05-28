package br.com.avila.service;


import br.com.avila.controllers.PersonController;
import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.data.dto.V2.PersonDTOV2;
import br.com.avila.exception.BadRequestException;
import br.com.avila.exception.FileStorageException;
import br.com.avila.exception.RequiredObjectIsNullException;
import br.com.avila.exception.ResourceNotFoundException;
import br.com.avila.file.exporter.contract.PersonExporter;
import br.com.avila.file.exporter.factory.FileExporterFactory;
import br.com.avila.file.importer.contract.FileImporter;
import br.com.avila.file.importer.factory.FileImporterFactory;
import br.com.avila.mapper.ObjectMapper;
import br.com.avila.mapper.custom.PersonMapper;
import br.com.avila.model.Person;
import br.com.avila.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class PersonService {

    private final AtomicLong counter = new AtomicLong();
    private Logger logger = LoggerFactory.getLogger(PersonService.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporter;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;


   /* public List<PersonDTO> findAll() {

        logger.info("Finding all People!");

        var people = repository.findAll();

        var persons = parseListObjects(repository.findAll(), PersonDTO.class);
        persons.forEach(this::addHateoasLinks);
        return persons;
    }*/

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pegeable) {

        logger.info("Finding all People!");

        var people = repository.findAll(pegeable);
        return buildPagedModel(pegeable, people);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pegeable) {

        logger.info("Finding People By Name!");

        var people = repository.findPeopleByName(firstName, pegeable);;
        return buildPagedModel(pegeable, people);
    }


    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity  = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        // Hateos
         var dto =ObjectMapper.parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    public Resource exportPage(Pageable pegeable, String acceptHeader) {

        logger.info("Export all People page!");

        var people = repository.findAll(pegeable).map(person -> ObjectMapper.parseObject(person, PersonDTO.class)).getContent();

        try {
            PersonExporter exporter = this.exporter.getFileExporter(acceptHeader);
            return exporter.exportPeople(people);
        } catch (Exception e) {
            throw new FileStorageException("Error to export file!", e);
        }
    }

    public Resource exportPerson(Long id, String acceptHeader) {
        logger.info("Export data of one Person !");

        var person = repository.findById(id)
                .map(entity -> ObjectMapper.parseObject(entity, PersonDTO.class))
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

         try {
        PersonExporter exporter = this.exporter.getFileExporter(acceptHeader);
        return exporter.exportPeson(person);
        } catch (Exception e) {
            throw new FileStorageException("Error to export PDF!", e);
        }
    }




    public PersonDTO create(PersonDTO person) {

        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");


        var entity = ObjectMapper.parseObject(person, Person.class);
        var dto = ObjectMapper.parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);
        return dto;

    }

       public PersonDTOV2 createV2(PersonDTOV2 person) {

        logger.info("Creating one Person!");
       var entity = personMapper.convertDTOToEntity(person);
        return personMapper.convertEntityToDTO(repository.save(entity));

    }

    public List<PersonDTO> massCreation(MultipartFile file){

        logger.info("Importing people from file!");

        if(file.isEmpty()) throw new BadRequestException("Please, select a file!");

        try(InputStream inputStream = file.getInputStream()) {
             String filename = Optional.ofNullable(file.getOriginalFilename()).orElseThrow(() -> new BadRequestException("File name not found"));
             FileImporter importer = this.importer.getFileImporter(filename);

            List<Person> entities = importer.importFile(inputStream).stream()
                    .map(dto -> repository.save(ObjectMapper.parseObject(dto, Person.class)))
                    .toList();

            return entities.stream().map(entity ->{
                var dto =ObjectMapper.parseObject(entity, PersonDTO.class);
                addHateoasLinks(dto);
                return dto;
            }).toList();
        } catch (Exception e) {
            throw new FileStorageException("Could not store file " + file.getOriginalFilename() + ". Please try again!", e);
        }
}



    public PersonDTO update(PersonDTO person) {

        if(person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        var dto = ObjectMapper.parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks( dto);
        return dto;
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {

        logger.info("Disabling one Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.disablePerson(id);
        var entity = repository.findById(id).get();
        var dto = ObjectMapper.parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }


    private PagedModel<EntityModel<PersonDTO>> buildPagedModel(Pageable pegeable, Page<Person> people) {
        var peopleWithLinks = people.map(person -> {
            var dto = ObjectMapper.parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class).findAll(
                pegeable.getPageNumber(), pegeable.getPageSize(), String.valueOf(pegeable.getSort()))).withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }





    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1,10,"asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findPersonByName("",1,10,"asc")).withRel("findPersonByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class)).slash("massCreation").withRel("massCreation").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).exportPage(1,10,"asc", null)).withRel("exportPage").withType("GET").withTitle("Export page"));
    }
}
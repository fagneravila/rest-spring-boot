package br.com.avila.mapper.custom;

import br.com.avila.data.dto.V2.PersonDTOV2;
import br.com.avila.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person) {
         PersonDTOV2 dto = new PersonDTOV2();
         dto.setId(person.getId());
         dto.setFirstName(person.getFirstName());
         dto.setLastName(person.getLastName());
         dto.setBirthDate(new Date());
         dto.setAddress(person.getAddress());
         dto.setGender(person.getGender());

        return dto;
    }

    public Person convertDTOToEntity(PersonDTOV2 person) {
        Person dto = new Person();
        dto.setId(person.getId());
        dto.setFirstName(person.getFirstName());
        dto.setLastName(person.getLastName());
      //  dto.setBirthDate(new Date());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());

        return dto;
    }
}

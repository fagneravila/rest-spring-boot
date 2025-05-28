package br.com.avila.file.exporter.contract;

import br.com.avila.data.dto.V1.PersonDTO;
import org.springframework.core.io.Resource;


import java.util.List;

public interface PersonExporter {

    Resource exportPeople(List<PersonDTO> people) throws Exception;
    Resource exportPeson(PersonDTO person) throws Exception;
}

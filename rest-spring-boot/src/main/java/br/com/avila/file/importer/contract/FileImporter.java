package br.com.avila.file.importer.contract;

import br.com.avila.data.dto.V1.PersonDTO;

import java.io.InputStream;
import java.util.List;

public interface FileImporter {

    List<PersonDTO> importFile(InputStream inputStream) throws Exception;


}

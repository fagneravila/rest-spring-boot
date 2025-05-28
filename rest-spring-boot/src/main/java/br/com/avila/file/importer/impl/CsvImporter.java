package br.com.avila.file.importer.impl;

import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.file.importer.contract.FileImporter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsvImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {
        CSVFormat csvFormat = CSVFormat.Builder.create()
                .setHeader() // Define o cabeçalho do arquivo CSV
                .setSkipHeaderRecord(true) // Pula a primeira linha (cabeçalho)
                .setIgnoreEmptyLines(true) // Ignora linhas vazias
                .setTrim(true) // Remove espaços em branco ao redor dos valores
                .build();

        Iterable<CSVRecord> records = csvFormat.parse(new InputStreamReader(inputStream)); // Lê o conteúdo do arquivo CSV

            return parseRecordsToPersonDTOs(records);
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();
        for (CSVRecord record : records) {
            PersonDTO person = new PersonDTO();
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);
            people.add(person);
        }
        return people;

    }
}

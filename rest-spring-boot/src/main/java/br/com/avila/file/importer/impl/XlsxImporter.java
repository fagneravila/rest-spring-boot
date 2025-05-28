package br.com.avila.file.importer.impl;

import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter  implements FileImporter {


    @Override
    public List<PersonDTO> importFile(InputStream inputStream) throws Exception {

        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = workbook.getSheetAt(0); // Acessa a primeira planilha do arquivo Excel
            Iterator<Row> rowIterator = sheet.iterator(); // Itera sobre as linhas da planilha

            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            return parseRowsToPersonDTOList(rowIterator);
        }
    }

    private List<PersonDTO> parseRowsToPersonDTOList(Iterator<Row> rowIterator) {
        List<PersonDTO> people = new ArrayList<>();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            if (isRowValid(row)) {
                people.add(parseRowToPersonDTO(row));
            }
        }
        return people;
    }

    private PersonDTO parseRowToPersonDTO(Row row) {
        PersonDTO person = new PersonDTO();
        person.setFirstName(row.getCell(0).getStringCellValue());
        person.setLastName(row.getCell(1).getStringCellValue());
        person.setAddress(row.getCell(2).getStringCellValue());
        person.setGender(row.getCell(3).getStringCellValue());
        person.setEnabled(true);
        return person;
    }



    private static boolean isRowValid(Row row) {
        return row.getCell(0) != null && row.getCell(0).getCellType() != CellType.BLANK;
    }

}

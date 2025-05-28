package br.com.avila.file.importer.factory;

import br.com.avila.exception.BadRequestException;
import br.com.avila.file.importer.contract.FileImporter;
import br.com.avila.file.importer.impl.CsvImporter;
import br.com.avila.file.importer.impl.XlsxImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class FileImporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileImporterFactory.class.getName());

    @Autowired
    private ApplicationContext context;

    public FileImporter getFileImporter(String fileType) {
        if(fileType.endsWith(".xlsx")){
            //return new XlsxImporter();
            return context.getBean(XlsxImporter.class);
        } else if (fileType.endsWith(".csv")) {
            //return new CsvImporter();
            return context.getBean(CsvImporter.class); // Spring gerencia as dependências e injeta as classes
        } else {
            throw new BadRequestException("Invalid file type");
        }
    }


}

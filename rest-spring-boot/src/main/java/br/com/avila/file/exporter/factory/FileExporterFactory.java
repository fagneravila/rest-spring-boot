package br.com.avila.file.exporter.factory;

import br.com.avila.file.exporter.MediaTypes;
import br.com.avila.file.exporter.contract.PersonExporter;
import br.com.avila.file.exporter.impl.CsvExporter;
import br.com.avila.file.exporter.impl.PdfExporter;
import br.com.avila.file.exporter.impl.XlsxExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class FileExporterFactory {

    private Logger logger = LoggerFactory.getLogger(FileExporterFactory.class.getName());

    @Autowired
    private ApplicationContext context;


    public PersonExporter getFileExporter(String acceptHeader) {
        if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_XLSX_VALUE)) {
            return context.getBean(XlsxExporter.class);
        } else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_CSV_VALUE)) {
            return context.getBean(CsvExporter.class);
        }         else if (acceptHeader.equalsIgnoreCase(MediaTypes.APPLICATION_PDF_VALUE)) {
            return context.getBean(PdfExporter.class);
        } else {
            throw new RuntimeException("Invalid file type");
        }
    }
}

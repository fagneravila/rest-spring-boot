package br.com.avila.file.exporter.impl;

import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.file.exporter.contract.PersonExporter;
import br.com.avila.service.QRCodeService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PdfExporter implements PersonExporter {

    @Autowired
    private QRCodeService qrCodeService;

    @Override
    public Resource exportPeople(List<PersonDTO> people) throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/templates/people.jrxml");
        if(inputStream == null) {
            throw new RuntimeException("Template File not found: /templates/people.jrxml");
        }
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(people);
        Map<String, Object> parameters = new HashMap<>();
        //parameters.put("title", "List of people");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        }
    }

    @Override
    public Resource exportPeson(PersonDTO person) throws Exception {
        InputStream mainTemplateStream = getClass().getResourceAsStream("/templates/person.jrxml");
        if(mainTemplateStream == null) {
            throw new RuntimeException("Template File not found: /templates/person.jrxml");
        }

        InputStream subReportStream = getClass().getResourceAsStream("/templates/books.jrxml");
        if(subReportStream == null) {
            throw new RuntimeException("Template File not found: /templates/books.jrxml");
        }

        JasperReport mainReport = JasperCompileManager.compileReport(mainTemplateStream);
        JasperReport subReport = JasperCompileManager.compileReport(subReportStream);

        // TODO generate QR Code
        InputStream qrCodeStream = qrCodeService.generateQRCode(person.getProfileUrl(), 200, 200);


        JRBeanCollectionDataSource subReportDataSource = new JRBeanCollectionDataSource(person.getBooks());

        String path = getClass().getResource("/templates/books.jasper").getPath();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("SUB_REPORT_DATA_SOURCE", subReportDataSource);
        parameters.put("BOOK_SUB_REPORT", subReport);
        parameters.put("SUB_REPORT_DIR", path);
        parameters.put("QR_CODEIMAGE", qrCodeStream);


        JRBeanCollectionDataSource mainDataSource = new JRBeanCollectionDataSource(Collections.singletonList(person));

        JasperPrint jasperPrint = JasperFillManager.fillReport(mainReport,parameters,mainDataSource);
        try(ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
            return new ByteArrayResource(outputStream.toByteArray());

        }
    }


}

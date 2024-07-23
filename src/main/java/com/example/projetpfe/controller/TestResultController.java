package com.example.projetpfe.controller;

import com.example.projetpfe.service.TestResultService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200")
@Controller
@AllArgsConstructor
public class TestResultController {
    private final TestResultService service ;
    private final Path rootLocation = Paths.get("C:\\Users\\ADMIN\\OneDrive - ESPRIT\\Bureau\\uploadfile");


    @GetMapping("/junit-results-pdf")
    public ResponseEntity<ByteArrayResource> getTestResultsAsPdf() {
        String url= "http://192.168.33.10:8080/job/projetpfe/lastBuild/testReport/api/xml";
        String xmlData = service.getTestResultsAsXml(url);
        try {
            return service.convertXmlToPdf(xmlData);
        } catch (IOException e) {
            // Gérer l'erreur selon vos besoins
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/selenuim-results-pdf")
    public ResponseEntity<ByteArrayResource> getPipelineLogPdf() throws IOException {
        String url="http://localhost:8084/job/test1.1/lastBuild/consoleText";
        String consoleOutput = service.extractseleniumtests(url);
        String extractedLogs = service.extractparteselenessential(consoleOutput);
        return service.convertResultExtractionToPdf(extractedLogs);
    }

    @GetMapping("/jmeter-results-pdf")
    public ResponseEntity<ByteArrayResource> getJMeterResultsPdf() throws IOException {

        try {
            String url= "http://192.168.33.10:8080/job/jmeter/lastSuccessfulBuild/artifact/resultats.jtl/*view*/";
            String jmeterResults = service.getJMeterResults(url);
            ByteArrayResource pdfResource = service.convertOutputJmeterToPdf(jmeterResults);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=jmeter-results.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfResource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUploadd(@RequestParam("file") MultipartFile file) {
        String message;

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            Files.copy(file.getInputStream(), this.rootLocation.resolve(originalFilename));
            message = "Successfully uploaded!";
            return ResponseEntity.status(HttpStatus.OK).body(message);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }
// nekes bech nzidhom f spring security w nbadel les ports fel angular nrodou 8082 ////


}

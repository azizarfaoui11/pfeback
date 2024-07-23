package com.example.projetpfe.service;

import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestResultService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String username = "aziz";
    private final String password = "admin";


    public String getTestResultsAsXml(String url) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);



        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    //convert xml to pdf

    public ResponseEntity<ByteArrayResource> convertXmlToPdf(String xmlData) throws IOException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xmlData.getBytes());
            Document document = builder.parse(inputStream);

            PDDocument pdfDocument = new PDDocument();
            PDPage page = new PDPage();
            pdfDocument.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);

            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
            float yPosition = 700;
            final float INITIAL_Y_POSITION = 700;
            final float LINE_SPACING = 20;

            NodeList suites = document.getElementsByTagName("suite");
            for (int i = 0; i < suites.getLength(); i++) {
                Node suiteNode = suites.item(i);
                if (suiteNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element suiteElement = (Element) suiteNode;
                    String suiteName = suiteElement.getAttribute("name");

                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText("Suite: " + suiteName);
                    contentStream.endText();
                    yPosition -= LINE_SPACING;

                    NodeList cases = suiteElement.getElementsByTagName("case");
                    for (int j = 0; j < cases.getLength(); j++) {
                        Node caseNode = cases.item(j);
                        if (caseNode.getNodeType() == Node.ELEMENT_NODE) {
                            Element caseElement = (Element) caseNode;
                            String caseName = caseElement.getElementsByTagName("name").item(0).getTextContent();
                            String duration = caseElement.getElementsByTagName("duration").item(0).getTextContent();
                            String status = caseElement.getElementsByTagName("status").item(0).getTextContent();
                            String skipped = caseElement.getElementsByTagName("skipped").item(0).getTextContent();

                            contentStream.beginText();
                            contentStream.newLineAtOffset(20, yPosition);
                            contentStream.showText("Test Case: " + caseName);
                            contentStream.newLineAtOffset(235, 0);
                            contentStream.showText("Duration: " + duration + " seconds");
                            contentStream.newLineAtOffset(260, 0);
                            contentStream.showText("Status: " + status);

                            contentStream.endText();
                            yPosition -= LINE_SPACING;
                        }
                    }
                }
            }

            contentStream.close();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            pdfDocument.save(byteArrayOutputStream);
            pdfDocument.close();

            ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=test-results.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (ParserConfigurationException | IOException | SAXException e) {
            // Handle exceptions appropriately
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    //recuperation du console output selenuim


    /* public String extractseleniumtests(String jobName, int buildNumber) {
           String jenkinsUrl = "http://localhost:8084"; // Remplace par ton URL Jenkins
           String username = "aziz"; // Remplace par ton nom d'utilisateur Jenkins
           String password = "admin"; // Remplace par ton mot de passe Jenkins
           RestTemplate restTemplate = new RestTemplate();
         String apiUrl = jenkinsUrl + "/job/" + jobName + "/" + buildNumber + "/consoleText";

         // Set up basic authentication headers
         HttpHeaders headers = new HttpHeaders();
         headers.setBasicAuth(username, password);

         // Build URI with query parameters
         UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);

         HttpEntity<String> entity = new HttpEntity<>(headers);

         ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class);
         return response.getBody();
     }*/
    public String extractseleniumtests(String url) {
        //String jenkinsUrl = "http://localhost:8084"; // Remplace par ton URL Jenkins
        String username = "aziz"; // Remplace par ton nom d'utilisateur Jenkins
        String password = "admin"; // Remplace par ton mot de passe Jenkins
        RestTemplate restTemplate = new RestTemplate();
        // String apiUrl = jenkinsUrl + "/job/" + jobName + "/" + buildNumber + "/consoleText";

        // Set up basic authentication headers
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        // Build URI with query parameters
        //UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(apiUrl);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        return response.getBody();
    }

    public String extractparteselenessential(String consoleOutput) {
        String[] lines = StringUtils.split(consoleOutput, "\n");
        StringBuilder extractedLogs = new StringBuilder();

        boolean isStageStarted = false;
        for (String line : lines) {
            if (line.contains("[Pipeline] { (Run Python Script)")) {
                isStageStarted = true;
            }

            if (isStageStarted) {
                extractedLogs.append(line).append("\n");
            }

            if (isStageStarted && line.contains("End of Pipeline")) {
                break;
            }
        }
        return extractedLogs.toString();
    }
    public ResponseEntity<ByteArrayResource> convertResultExtractionToPdf(String text) throws IOException {
        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.beginText();
        contentStream.setLeading(14.5f);
        contentStream.newLineAtOffset(25, 725);

        String[] lines = text.split("\n");
        for (String line : lines) {
            String cleanedLine = line.replaceAll("[\\x00-\\x1F\\x7F]", ""); // Remove control characters
            contentStream.showText(cleanedLine);
            contentStream.newLine();
        }

        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfDocument.save(byteArrayOutputStream);
        pdfDocument.close();

        ByteArrayResource resource = new ByteArrayResource(byteArrayOutputStream.toByteArray());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=pipeline-log.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }


    ///// jmeter output /////
    public String getJMeterResults(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        // return response.getBody();
        String content = response.getBody();
        if (content != null && !content.isEmpty()) {
            return Arrays.stream(content.split("\n"))
                    .map(line -> {
                        String[] fields = line.split(",");
                        return String.join(",", Arrays.copyOfRange(fields, 2, fields.length));
                    })
                    .collect(Collectors.joining("\n"));
        }
        return "";
    }




    public ByteArrayResource convertOutputJmeterToPdf(String textData) throws IOException {
        PDDocument pdfDocument = new PDDocument();
        PDPage page = new PDPage();
        pdfDocument.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page);
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        float yPosition = 700;
        final float LINE_SPACING = 20;

        String[] lines = textData.split("\n");
        for (String line : lines) {
            if (yPosition < 50) {
                // contentStream.endText();
                contentStream.close();
                page = new PDPage();
                pdfDocument.addPage(page);
                contentStream = new PDPageContentStream(pdfDocument, page);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
                yPosition = 700;
            }
            contentStream.beginText();
            contentStream.newLineAtOffset(50, yPosition);
            contentStream.showText(line);
            contentStream.endText();
            yPosition -= LINE_SPACING;
        }

        contentStream.close();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        pdfDocument.save(byteArrayOutputStream);
        pdfDocument.close();

        return new ByteArrayResource(byteArrayOutputStream.toByteArray());
    }



}

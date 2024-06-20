package com.example.projetpfe.service;
import com.example.projetpfe.model.user.User;
import com.example.projetpfe.service.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;


@Service
public class srv {
    @Autowired
    private RestTemplate restTemplate;
    String dockerfile = "FROM node:latest as build\n" +
            "WORKDIR /app\n" +
            "COPY package*.json ./\n" +
            "COPY . .\n" +
            "RUN npm run build\n" +
            "FROM nginx:latest\n" +
            "COPY --from=build /app/dist/* /usr/share/nginx/html/\n" +
            "EXPOSE 80\n" +
            "CMD [\"nginx\", \"-g\", \"daemon off;\"]";









    public void triggerJenkinsPipeline(@RequestBody PipelineParams params) {
        //String targetstage1 = "clone";
        //String targetstage2 = "build";
        //String targetstage3 = "sonarqube";
       String a= params.getTargetStage1();
       String b= params.getTargetStage2();
       String c= params.getTargetStage3();
       String d= params.getTargetStage4();
        String e= params.getTargetStage5();
        String f= params.getTargetStage6();
        String j = params.getTargetStage7();
        String h= params.getTargetStage8();
        String i= params.getTargetStage9();
        String g= params.getTargetStage10();
        String k= params.getTargetStage11();
        String l= params.getTargetStage12();
        String m= params.getTargetStage13();
        String n= params.getTargetStage14();
        String o= params.getTargetStage15();
        String p= params.getTargetStage16();
        String aa= params.getTargetStage17();
        String bb= params.getTargetStage18();
        String cc= params.getTargetStage19();
        String dd= params.getTargetStage20();
        String ee= params.getTargetStage21();








        //windows
        // String jenkinsUrl = "http://localhost:8080/job/test/buildWithParameters/";
        String jenkinsUrl = "http://192.168.33.10:8080/job/projetpfe/buildWithParameters?TARGET_STAGE1=" +a+ "&TARGET_STAGE2=" +b+ "&TARGET_STAGE3=" +c+"&GITHUB_URL="+d+"&TARGET_STAGE5=" +e+"&TARGET_STAGE6="+f+"&TARGET_STAGE7="+j+"&TARGET_STAGE8="+h+"&TARGET_STAGE9="+i+"&TARGET_STAGE10="+g+"&TARGET_STAGE11="+k+"&TARGET_STAGE12="+l+"&TARGET_STAGE13="+m+"&TARGET_STAGE14="+n;

        // win
        //  String username = "aziz";
        String username = "aziz";

        //win
        // String apiToken = "11ad7512add4d956ec5530229b6ba925f6";
        String apiToken = "11f72b60ec8526a5e0697bf7b2559686ab";


        // Construire les paramètres de la requête

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

           /* requestBody.add("", TARGET_STAGE1);
        requestBody.add("", TARGET_STAGE2);
        requestBody.add("", TARGET_STAGE3);*/





        // Construire l'en-tête d'authentification
        String authString = username + ":" + apiToken;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(authString.getBytes()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Construire l'entité de requête HTTP avec les paramètres et l'en-tête
        // HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);


        // Effectuer la requête HTTP POST
        ResponseEntity<String> responseEntity = restTemplate.exchange(jenkinsUrl, HttpMethod.POST, requestEntity, String.class);

        // Vous pouvez traiter la réponse ici si nécessaire
    }



    public void pipelinewindows() {
        String a= "selenuim";

        //windows
        String jenkinsUrl = "http://localhost:8083/job/test/buildWithParameters?TARGET_STAGE1=" +a;
       // String jenkinsUrl = "http://192.168.33.10:8080/job/projetpfe/buildWithParameters?TARGET_STAGE1=" +a;

        // win
          String username = "aziz";
        //String username = "aziz";

        //win
         String apiToken = "11ad7512add4d956ec5530229b6ba925f6";
        //String apiToken = "11f72b60ec8526a5e0697bf7b2559686ab";


        // Construire les paramètres de la requête

       // MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

           /* requestBody.add("", TARGET_STAGE1);
        requestBody.add("", TARGET_STAGE2);
        requestBody.add("", TARGET_STAGE3);*/





        // Construire l'en-tête d'authentification
        String authString = username + ":" + apiToken;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString(authString.getBytes()));
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Construire l'entité de requête HTTP avec les paramètres et l'en-tête
        // HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        HttpEntity<String> requestEntity = new HttpEntity<>(headers);


        // Effectuer la requête HTTP POST
        ResponseEntity<String> responseEntity = restTemplate.exchange(jenkinsUrl, HttpMethod.POST, requestEntity, String.class);

        // Vous pouvez traiter la réponse ici si nécessaire
    }









    public String getSonarQubeDashboardUrl() {
        String sonarQubeUrl = "";
        try {
            String jenkinsUrl = "http://192.168.33.10:8080/job/projetpfe/181/consoleText";
            String username = "aziz";
            String apiToken = "112cdd86078587b90e547793e86cae71e3";
            String authString = username + ":" + apiToken;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
            URL url = new URL(jenkinsUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", authHeader);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                String patternString = "http://192.168.33.10:9000/dashboard\\?id=[^\\s]+";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(response.toString());
                if (matcher.find()) {
                    sonarQubeUrl = matcher.group();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sonarQubeUrl;
    }

    public void openUrl(String url) {
        if (url != null) {
            System.out.println("URL : " + url);
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Le paramètre 'url' n'a pas été trouvé dans la liste des actions.");
        }
    }

         public String tandhifa()
         {
             // Supprimer le texte '[INFO]' de l'URL récupérée
             String urlWithInfo = getSonarQubeDashboardUrl();
             String urlWithoutInfo = urlWithInfo.replaceAll("\\[INFO\\]", "").trim();

             System.out.println("URL sans [INFO] : " + urlWithoutInfo);
             return urlWithoutInfo;

         }




    public String getjarfile() {
        String nexusurl = "";
        try {
            String jenkinsUrl = "http://192.168.33.10:8080/job/projetpfe/lastBuild/consoleText";
            String username = "aziz";
            String apiToken = "112cdd86078587b90e547793e86cae71e3";
            String authString = username + ":" + apiToken;
            String authHeader = "Basic " + Base64.getEncoder().encodeToString(authString.getBytes());
            URL url = new URL(jenkinsUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", authHeader);

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                String patternString = "http://192\\.168\\.33\\.10:8081/repository/repo\\/[^\\s]+?\\.jar";
                Pattern pattern = Pattern.compile(patternString);
                Matcher matcher = pattern.matcher(response.toString());
                if (matcher.find()) {
                    nexusurl = matcher.group();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nexusurl;
    }

    public void downloadjarfile(String url) {
        if (url != null) {
            System.out.println("URL : " + url);
            try {
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Le paramètre 'url' n'a pas été trouvé dans la liste des actions.");
        }
    }














}



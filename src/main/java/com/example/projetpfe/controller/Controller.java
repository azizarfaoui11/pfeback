package com.example.projetpfe.controller;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.projetpfe.service.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class Controller {   

    @Autowired
    srv s;

    String url="";
    String token="";
    String username="";

    @PostMapping("/pipeline")
        public ResponseEntity<String> triggerPipeline(@RequestBody PipelineParams pipelineParams) {
        // Votre logique de traitement ici
        System.out.println("Received pipelineParams:");
        System.out.println("targetstage1: " + pipelineParams.getTargetStage1());
        System.out.println("targetstage2: " + pipelineParams.getTargetStage2());
        System.out.println("targetstage3: " + pipelineParams.getTargetStage3());
        System.out.println("targetstage4: " + pipelineParams.getTargetStage4());
        System.out.println("targetstage5: " + pipelineParams.getTargetStage5());
        System.out.println("targetstage6: " + pipelineParams.getTargetStage6());
        System.out.println("targetstage7: " + pipelineParams.getTargetStage7());
        System.out.println("targetstage8: " + pipelineParams.getTargetStage8());
        System.out.println("targetstage9: " + pipelineParams.getTargetStage9());
        System.out.println("targetstage10: " + pipelineParams.getTargetStage10());
        System.out.println("targetstage11: " + pipelineParams.getTargetStage11());
        System.out.println("targetstage12: " + pipelineParams.getTargetStage12());
        System.out.println("targetstage13: " + pipelineParams.getTargetStage13());
        System.out.println("targetstage14: " + pipelineParams.getTargetStage14());
        System.out.println("targetstage15: " + pipelineParams.getTargetStage15());
        System.out.println("targetstage16: " + pipelineParams.getTargetStage16());
        System.out.println("targetstage17: " + pipelineParams.getTargetStage17());
        System.out.println("targetstage18: " + pipelineParams.getTargetStage18());
        System.out.println("targetstage19: " + pipelineParams.getTargetStage19());
        System.out.println("targetstage20: " + pipelineParams.getTargetStage20());
        System.out.println("targetstage21: " + pipelineParams.getTargetStage21());




        //  s.triggerJenkinsPipeline(pipelineParams);



        // Vous pouvez ajouter ici votre logique de traitement
        // par exemple, appeler un service pour exécuter la pipeline

        return ResponseEntity.ok("Pipeline triggered successfully");
    }



    @PostMapping("/pipeline2")
    public ResponseEntity<String> pipelinsel() {




               s.pipelinewindows();


        // Vous pouvez ajouter ici votre logique de traitement
        // par exemple, appeler un service pour exécuter la pipeline

        return ResponseEntity.ok("Pipeline triggered successfully");
    }


   /* @GetMapping("/sonarqube-dashboard")
    public ResponseEntity<String> getSonarQubeDashboardUrl(
            @RequestParam("url") String url,
            @RequestParam("username") String username,
            @RequestParam("token") String token)  {

        String sonarQubeDashboardUrl = s.getSonarQubeDashboardUrl(url, username, token);

        return ResponseEntity.ok(sonarQubeDashboardUrl);
    }*/

    @GetMapping(    "/sonarqube-url")
    public ResponseEntity<String> getSonarQubeUrl() {
        String sonarQubeUrl = s.getSonarQubeDashboardUrl();
       // System.out.println("URL: " + this.getSonarQubeUrl());

        return ResponseEntity.ok(sonarQubeUrl);

    }


    @GetMapping("/openUrl")
    public String openUrl() {
       // url="http://192.168.33.10:9000/dashboard?id=az";
        url=s.tandhifa();
        s.openUrl(url);
        return "URL testée avec succès : " + url;
    }


    @GetMapping("/tandhifa")
    public void tandhifa()
    {

        System.out.println("URL: "+s.tandhifa() );

    }

    @GetMapping(    "/nexus-url")
    public ResponseEntity<String> getnexusurl() {
        String nexusurl = s.getjarfile();
        // System.out.println("URL: " + this.getSonarQubeUrl());

        return ResponseEntity.ok(nexusurl);

    }
    @GetMapping("/downloadartifact")
    public String artifact() {
        // url="http://192.168.33.10:9000/dashboard?id=az";
        url=s.getjarfile();
        s.downloadjarfile(url);
        return "URL testée avec succès : " + url;
    }
    @GetMapping("/openUrljmeter")
    public String openUrll() {
        url="http://192.168.33.10:8080/job/projetpfe/lastSuccessfulBuild/artifact/resultats.jtl/*view*/";
        //url=s.tandhifa();
        s.openUrl(url);
        return "URL testée avec succès : " + url;
    }








    //@PostMapping("/pipeline")
    /*public ResponseEntity<String> trigger()
    {

        try {
            s.triggerJenkinsPipeline();
            return ResponseEntity.ok("Pipeline déclenché avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors du déclenchement du pipeline : " + e.getMessage());
        }
 }*/

    /*public ResponseEntity<String> triggerPipeline(@RequestBody PipelineParams params) {
        try {
            s.triggerJenkinsPipeline(params);
            return ResponseEntity.ok("Pipeline déclenché avec succès");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Erreur lors du déclenchement du pipeline : " + e.getMessage());
        }
    }*/




}
    
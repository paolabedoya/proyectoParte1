package com.proyectfinal.file.controller;

import com.proyectfinal.file.model.FileModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class EventController {

    @Value("${rutaSentencias}")
    private String rutaSentencias;

    @GetMapping("/evento")
    public String leerSentencia() {
        String s = null;
        try(FileReader fr = new FileReader(rutaSentencias+"Sentences.txt");
            BufferedReader br = new BufferedReader(fr)

        ){while ((s = br.readLine()) != null){
            if(s.startsWith("@Name")){
            System.out.println(s);
            }
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "evento2";
    }

}

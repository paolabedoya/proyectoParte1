package com.proyectfinal.file.controller;
import com.proyectfinal.file.EPL.FIleXML;
import com.proyectfinal.file.message.FileMessage;
import com.proyectfinal.file.model.Decision;
import com.proyectfinal.file.model.FileModel;
import com.proyectfinal.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@CrossOrigin("*")
public class FileController {
    //Inyectamos el servicio
    @Autowired
    FileService fileService;
    @Value("${rutaEPL}")
    private String rutaEpl;
    //@Value("${rutaSentencias}")
    //private String rutaSentencias;

    @PostMapping("/upload")
    public ResponseEntity<List<String>> uploadFiles(@RequestParam("files")MultipartFile[] files) {
        String message = "";

        try {
            List<String> fileNames = new ArrayList<>();
            //fileNames.add(rutaSentencias+"Table.txt");
            //fileNames.add(rutaSentencias+"Sentences.txt");

            for (MultipartFile file : files) {
                fileService.save(file);

                    FIleXML m = new FIleXML();
                    String rutaArchivo = rutaEpl+file.getOriginalFilename();
                    System.out.println(rutaArchivo);
                     List<Decision> decisions = m.parseXML(rutaArchivo);

                     m.crearTablaParse (decisions);
            }
            message = "Se subieron los archivos correctamente " + fileNames;
            return ResponseEntity.status(HttpStatus.OK).body(fileNames);
        } catch (Exception e) {
            message = e.getMessage() ;
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Collections.singletonList(message));
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<FileModel>> getFiles(){
        List<FileModel> fileInfos = fileService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFile",
                    path.getFileName().toString()).build().toString();
            return new FileModel(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
/*
    @GetMapping("/filesOut")
    public ResponseEntity<List<FileModel>> getFilesOutputs(){
        List<FileModel> fileInfos = fileService.loadAllOutputs().map(path -> {
            String fileOut = path.getFileName().toString();
            String url = MvcUriComponentsBuilder.fromMethodName(FileController.class, "getFileOut",
                    path.getFileName().toString()).build().toString();
            return new FileModel(fileOut, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }*/

    @GetMapping("/files/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename){
        Resource file = fileService.load(filename);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\""+file.getFilename() + "\"").body(file);
    }
    /*
    @GetMapping("/files/{fileOut:.+}")
    public ResponseEntity<Resource> getFileOut(@PathVariable String fileOut){
        Resource out = fileService.loadOut(fileOut);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; fileOut=\""+out.getFilename() + "\"").body(out);
    }*/

    @GetMapping("/delete/{filename:.+}")
    public ResponseEntity<FileMessage> deleteFile(@PathVariable String filename) {
        String message = "";
        try {
            message = fileService.deleteFile(filename);
            return ResponseEntity.status(HttpStatus.OK).body(new FileMessage(message));
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new FileMessage(message));
        }
    }
}

package com.example.TopExamSpring.controllers;

import com.example.TopExamSpring.model.Image;
import com.example.TopExamSpring.repositories.ImagesRepository;
import com.example.TopExamSpring.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImagesRepository imagesRepository;

//    @GetMapping("/{id}")
//    public ResponseEntity<?> getImageId(@PathVariable("id") int id) {
//        Image image = imagesRepository.findById(id).orElse(null);
//        return ResponseEntity.ok()
//                .header("fileName", image.getOriginalFileName())
//                .contentType(MediaType.valueOf(image.getContentType()))
//                .contentLength(image.getSize())
//                .body(new InputStreamResource(new ByteArrayInputStream(image.getData())));
//    }

}

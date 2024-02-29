package com.example.TopExamSpring.service;

import com.example.TopExamSpring.model.Image;
import com.example.TopExamSpring.repositories.ImagesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ImageService {

    private final ImagesRepository imagesRepository;

    public List<Image> getAllImage() {
        log.info("Вывод всех фото: ");
        return imagesRepository.findAll();
    }

    public Image getImageById(int id) {
        log.info("Вывод фото по id:");
        return imagesRepository.getById(id);
    }

//    @Transactional
//    public void saveImage(Image image) {
//        log.info("Сохранение фото:");
////        Image imageEntity = toImageEntity(file);
//        imagesRepository.save(toImageEntity((MultipartFile) image));
//    }
//
//    @Transactional
//    public Image toImageEntity(MultipartFile file){
//        Image image = new Image();
//        image.setName(file.getName());
//        image.setOriginalFileName(file.getOriginalFilename());
//        image.setContentType(file.getContentType());
//        image.setSize(image.getSize());
//        image.setData(image.getData());
//        return image;
//    }
}

package com.javainuse.controller;

import com.javainuse.db.imageRepository;
import com.javainuse.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@RestController
@CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4200"})
@RequestMapping(path = "image")
public class ImageController {
    @Autowired
    imageRepository imageRepository;

    @PostMapping("/upload")
    public Long uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {

        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        Image img = new Image(compressZLib(file.getBytes()));
        return imageRepository.save(img).getId();
    }

    @GetMapping(path = {"/{id}"})
    public Image getImage(@PathVariable("id") Long id) throws IOException {

        Optional<Image> retrievedImage = imageRepository.findTopById(id);
        return retrievedImage.map(image -> new Image(decompressZLib(image.getImage()))).orElse(null);
    }

    @GetMapping(path = {"/all"})
    public List<Image> getAllImages() {

        List<Image> images = imageRepository.findAll();
        List<Image> finalImages = new ArrayList<>();
        for (Image image : images) {
            Image img = new Image(decompressZLib(image.getImage()));
            img.setId(image.getId());
            finalImages.add(img);
        }
        return finalImages;
    }

    @DeleteMapping("/{id}")
    public String deleteImage(@PathVariable("id") Long id){
        if(!imageRepository.findById(id).isPresent()){
            return "no image found";
        }
        else{
            imageRepository.deleteById(id);
            return "deleted";
        }
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressZLib(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);

        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressZLib(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ioe) {
            ioe.printStackTrace();
        }
        return outputStream.toByteArray();
    }
}

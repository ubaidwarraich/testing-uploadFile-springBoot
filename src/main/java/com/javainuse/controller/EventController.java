package com.javainuse.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.javainuse.db.imageRepository;
import com.javainuse.model.Event;
import com.javainuse.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.javainuse.db.eventRepository;

@RestController
@CrossOrigin(origins = {"http://localhost:4201", "http://localhost:4200"})
@RequestMapping(path = "event")
public class EventController {

    @Autowired
    eventRepository eventRepository;

    @Autowired
    imageRepository imageRepository;

    @PostMapping()
    public Event createEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @GetMapping("/all")
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Event> getById(@PathVariable("id") Long id) {
        return eventRepository.findById(id);
    }
    
    @PutMapping("/{id}")
    public Event updateEvent(@PathVariable("id") Long id,@RequestBody Event event){
        Optional<Event> event1=eventRepository.findById(id);
        return event1.map(event2 -> {
            event2.setTitle(event.getTitle());
            event2.setDescription(event.getDescription());
            event2.setDate(event.getDate());
            event2.setVenue(event.getVenue());
            event2.setImageId(event.getImageId());
           return eventRepository.save(event2);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public Event DeleteEvent(@PathVariable("id") Long id){
         eventRepository.deleteById(id);
         return null;
    }

    @PostMapping("/upload")
    public Long uplaodImage(@RequestParam("imageFile") MultipartFile file) throws IOException {

        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        Image img = new Image(compressZLib(file.getBytes()));
        return imageRepository.save(img).getId();
    }

    @GetMapping(path = {"/image/{id}"})
    public Image getImage(@PathVariable("id") Long id) throws IOException {

        Optional<Image> retrievedImage = imageRepository.findTopById(id);
        return retrievedImage.map(image -> new Image(decompressZLib(image.getImage()))).orElse(null);
    }

    @GetMapping(path = {"/image/all"})
    public List<Image> getAllImages(){

        List<Image> images=imageRepository.findAll();
        List<Image> finalImages=new ArrayList<>();
        for(Image image:images){
            Image img=new Image(decompressZLib(image.getImage()));
            img.setId(image.getId());
           finalImages.add(img);
        }
       return finalImages;
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
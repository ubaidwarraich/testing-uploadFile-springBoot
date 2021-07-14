package com.javainuse.controller;

import com.javainuse.db.imageRepository;
import com.javainuse.db.newsRepository;
import com.javainuse.model.Image;
import com.javainuse.model.News;
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
@RequestMapping(path = "news")
public class NewsController {
    @Autowired
    newsRepository newsRepository;

    @PostMapping()
    public News createNews(@RequestBody News news) {
        return newsRepository.save(news);
    }

    @GetMapping("/recent")
    public List<News> getRecent() {
        return newsRepository.findRecent();
    }

    @GetMapping("/all")
    public List<News> getAll() {
        return newsRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<News> getById(@PathVariable("id") Long id) {
        return newsRepository.findById(id);
    }

    @PutMapping("/{id}")
    public News updateNews(@PathVariable("id") Long id, @RequestBody News event) {
        Optional<News> event1 = newsRepository.findById(id);
        return event1.map(event2 -> {
            event2.setTitle(event.getTitle());
            event2.setDescription(event.getDescription());
            event2.setDate(event.getDate());
            event2.setAuthor(event.getAuthor());
            event2.setImageId(event.getImageId());
            return newsRepository.save(event2);
        }).orElse(null);
    }

    @DeleteMapping("/{id}")
    public News DeleteEvent(@PathVariable("id") Long id) {
        newsRepository.deleteById(id);
        return null;
    }
}

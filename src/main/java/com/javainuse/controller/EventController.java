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
import com.javainuse.model.News;
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
    public Event updateEvent(@PathVariable("id") Long id, @RequestBody Event event) {
        Optional<Event> event1 = eventRepository.findById(id);
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
    public Event DeleteEvent(@PathVariable("id") Long id) {
        eventRepository.deleteById(id);
        return null;
    }

    @GetMapping("/recent")
    public List<Event> getRecent() {
        return eventRepository.findRecent();
    }


}
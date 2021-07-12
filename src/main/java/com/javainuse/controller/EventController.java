package com.javainuse.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.javainuse.db.eventRepository;
import com.javainuse.model.Event;

@RestController
@CrossOrigin(origins = {"http://localhost:4201","http://localhost:4200"})
@RequestMapping(path = "image")
public class EventController {

	@Autowired
	eventRepository imageRepository;


	@PostMapping("/upload")
	public BodyBuilder uplaodImage(@RequestParam("imageFile") MultipartFile file, @RequestPart("event") Event event) throws IOException {

		System.out.println("Original Image Byte Size - " + file.getBytes().length);
		Event img = new Event(event.getVenue(),event.getDate(),event.getTitle(), event.getDescription(),
				compressZLib(file.getBytes()));
		imageRepository.save(img);
		return ResponseEntity.status(HttpStatus.OK);
	}

	@GetMapping(path = { "/get/{imageName}" })
	public Event getImage(@PathVariable("imageName") String imageName) throws IOException {

		final Optional<Event> retrievedImage = imageRepository.findTopByTitle(imageName);
		Event img = new Event(retrievedImage.get().getVenue(),retrievedImage.get().getDate(),retrievedImage.get().getTitle(), retrievedImage.get().getDescription(),
				decompressZLib(retrievedImage.get().getImage()));
		return img;
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
		} catch (IOException ioe) {
		} catch (DataFormatException e) {
		}
		return outputStream.toByteArray();
	}
}
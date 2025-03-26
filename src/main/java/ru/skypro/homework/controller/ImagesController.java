package ru.skypro.homework.controller;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.util.ImageUtils;

import java.io.IOException;

@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImagesController {
    ImageUtils imageUtils;

    @SneakyThrows
    @GetMapping("{imagePath}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<byte[]> image(Object o) throws IOException {
        return image(null);
    }

    @GetMapping(value = "/{imageName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> getImage(
            @PathVariable String imageName
    ) {
        byte[] imageBytes = imageUtils.getImageAsBytes("/" + imageName);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(imageBytes);
    }
}
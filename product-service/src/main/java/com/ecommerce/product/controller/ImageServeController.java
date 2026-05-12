package com.ecommerce.product.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequiredArgsConstructor
public class ImageServeController {
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/products/";

    @GetMapping("/products/images")
    public ResponseEntity<Resource> getImage(@RequestParam String fileName) throws IOException {
        Path path = Paths.get(UPLOAD_DIR, fileName);
        Resource resource = new UrlResource(path.toUri());
        if(!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
    }
}

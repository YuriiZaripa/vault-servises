package org.example.testvaultservice.aws.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.example.testvaultservice.aws.service.AswService;

import java.io.IOException;

@RestController
@RequestMapping("/aws")
@RequiredArgsConstructor
public class AswController {

    private final AswService awsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    void saveImage(@RequestParam("upload") MultipartFile file) throws IOException {
        awsService.saveImage(file);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable("filename") String filename) throws IOException {

        Resource resource = awsService.downloadImage(filename);
        String contentType = "application/octet-stream";

        return ResponseEntity.ok()
                             .contentType(MediaType.parseMediaType(contentType))
                             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                             .body(resource);
    }
}

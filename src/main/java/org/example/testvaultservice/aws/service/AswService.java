package org.example.testvaultservice.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AswService {

    private final AmazonS3 amazonS3;

    @Value("${aws.bucket-name}")
    private String bucketName;

    public void saveImage(MultipartFile file) throws IOException {
        byte[] encodedString = Base64.getEncoder().encode(file.getBytes());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(encodedString.length);
        PutObjectResult putObjectResult = amazonS3.putObject(bucketName,
                                                             file.getOriginalFilename(),
                                                             new ByteArrayInputStream(encodedString),
                                                             objectMetadata);

        System.out.println("____________AWS_____________\nFile with ETag" +
                                   putObjectResult.getETag() +
                                   "\n____________________________");
    }

    public Resource downloadImage(String filename) throws IOException {
        S3Object s3Object = amazonS3.getObject(bucketName, filename);
        var encodedBytes = s3Object.getObjectContent().readAllBytes();
        byte[] decodedBytes = Base64.getDecoder().decode(encodedBytes);
        Resource resource = new ByteArrayResource(decodedBytes);
        return resource;
    }
}

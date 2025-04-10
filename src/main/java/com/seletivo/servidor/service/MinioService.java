package com.seletivo.servidor.service;

import io.minio.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import jakarta.annotation.PostConstruct;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    @Value("${minio.bucket}")
    private String bucketName;

    private final MinioClient minioClient;

    @PostConstruct
    public void init() {
        try {
            boolean bucketExists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!bucketExists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket {} criado com sucesso", bucketName);
            } else {
                log.info("Bucket {} já existe", bucketName);
            }
        } catch (Exception e) {
            log.error("Erro ao inicializar bucket: {}", e.getMessage());
            throw new RuntimeException("Erro ao inicializar bucket", e);
        }
    }

    public void uploadFile(String objectName, MultipartFile file) throws Exception {
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build()
            );
        } catch (Exception e) {
            log.error("Erro ao fazer upload do arquivo: {}", e.getMessage());
            throw new RuntimeException("Erro ao fazer upload do arquivo", e);
        }
    }

    public String getPresignedUrl(String objectName) throws Exception {
        try {
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .method(io.minio.http.Method.GET)
                    .expiry(5, TimeUnit.MINUTES)
                    .build()
            );
        } catch (Exception e) {
            log.error("Erro ao gerar URL do arquivo: {}", e.getMessage());
            throw new RuntimeException("Erro ao gerar URL do arquivo", e);
        }
    }

    public void deleteFile(String objectName) throws Exception {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
        } catch (Exception e) {
            log.error("Erro ao deletar arquivo: {}", e.getMessage());
            throw new RuntimeException("Erro ao deletar arquivo", e);
        }
    }

    public String getBucketName() {
        return bucketName;
    }
} 

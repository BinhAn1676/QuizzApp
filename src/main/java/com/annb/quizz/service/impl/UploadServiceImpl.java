package com.annb.quizz.service.impl;

import com.annb.quizz.service.UploadFileService;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import static java.lang.String.join;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadFileService {
    private final Logger log = Logger.getLogger(UploadServiceImpl.class.getName());
    private final Cloudinary cloudinary;

    @Override
    public String uploadImage(MultipartFile file) {
        try{
            assert file.getOriginalFilename() != null;
            String publicValue = generatePublicValue(file.getOriginalFilename());
            System.out.println("publicValue is: {" + publicValue + "}");
            String extension = getFileName(file.getOriginalFilename())[1];
            System.out.println("extension is: {" + extension + "}");
            byte[] bytes = file.getBytes();
            Map uploadResult = cloudinary.uploader().upload(
                    bytes,
                    ObjectUtils.asMap("public_id", publicValue)
            );
            System.out.println("UPLOAD RESULT " + uploadResult);
            // Generate the URL
            return cloudinary.url().generate(join(publicValue, ".", extension));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private File convert(MultipartFile file) throws IOException {
        assert file.getOriginalFilename() != null;
        File convFile = new File(StringUtils.join(generatePublicValue(file.getOriginalFilename()), getFileName(file.getOriginalFilename())[1]));
        try (InputStream is = file.getInputStream()) {
            Files.copy(is, convFile.toPath());
        }
        return convFile;
    }

    private void cleanDisk(File file) {
        try {
            System.out.println("file.toPath(): {" + file.toPath() + "}");
            Path filePath = file.toPath();
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    public String generatePublicValue(String originalName) {
        String fileName = getFileName(originalName)[0];
        return join(UUID.randomUUID().toString(), "_", fileName);
    }

    public String[] getFileName(String originalName) {
        return originalName.split("\\.");
    }
}

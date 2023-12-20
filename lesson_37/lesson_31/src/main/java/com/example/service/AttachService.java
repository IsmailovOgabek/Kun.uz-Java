package com.example.service;

import com.example.dto.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachService {
    @Value("${attach.upload.folder}")
    private String attachFolder;

    @Value("${attach.open.url}")
    private String attachOpenUrl;
    @Autowired
    private AttachRepository attachRepository;

    public AttachDTO saveToSystem(MultipartFile file) {
        try {
            // attaches/2022/04/23/UUID.png
            String attachPath = getYmDString(); // 2022/04/23
            String extension = getExtension(file.getOriginalFilename()); // .png....
            String uuid = UUID.randomUUID().toString();
            String fileName = uuid + "." + extension; // UUID.png

            File folder = new File(attachFolder + attachPath);  // attaches/2022/04/23/
            if (!folder.exists()) {
                folder.mkdirs();
            }

            byte[] bytes = file.getBytes();

            Path path = Paths.get(attachFolder + attachPath + "/" + fileName); // attaches/2022/04/23/UUID.png
            Files.write(path, bytes);

            AttachEntity entity = new AttachEntity();
            entity.setPath(attachPath);
            entity.setExtension(extension);
            entity.setSize(file.getSize());
            entity.setOriginalName(file.getOriginalFilename());
            entity.setCreatedData(LocalDateTime.now());
            entity.setId(uuid);
            attachRepository.save(entity);

            AttachDTO attachDTO = new AttachDTO();
            attachDTO.setId(entity.getId());
            attachDTO.setOriginalName(file.getOriginalFilename());
            //...
            attachDTO.setUrl(attachOpenUrl + fileName);

            return attachDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] loadImage(String fileName) {
        //
        String id = fileName.split("\\.")[0];
        byte[] imageInByte;
        BufferedImage originalImage;
        try {
            Optional<AttachEntity> optional = attachRepository.findById(id);
            if (optional.isEmpty()) {
                return new byte[0];
            }

            AttachEntity entity = optional.get();
            // attaches/2022/04/23/5e2733ba-0fe0-4142-abc6-a48c49d7e8da.PNG

            originalImage = ImageIO.read(new File(attachFolder + entity.getPath() + "/" + fileName));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public String saveToSystemOld(MultipartFile file) {
        try {
            File folder = new File("attaches");
            if (!folder.exists()) {
                folder.mkdir();
            }

            byte[] bytes = file.getBytes();


            Path path = Paths.get("attaches/" + file.getOriginalFilename());
            Files.write(path, bytes);
            return file.getOriginalFilename();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 20022_12_7_2fd399c0-767a-4763-ab7c-2e8c699cfd83.PNG
    public byte[] loadImageOld(String fileName) {
        byte[] imageInByte;
        BufferedImage originalImage;
        try {
            originalImage = ImageIO.read(new File("attaches/" + fileName));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, "png", baos);

            baos.flush();
            imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public byte[] open_general(String fileName) {
        byte[] data;
        try {
            Path file = Paths.get("attaches/" + fileName);
            data = Files.readAllBytes(file);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public Resource download(String fileName) {
        try {
            Path file = Paths.get("attaches/" + fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2022/04/23
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

}

package ru.hogwarts.school.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.repository.StudentAvatarRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;
@Slf4j
@Service
@Transactional
public class StudentAvatarService {
    @Value("${students.avatar.dir.patch}")
    private String avatarsDir;

    private final StudentService studentService;
    private final StudentAvatarRepository studentAvatarRepository;

    public StudentAvatarService(StudentService studentService, StudentAvatarRepository studentAvatarRepository) {
        this.studentService = studentService;
        this.studentAvatarRepository = studentAvatarRepository;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException{
        log.info("Was invoked method for uploadAvatar student: " +  studentId);
        Student student = studentService.findStudentById(studentId);
        Path filePath = Path.of(avatarsDir,studentId+"."+ getExtension((file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is,1024);
             BufferedOutputStream bos = new BufferedOutputStream(os,1024);
        ){
            bis.transferTo(bos);
        }
        StudentAvatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));

        studentAvatarRepository.save(avatar);
        log.info("Avatar for student with id: "+studentId+" has been upload");
    }
    public StudentAvatar findAvatar(Long id){
        log.info("Was invoked method for findAvatar student: " +  id);
        return studentAvatarRepository.findByStudentId(id).orElse(new StudentAvatar());

    }
    private byte[] generateImagePreview(Path filePath) throws IOException{
        try(InputStream is = Files.newInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(is, 1024);
            ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            BufferedImage image = ImageIO.read(bis);

            Integer height = image.getHeight()/(image.getWidth()/100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();
            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }
    private String getExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

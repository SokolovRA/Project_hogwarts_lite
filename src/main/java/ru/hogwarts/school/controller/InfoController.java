package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.dto.InfoDTO;
import ru.hogwarts.school.model.Info;

@RestController
public class InfoController {
    @Value("${app.env}")
    private String devEnv;
    @GetMapping("/appInfo")
  
    public InfoDTO appInfo(){
    Info info = new Info("hogwarts-school", "0.0.1", devEnv);
    return InfoDTO.fromInfo(info);
    }
}

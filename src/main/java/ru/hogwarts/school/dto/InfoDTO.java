package ru.hogwarts.school.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.hogwarts.school.model.Info;

@Data
@NoArgsConstructor
public class InfoDTO {
    private String appName;
    private String appVersion;
    private String appEnvironment;

    public static InfoDTO fromInfo(Info info){
        InfoDTO dto = new InfoDTO();
        dto.setAppName(info.getAppName());
        dto.setAppVersion(info.getAppVersion());
        dto.setAppEnvironment(info.getAppEnvironment());
        return dto;
    }
}


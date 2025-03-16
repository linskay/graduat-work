package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.dto.AppInfoDTO;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check", description = "Контроллер для проверки состояния приложения")
public class HealthCheckController {

    @Value("${app.name}")
    private String appName;

    @Value("${app.description}")
    private String appDescription;

    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    @Operation(
            summary = "Проверка состояния приложения",
            description = "Возвращает информацию о приложении: название, описание и версию.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Информация о приложении успешно получена",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AppInfoDTO.class)
                            )
                    )
            })
    public AppInfoDTO healthCheck() {
        AppInfoDTO appInfo = new AppInfoDTO();
        appInfo.setName(appName);
        appInfo.setDescription(appDescription);
        appInfo.setVersion(appVersion);
        return appInfo;
    }
}
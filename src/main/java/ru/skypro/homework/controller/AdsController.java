package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.Ads;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.service.AdService;

import java.util.List;

@Slf4j
@CrossOrigin(value = "http://localhost:3000")
@RestController
@RequestMapping("/ads")
@RequiredArgsConstructor
@Tag(name = "Объявления", description = "Операции с объявлениями")
public class AdsController {

    private final AdService adService;

    @GetMapping
    @Operation(
            summary = "Получение всех объявлений",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ads.class)
                            )
                    )
            }
    )
    public Ads getAllAds() {
        return adService.getAllAds();
    }

    @PostMapping(consumes = "multipart/form-data")
    @Operation(
            summary = "Добавление объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public Integer addAd(
            @RequestPart("properties") CreateOrUpdateAd properties,
            @RequestPart("image") MultipartFile image) {
        Ad ad = adService.addAd(properties, image);
        return ad.getPk();
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получение информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ExtendedAd.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @PreAuthorize("isAuthenticated()")
    public ExtendedAd getAd(@PathVariable Integer id) {
        return adService.getAd(id);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление объявления",
            responses = {
                    @ApiResponse(responseCode = "204", description = "No Content"),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeAd(@PathVariable Integer id) {
        adService.removeAd(id);
    }

    @PatchMapping("/{id}")
    @Operation(
            summary = "Обновление информации об объявлении",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public Ad updateAd(
            @PathVariable Integer id,
            @RequestBody CreateOrUpdateAd updatedAd) {
        return adService.updateAd(id, updatedAd);
    }

    @GetMapping("/me")
    @Operation(
            summary = "Получение объявлений авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Ad.class)
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized")
            }
    )
    public Ads getAdsMe() {
        List<Ad> ads = adService.getAdsMe();
        Ads response = new Ads();
        response.setCount(ads.size());
        response.setResults(ads);
        return response;
    }

    @PatchMapping(value = "/{id}/image", consumes = "multipart/form-data")
    @Operation(
            summary = "Обновление картинки объявления",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/octet-stream",
                                    schema = @Schema(type = "string", format = "byte")
                            )
                    ),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not found")
            }
    )
    public ResponseEntity<byte[]> updateImage(
            @PathVariable Integer id,
            @RequestPart("image") MultipartFile image) {
        byte[] imageBytes = adService.updateImage(id, image);
        return ResponseEntity.ok(imageBytes);
    }
}
package ru.skypro.homework.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.model.AdEntity;
import ru.skypro.homework.model.UserEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AdMapperTest {

    @Autowired
    private AdMapper adMapper;

    public AdMapperTest() {
        this.adMapper = Mappers.getMapper(AdMapper.class);
    }

    private AdEntity createAdEntity() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setPhone("+79251234567");
        user.setFirstName("Иван");
        user.setLastName("Иванов");

        AdEntity adEntity = new AdEntity();
        adEntity.setId(1);
        adEntity.setTitle("Продам велосипед");
        adEntity.setPrice(1000);
        adEntity.setDescription("Отличный велосипед");
        adEntity.setAuthor(user);

        return adEntity;
    }

    @Test
    @DisplayName("Проверка преобразования AdEntity в Ad")
    void testAdEntityToAd() {
        AdEntity adEntity = createAdEntity();

        Ad ad = adMapper.adEntityToAd(adEntity);

        assertThat(ad).isNotNull();
        assertThat(ad.getPk()).isEqualTo(adEntity.getId());
        assertThat(ad.getTitle()).isEqualTo(adEntity.getTitle());
        assertThat(ad.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(ad.getAuthor()).isEqualTo(adEntity.getAuthor().getId());
        assertThat(ad.getImage()).isNull();
    }

    @Test
    @DisplayName("Проверка преобразования AdEntity в ExtendedAd")
    void testAdEntityToExtendedAd() {
        AdEntity adEntity = createAdEntity();

        ExtendedAd extendedAd = adMapper.adEntityToExtendedAd(adEntity);

        assertThat(extendedAd).isNotNull();
        assertThat(extendedAd.getPk()).isEqualTo(adEntity.getId());
        assertThat(extendedAd.getTitle()).isEqualTo(adEntity.getTitle());
        assertThat(extendedAd.getPrice()).isEqualTo(adEntity.getPrice());
        assertThat(extendedAd.getDescription()).isEqualTo(adEntity.getDescription());
        assertThat(extendedAd.getEmail()).isEqualTo(adEntity.getAuthor().getEmail());
        assertThat(extendedAd.getAuthor()).isEqualTo(adEntity.getAuthor().getId());
        assertThat(extendedAd.getPhone()).isEqualTo(adEntity.getAuthor().getPhone());
        assertThat(extendedAd.getAuthorFirstName()).isEqualTo(adEntity.getAuthor().getFirstName());
        assertThat(extendedAd.getAuthorLastName()).isEqualTo(adEntity.getAuthor().getLastName());
        assertThat(extendedAd.getImage()).isNull();
    }

    @Test
    @DisplayName("Проверка преобразования CreateOrUpdateAd в AdEntity")
    void testCreateOrUpdateAdDTOToAdEntity() {
        CreateOrUpdateAd dto = new CreateOrUpdateAd();
        dto.setTitle("Продам велосипед");
        dto.setPrice(1000);
        dto.setDescription("Отличный велосипед");

        AdEntity entity = adMapper.createOrUpdateAdDTOToAd(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getTitle()).isEqualTo(dto.getTitle());
        assertThat(entity.getPrice()).isEqualTo(dto.getPrice());
        assertThat(entity.getDescription()).isEqualTo(dto.getDescription());
        assertThat(entity.getId()).isNull();
        assertThat(entity.getAuthor()).isNull();
        assertThat(entity.getImage()).isNull();
    }

    @Test
    @DisplayName("Проверка преобразования Ad в AdEntity")
    void testAdToAdEntity() {
        Ad ad = new Ad();
        ad.setPk(1);
        ad.setTitle("Продам кирпич");
        ad.setPrice(1000);
        ad.setAuthor(1L);

        AdEntity entity = adMapper.adToAdEntity(ad);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isEqualTo(ad.getTitle());
        assertThat(entity.getPrice()).isEqualTo(ad.getPrice());
        assertThat(entity.getAuthor()).isNull();
        assertThat(entity.getImage()).isNull();
    }
}
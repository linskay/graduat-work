package ru.skypro.homework.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.util.AuthenticationUtils;

@Service
@RequiredArgsConstructor
public class AdSecurityService {

    private final AdRepository adRepository;
    private final AuthenticationUtils authenticationUtils;

    public boolean isAuthor(Integer adId) {
        return adRepository.findById(adId)
                .map(adEntity -> adEntity.getAuthor().equals(authenticationUtils.getAuthenticatedUser()))
                .orElse(false);
    }
}
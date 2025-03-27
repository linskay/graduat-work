package ru.skypro.homework.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.User;
import ru.skypro.homework.exception.InvalidPasswordException;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.model.UserEntity;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.util.AuthenticationUtils;
import ru.skypro.homework.util.ImageUtils;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationUtils authenticationUtils;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ImageUtils imageUtils;

    @Override
    public void changePassword(NewPassword newPassword) {
        UserEntity userEntity = getCurrentUser();
        if (!userEntity.getPassword().matches(newPassword.getCurrentPassword())) {
            throw new InvalidPasswordException("Некорректный пароль");
        }
        userEntity.setPassword(newPassword.getNewPassword());
        userRepository.save(userEntity);
    }

    @Override
    public User getUser() {
        UserEntity userEntity = getCurrentUser();
        return userMapper.toUserDTO(userEntity);
    }

    @Override
    public UpdateUser updateUser(UpdateUser updateUser) {
        UserEntity userEntity = getCurrentUser();
        userMapper.updateUserFromDTO(updateUser, userEntity);
        userRepository.save(userEntity);
        return userMapper.toUpdateUser(userEntity);
    }

//        @Override
//        public byte[] updateUserAvatar(Long id, MultipartFile image) {
//        UserEntity currentUser = getCurrentUser();
//        String avatarUrl = imageUtils.saveImage(image);
//        currentUser.setImage(avatarUrl);
//        userRepository.save(currentUser);
//        return avatarUrl.getBytes();
//    }

    public UserEntity getCurrentUser() {
        return authenticationUtils.getAuthenticatedUser();
    }

    @Override
    public String updateUserImage(MultipartFile file) {
        UserEntity currentUser = getCurrentUser();

        String relativeImagePath = imageUtils.saveImage(file);

        currentUser.setImage(relativeImagePath);
        userRepository.save(currentUser);

        return imageUtils.getBaseUrl() + relativeImagePath;
    }
}
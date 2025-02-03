package com.example.bookingservice.service;

import com.example.bookingservice.exception.EntityAlreadyExistsException;
import com.example.bookingservice.model.User;
import com.example.bookingservice.repository.UserRepository;
import com.example.bookingservice.utils.AppMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    //TODO: добавить проверку на то, что пользователь с таким именем уже существует
    @Override
    public User save(User user) {
        Optional<User> newUser = userRepository.findByEmailAndUserName(user.getEmail(), user.getUserName());
        if(newUser.isPresent()) throw new EntityAlreadyExistsException(
                MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Пользователь", user.getUserName())
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User with name " +
                        userName + "not found")
        );
    }

}

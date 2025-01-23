package com.booking.demo.service;

import com.booking.demo.exception.EntityAlreadyExistsException;
import com.booking.demo.model.User;
import com.booking.demo.repository.UserRepository;
import com.booking.demo.utils.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        Optional<User> newUser = userRepository.findByEmailAndUserName(user.getEmail(), user.getUserName());
        if(newUser.isPresent()) throw new EntityAlreadyExistsException(
                MessageFormat.format(Strings.ENTITY_ALREADY_EXISTS, "Пользователь", user.getUserName())
        );
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.saveAndFlush(user);
    }


}

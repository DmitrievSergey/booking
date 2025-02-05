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
import java.util.concurrent.locks.ReentrantLock;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    ReentrantLock lock = new ReentrantLock();
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        lock.lock();
        Optional<User> newUser = userRepository.findByEmailAndUserName(user.getEmail(), user.getUserName());
        if (newUser.isPresent()) {
            lock.unlock();
            throw new EntityAlreadyExistsException(
                    MessageFormat.format(AppMessages.ENTITY_ALREADY_EXISTS, "Пользователь", user.getUserName())
            );
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.saveAndFlush(user);
        lock.unlock();
        return savedUser;
    }

    @Override
    public User findByUserName(String userName) {
        return userRepository.findByUserName(userName).orElseThrow(
                () -> new UsernameNotFoundException("User with name " +
                        userName + " not found")
        );
    }

}

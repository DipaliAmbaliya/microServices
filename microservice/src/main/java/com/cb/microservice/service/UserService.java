package com.cb.microservice.service;

import com.cb.microservice.client.InstantWebToolsApiClient;
import com.cb.microservice.common.UserStatus;
import com.cb.microservice.entity.UserEntity;
import com.cb.microservice.exception.EntityNotFoundException;
import com.cb.microservice.exception.UserAlreadyRegisteredException;
import com.cb.microservice.exception.config.GlobalErrorCode;
import com.cb.microservice.model.User;
import com.cb.microservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final InstantWebToolsApiClient instantWebToolsApiClient;
//    private final WebClient webClient;
//    private final ReactiveCircuitBreaker readingListCircuitBreaker;

//    public UserService(UserRepository userRepository, InstantWebToolsApiClient instantWebToolsApiClient, ReactiveCircuitBreakerFactory circuitBreakerFactory) {
//        this.userRepository = userRepository;
//        this.instantWebToolsApiClient = instantWebToolsApiClient;
//        this.webClient = WebClient.builder().baseUrl("http://localhost:9091/").build();
//        this.readingListCircuitBreaker = circuitBreakerFactory.create("product");
//    }

    public void createUser(User user){
        Optional<UserEntity> userEntitiesByUsername = userRepository.findUserEntitiesByUsername(user.getUsername());
        if (userEntitiesByUsername.isPresent()) {
            throw new UserAlreadyRegisteredException("exception.user.already.registered", GlobalErrorCode.ERROR_USER_ALREADY_REGISTERED);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(UUID.randomUUID().toString());
        userEntity.setUserStatus(UserStatus.PENDING);
        userEntity.setProductId(user.getProductId());
        userEntity.setCellPhoneNumber(user.getCellPhoneNumber());
        userRepository.save(userEntity);
    }

    public User readUserByUsername(@NotBlank String username) {
        UserEntity userEntity = userRepository.findUserEntitiesByUsername(username).orElseThrow(EntityNotFoundException::new);
        User user = new User();
        BeanUtils.copyProperties(userEntity, user);
        user.setProductId(userEntity.getProductId());
        user.setProductName(readProduct(user.getProductId()));
        return user;
    }

    public String readProduct (String id){
        return instantWebToolsApiClient.readreadProductById(Integer.parseInt(id));
    }

//    public String readProductUSingRetry (String id){
//        return String.valueOf(readingListCircuitBreaker.run(webClient.get().uri("/single-product-name/"+id).retrieve().bodyToMono(String.class), throwable -> {
//            return Mono.just("Cloud Native Java (O'Reilly)");
//        }));
//    }


}

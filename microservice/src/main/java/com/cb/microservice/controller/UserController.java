package com.cb.microservice.controller;

import java.util.List;

import com.cb.microservice.model.User;
import com.cb.microservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Validated
@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

  @GetMapping("/{username}")
  public ResponseEntity readUser (@RequestHeader(value = "token-id") @Pattern(regexp = "^[0-9]+$", message = "token-id should be a digit number.") String tokenId, @PathVariable String username) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("token-id",tokenId);
    return new ResponseEntity(userService.readUserByUsername(username),headers, HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity createUser(@RequestBody @Valid User user)  {
    userService.createUser(user);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity getProduct(@PathVariable String productId){
    return ResponseEntity.ok(userService.readProduct(productId));
  }

//    @GetMapping("/productWithRetry/{productId}")
//    public ResponseEntity productWithRetry(@PathVariable String productId){
//        return ResponseEntity.ok(userService.readProductUSingRetry(productId));
//    }

  @GetMapping("/getUser")
  public ResponseEntity readUser1 (@RequestParam @NotBlank(message = "username is mandatory") String username) {
    return ResponseEntity.ok(userService.readUserByUsername(username));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handlePasswordValidationException(MethodArgumentNotValidException e) {
    //Returning password error message as a response.
    return String.join(",", e.getBindingResult().getFieldError().getDefaultMessage());

  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
    return new ResponseEntity<>("not valid due to validation error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/users")
  public List<String> getUsers() {
    //throw new RuntimeException();
   return List.of("test", "test1");
  }
}

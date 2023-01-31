package com.gksoft.application.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gksoft.application.domain.User;
import com.gksoft.application.repository.UserRepository;
import com.gksoft.application.security.jwt.JWTFilter;
import com.gksoft.application.security.jwt.TokenProvider;
import com.gksoft.application.service.UserService;
import com.gksoft.application.service.dto.AdminUserDTO;
import com.gksoft.application.web.rest.vm.LoginVM;
import javax.validation.Valid;

import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {
    private final Logger log = LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;

    private final UserService userService;
    public UserJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, UserRepository userRepository, UserService userService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userRepository = userRepository;
        this.userService = userService;
    }
    @GetMapping(value = "/generateOTP")
    public ResponseEntity<String> generateOTP(@RequestParam(value = "phoneNumber") String phoneNumber){

        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));

        Verification verification = Verification.creator(
                "VAde35d077c89d8a7fc20c1830562bb795", // this is your verification sid
                phoneNumber, //this is your Twilio verified recipient phone number
                "sms") // this is your channel type
            .create();
       User user =  userService.getUserWithAuthoritiesByLogin(phoneNumber).orElse(null);
       if(user == null){
           user.setLogin(phoneNumber);
           user.setMobileNumber(phoneNumber);
           user.setCreatedDate(Instant.now());
           userService.registerUserOtp(new AdminUserDTO(user));
       }
        System.out.println(verification.getStatus());

        log.info("OTP has been successfully generated, and awaits your verification {}", LocalDateTime.now());

        return new ResponseEntity<>("Your OTP has been sent to your verified phone number", HttpStatus.OK);
    }

    @GetMapping("/verifyOTP")
    public ResponseEntity<JWTToken> verifyUserOTP(@RequestParam(value = "phoneNumber") String phoneNumber,
                                           @RequestParam(value = "otp")   String otp) throws Exception {
        Twilio.init(System.getenv("TWILIO_ACCOUNT_SID"), System.getenv("TWILIO_AUTH_TOKEN"));

        try {

            VerificationCheck verificationCheck = VerificationCheck.creator(
                    "VAde35d077c89d8a7fc20c1830562bb795")
                .setTo(phoneNumber)
                .setCode(otp)
                .create();

            System.out.println(verificationCheck.getStatus());

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            phoneNumber,
           ""
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
     }
    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            loginVM.getUsername(),
            loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.createToken(authentication, loginVM.isRememberMe());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}

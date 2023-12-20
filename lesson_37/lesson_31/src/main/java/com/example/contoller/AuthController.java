package com.example.contoller;

import com.example.dto.*;
import com.example.enums.Language;
import com.example.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    // private Logger log = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Authorization method", description = "Method used for profile authorization")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthorizationDTO dto,
                                   @RequestHeader(value = "Accept-Language", defaultValue = "UZ") Language language) {
        log.info("Authorization: {} " + dto);
        AuthorizationResponseDTO response = authService.authorization(dto, language);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/registration")
    @Operation(summary = "Create Article ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Article Created",
                    content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid info supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "item not found",
                    content = @Content)})
    public ResponseEntity<?> registration(@RequestBody RegistrationDTO dto) {
        log.info("Registration: " + dto);
        String response = authService.registration(dto);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/verification/email/{jtwToken}")
    public ResponseEntity<?> emailVerification(@PathVariable("jtwToken") String jwt) {
        String response = authService.verification(jwt);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verification/phone/")
    public ResponseEntity<?> emailVerification(@RequestBody SmsVerifivationDTO dto) {
        String response = authService.phoneVerification(dto);
        return ResponseEntity.ok(response);
//        return null;
    }


}

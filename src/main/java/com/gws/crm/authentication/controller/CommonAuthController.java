package com.gws.crm.authentication.controller;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.gws.crm.authentication.dto.RefreshTokenDto;
import com.gws.crm.authentication.dto.ResetPasswordDto;
import com.gws.crm.authentication.dto.SignInRequest;
import com.gws.crm.authentication.service.CommonAuthService;
import com.gws.crm.common.exception.InvalidPhoneNumberException;
import com.gws.crm.common.utils.PhoneNumberUtilsService;
import com.gws.crm.core.leads.dto.PhoneNumberDTO;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.gws.crm.common.utils.CountryCodeMapper.getRegionCode;

@RestController
@RequestMapping("auth")
@AllArgsConstructor
@Slf4j
public class CommonAuthController {

    private final CommonAuthService commonAuthService;
    private final PhoneNumberUtilsService phoneNumberUtilsService;
    private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

    @PostMapping("signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest signInRequest) {
        return commonAuthService.signIn(signInRequest);
    }

    @GetMapping("check-email/{email}")
    public ResponseEntity<?> isEmailExist(@PathVariable String email) {
        return commonAuthService.isEmailExist(email);
    }


    @PostMapping("/forget-password")
    public ResponseEntity<?> forgetPasswordRequest(@Valid @RequestParam String email) throws MessagingException {
        return commonAuthService.forgetPasswordRequest(email);
    }

    @PutMapping("/forget-password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) throws MessagingException {
        return commonAuthService.resetPassword(resetPasswordDto);
    }

    @GetMapping("/reset/password/{token}")
    public ResponseEntity<?> isResetPasswordTokenValid(@PathVariable String token) {
        return commonAuthService.isResetPasswordTokenValid(token);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAccessToken(@Valid @RequestBody RefreshTokenDto refreshTokenRequest) {
        return commonAuthService.refreshAccessToken(refreshTokenRequest);
    }

    @PostMapping("/validate-phone")
    public ResponseEntity<String> validatePhoneNumber(@RequestBody PhoneNumberDTO phoneNumberDto) {
        try {
            boolean isValid = phoneNumberUtilsService.isValidPhoneNumber(phoneNumberDto.getPhone(),
                    getRegionCode(phoneNumberDto.getCode()));
            log.info("Country code =>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> {},{}", phoneNumberDto.getCode(), phoneNumberDto.getCode());
            Phonenumber.PhoneNumber phonenumber = phoneUtil.parse(phoneNumberDto.getPhone(),
                    getRegionCode(phoneNumberDto.getCode()));
            log.info("Phone Number {}", phonenumber.getNationalNumber());
            log.info("is valid Phone Number {}", isValid);
            log.info(phoneNumberDto.getCode());
            return ResponseEntity.ok(phoneNumberDto.getCode());
        } catch (InvalidPhoneNumberException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        } catch (NumberParseException e) {
            throw new RuntimeException(e);
        }
    }


}

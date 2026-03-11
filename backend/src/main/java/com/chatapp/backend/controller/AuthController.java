package com.chatapp.backend.controller;

import com.chatapp.backend.dto.*;
import com.chatapp.backend.service.AuthService;
import com.chatapp.backend.service.ForgotPasswordService;
import com.chatapp.backend.service.OtpService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;
    private final ForgotPasswordService forgotPasswordService;

    public AuthController(AuthService authService, OtpService otpService,ForgotPasswordService forgotPasswordService) {
        this.authService = authService;
        this.otpService = otpService;
        this.forgotPasswordService=forgotPasswordService;

    }

    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequest request) {
        return authService.signup(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/resend-otp")
    public String resendOtp(@RequestParam String email) {

        //otpService.resendOtp(email);

        return "OTP resent successfully";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpVerificationRequest request)
    {
        return authService.verifyOtp(request);
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {

        return forgotPasswordService.forgotPassword(request);
    }

    @PostMapping("/reset-password")
    public String resetPassword(@Valid @RequestBody ResetPasswordRequest request) {

        return forgotPasswordService.resetPassword(request);

    }


    @GetMapping("/check-username")
    public boolean checkUsernameAvailability(@RequestParam String username) {
        return authService.isUsernameAvailable(username);
    }


}
package com.chatapp.backend.service;

import com.chatapp.backend.dto.ForgotPasswordRequest;
import com.chatapp.backend.dto.ResetPasswordRequest;
import com.chatapp.backend.entity.OtpVerification;
import com.chatapp.backend.entity.User;
import com.chatapp.backend.repository.OtpVerificationRepository;
import com.chatapp.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final OtpVerificationRepository otpRepository;
    private final OtpService otpService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordService(UserRepository userRepository,
                                 OtpVerificationRepository otpRepository,
                                 OtpService otpService,
                                 EmailService emailService,
                                 PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.otpRepository = otpRepository;
        this.otpService = otpService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public String forgotPassword(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = otpService.generateOtp();

        otpRepository.findByEmail(request.getEmail())
                .ifPresent(otpRepository::delete);

        OtpVerification record = new OtpVerification();
        record.setEmail(request.getEmail());
        record.setOtp(otp);
        record.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(record);

        emailService.sendOtpEmail(request.getEmail(), otp);

        return "OTP sent to email";
    }

    public String resetPassword(ResetPasswordRequest request) {

        OtpVerification record = otpRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!record.getOtp().equals(request.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        if (record.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String hashedPassword = passwordEncoder.encode(request.getNewPassword());
        user.setPassword(hashedPassword);

        userRepository.save(user);

        otpRepository.delete(record);

        return "Password updated successfully";
    }
}
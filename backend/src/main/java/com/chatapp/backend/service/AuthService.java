package com.chatapp.backend.service;

import com.chatapp.backend.dto.AuthResponse;
import com.chatapp.backend.dto.LoginRequest;
import com.chatapp.backend.dto.OtpVerificationRequest;
import com.chatapp.backend.dto.SignupRequest;
import com.chatapp.backend.entity.OtpVerification;
import com.chatapp.backend.entity.User;
import com.chatapp.backend.repository.OtpVerificationRepository;
import com.chatapp.backend.repository.UserRepository;
import com.chatapp.backend.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final OtpVerificationRepository otpVerificationRepository;
    private final EmailService emailService;
    private final OtpService otpService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil,
                       EmailService emailService,
                       OtpVerificationRepository otpVerificationRepository,
                       OtpService otpService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
        this.otpVerificationRepository = otpVerificationRepository;
        this.otpService = otpService;
    }
    public String signup(SignupRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        String hashedPassword = passwordEncoder.encode(request.getPassword());

        String otp = otpService.generateOtp();

        otpService.saveSignupOtp(
                request.getEmail(),
                request.getUsername(),
                hashedPassword,
                otp
        );

        emailService.sendOtpEmail(request.getEmail(), otp);

        return "OTP sent to email";
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        long userID=user.getId();

        return new AuthResponse(token,userID);
    }



    public String verifyOtp(OtpVerificationRequest request) {

        OtpVerification record = otpService.verifyOtp(
                request.getEmail(),
                request.getOtp()
        );

        User user = new User();
        user.setEmail(record.getEmail());
        user.setUsername(record.getUsername());
        user.setPassword(record.getPassword());

        userRepository.save(user);

        otpVerificationRepository.delete(record);

        emailService.sendWelcomeEmail(record.getEmail(), record.getUsername());

        return "Signup successful";
    }


    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }
}
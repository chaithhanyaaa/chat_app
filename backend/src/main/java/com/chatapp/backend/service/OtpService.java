package com.chatapp.backend.service;

import com.chatapp.backend.dto.OtpVerificationRequest;
import com.chatapp.backend.entity.OtpVerification;
import com.chatapp.backend.repository.OtpVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class OtpService {

    private final OtpVerificationRepository otpRepository;
    private final EmailService emailService;

    public OtpService(OtpVerificationRepository otpRepository,
                      EmailService emailService) {
        this.otpRepository = otpRepository;
        this.emailService = emailService;
    }


    public String generateOtp() {

        int otp = (int) (Math.random() * 900000) + 100000;

        return String.valueOf(otp);
    }


    public void sendOtpEmail(String email, String otp) {

        emailService.sendOtpEmail(email, otp);

    }

    public void saveSignupOtp(String email, String username, String password, String otp) {

        Optional<OtpVerification> existing = otpRepository.findByEmail(email);

        existing.ifPresent(otpRepository::delete);

        OtpVerification record = new OtpVerification();

        record.setEmail(email);
        record.setUsername(username);
        record.setPassword(password);
        record.setOtp(otp);
        record.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(record);
    }


    public void saveOtp(String email, String otp) {

        Optional<OtpVerification> existing = otpRepository.findByEmail(email);
        existing.ifPresent(otpRepository::delete);
        OtpVerification otpVerification = new OtpVerification();
        otpVerification.setEmail(email);
        otpVerification.setOtp(otp);
        otpVerification.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(otpVerification);
    }

    public OtpVerification verifyOtp(String email, String otp) {

        OtpVerification record = otpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (!record.getOtp().equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }

        if (record.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }

        return record;
    }


    public void resendOtp(String email) {

        OtpVerification record = otpRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("No signup request found"));

        String newOtp = generateOtp();

        record.setOtp(newOtp);
        record.setExpiryTime(LocalDateTime.now().plusMinutes(5));

        otpRepository.save(record);

        sendOtpEmail(email, newOtp);
    }

}
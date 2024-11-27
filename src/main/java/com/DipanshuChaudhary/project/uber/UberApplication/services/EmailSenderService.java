package com.DipanshuChaudhary.project.uber.UberApplication.services;

public interface EmailSenderService {

    void sendEmail(String toEmail, String subject, String body);

    void sendEmail(String[] toEmail, String subject, String body);

}

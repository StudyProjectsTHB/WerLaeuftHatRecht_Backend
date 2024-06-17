package com.example.mux.user.service;


import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base.url}")
    private String baseUrl;

    private static final String welcomeTemplate = "Hallo %s!\n" + "Um beim Wettbewerb teilzunehmen, tippe bitte auf folgenden Link und vergib ein Passwort: %s";

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setFrom("wer.laeuft.hat.recht@gmail.com");
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Async
    public void sendWelcomeEmails(List<User> users, List<UserToken> userTokens) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            UserToken token = userTokens.get(i);

            // TODO: Format Link as Link
            String message = String.format(welcomeTemplate, user.getCompetitionUserName(), generateLoginLink(token.getToken().toString()));
            sendEmail(user.getEmail(), "Anmeldung zum Schrittzählwettbewerb", message);
        }
    }

    public String generateLoginLink(String token) {
        // TODO: Path and baseURL (in application.properties
        return baseUrl + "/path/to/login/" + token;
    }

}

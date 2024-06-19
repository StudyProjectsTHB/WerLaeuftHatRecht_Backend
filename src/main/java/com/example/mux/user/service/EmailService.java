package com.example.mux.user.service;


import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.base.url}")
    private String baseUrl;

    private static final String welcomeTemplate = "Hallo %s! " + "<p>Um beim Wettbewerb teilzunehmen, tippe bitte auf folgenden Link und vergib ein Passwort: <a href=\"%s\">Passwort vergeben</a> oder tippe auf folgenden Link: %s</p>";


    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        MimeMessage mail = mailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mail, true, "UTF-8");
            messageHelper.setFrom("wer.laeuft.hat.recht@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(body, true);
            mailSender.send(mail);
        } catch (Exception ex) {
            System.out.println("Email not sent");
        }
    }

    @Async
    public void sendWelcomeEmails(List<User> users, List<UserToken> userTokens) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            UserToken token = userTokens.get(i);

            String message = String.format(welcomeTemplate, user.getCompetitionUserName(), generateLoginLink(token.getToken().toString()), generateLoginLink(token.getToken().toString()));
            sendEmail(user.getEmail(), "Anmeldung zum Schrittzählwettbewerb", message);
        }
    }

    public String generateLoginLink(String token) {
        // TODO: Path and baseURL (in application.properties
        return baseUrl + "/path/to/login/" + token;
    }

}

package com.example.mux.user.service;


import com.example.mux.challenge.model.dto.ChallengeDTO;
import com.example.mux.user.UserProperties;
import com.example.mux.user.model.User;
import com.example.mux.user.model.UserToken;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    private final UserProperties userProperties;

    private static final String welcomeTemplate = "Hallo %s! " + "<p>Um beim Wettbewerb teilzunehmen, tippe bitte auf folgenden Link und vergib ein Passwort: <a href=\"%s\">Passwort vergeben</a> oder tippe auf folgenden Link: %s</p>";
    private static final String passwordResetTemplate = "Hallo %s! " + "<p>Um dein Passwort zurückzusetzen, tippe bitte auf folgenden Link und vergib ein Passwort: <a href=\"%s\">Passwort vergeben</a> oder tippe auf folgenden Link: %s</p>";
    private static final String reminderTemplate = "Hallo %s! " + "<p>Uns ist aufgefallen, dass du seit mindestens %s Tagen keine Schritte mehr eingetragen hast. Wenn du willst kannst du das gleich unter diesem Link nachholen: <a href=\"%s\">Zur Webseite</a> oder tippe auf folgenden Link: %s</p>";
    private static final String challengeTemplate = "Hallo %s! " + "<p>%s, du hast in der letzten Woche %s der Herausforderungen absolviert.</p> <p>%s</p> <p>%s Hier kannst du gleich weitere Schritte eintragen: <a href=\"%s\">Zur Webseite</a> oder tippe auf folgenden Link: %s</p>";

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
            System.out.println("Email sent");
        } catch (Exception ex) {
            System.out.println("Email not sent");
        }
    }

    @Async
    public void sendWelcomeEmails(List<User> users, List<UserToken> userTokens) {
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            UserToken token = userTokens.get(i);
            String loginLink = userProperties.getFullRegistrationUrl(token.getToken().toString());
            String message = String.format(welcomeTemplate, user.getCompetitionUserName(), loginLink, loginLink);
            sendEmail(user.getEmail(), "Anmeldung zum Schrittzählwettbewerb", message);
        }
    }

    @Async
    public void sendPasswordResetEmail(User user, UserToken userToken){
        String passwordResetLink = userProperties.getFullResetPasswordUrl(userToken.getToken().toString());
        String message = String.format(passwordResetTemplate, user.getCompetitionUserName(), passwordResetLink, passwordResetLink);
        sendEmail(user.getEmail(), "Passwort zurücksetzen", message);
    }

    @Async
    public void sendReminderEmail(User user, int numberOfDays) {
        String message = String.format(reminderTemplate, user.getCompetitionUserName(), numberOfDays, userProperties.getBaseUrl(), userProperties.getBaseUrl());
        sendEmail(user.getEmail(), "Willst du Schritte eintragen?", message);
    }

    @Async
    public void sendChallengeEmail(User user, List<ChallengeDTO> challengeDTOs) {
        int successCount = 0;
        StringBuilder cString = new StringBuilder();
        for (ChallengeDTO cDTO : challengeDTOs) {
            cString.append(cDTO.getChallengeString()).append(" (").append(cDTO.getProgressString()).append(") <br>");
            if (cDTO.isCompleted()) {
                successCount++;
            }
        }

        String message = String.format(challengeTemplate,
                user.getCompetitionUserName(),
                (successCount >= 1) ? "Super" : "Schade",
                (successCount == 0) ? "keine" : successCount + " von " + challengeDTOs.size(),
                cString,
                (successCount == 0) ? "Diese Woche klappt es bestimmt!" : (successCount < challengeDTOs.size()) ? "Diese Woche schaffst du bestimmt noch mehr, viel Erfolg!" : "Schaffst du auch die Challenges dieser Woche?",
                userProperties.getBaseUrl(),userProperties.getBaseUrl());

        sendEmail(user.getEmail(), "Wöchentliche Herausforderungen", message);
    }

}

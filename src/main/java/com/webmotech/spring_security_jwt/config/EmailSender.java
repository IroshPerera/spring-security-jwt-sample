package com.webmotech.spring_security_jwt.config;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
public class EmailSender {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private  String email ;

    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

    }

    @Async
    public void sendHtmlMail( String subject, String userEmail) throws MessagingException, IOException {

        LocalTime localTime = LocalTime.now();

        // Define a formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        // Format the local time
        String formattedTime = localTime.format(formatter);

        System.out.println("Formatted Local Time: " + formattedTime);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.setFrom(new InternetAddress(email));
        message.setRecipients(MimeMessage.RecipientType.TO, email);

        String htmlTemplate = readFile("src/main/resources/email/email.html");
        htmlTemplate = htmlTemplate.replace("{loginTime}", formattedTime);
        htmlTemplate = htmlTemplate.replace("{userEmail}", userEmail);

        message.setContent(htmlTemplate, "text/html; charset=utf-8");
        message.setSubject(subject);

        javaMailSender.send(message);

    }

    public String readFile(String path) throws IOException {
        Path filePath = Paths.get(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }
}

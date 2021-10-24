package skarbnikApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class SpringMailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public SpringMailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMail(String sentTo, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Aplikacja ESkarbnik <eskarbnik@vp.pl>");
        message.setTo(sentTo);
        message.setSubject(subject);
        message.setText(text);
        try {
            javaMailSender.send(message);

        } catch(MailException e) {
            System.out.println("Nie można wysłać wiadomości z powodu błędu:" + e);
        }
    }
}

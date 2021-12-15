package skarbnikApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Configuration
public class SpringMailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public SpringMailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendMessage(String to, String subject, String message, boolean html) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setFrom("Aplikacja Eskarbnik <eskarbnik@vp.pl>");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(message, html);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RequestException("Nie można wysłać wiadomości z powodu błędu", e.getMessage());
        }
    }
    @Bean
    public SpringTemplateEngine springTemplateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(htmlTemplateResolver());
        return templateEngine;
    }
    @Bean
    public ITemplateResolver htmlTemplateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding(StandardCharsets.UTF_8.name());
        return templateResolver;
    }
    public void sendMailWithTemplate(String to, String subject, Map<String, Object> model) {
        Context thymeleafContext = new Context();
        thymeleafContext.setVariables(model);
        String body = springTemplateEngine().process("mail", thymeleafContext);
        sendMessage(to, subject, body, true);
    }

}

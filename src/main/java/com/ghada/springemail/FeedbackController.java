package com.ghada.springemail;

import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.ValidationException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    EmailConfig emailConfig;

    public FeedbackController(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
    }
    @PostMapping
    public void sendFeedback(@RequestBody Feedback feedback, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("invalid feedback");
        }
        //create a mail seeder
        JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfig.getHost());
        mailSender.setPort(this.emailConfig.getPort());
        mailSender.setUsername(this.emailConfig.getUsername());
        mailSender.setPassword(this.emailConfig.getPassword());

        // create a simple message
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom(feedback.getEmail());
        message.setTo("user@feedback.com");
        message.setSubject(feedback.getName());
        message.setText(feedback.getFeedback());



        //send message
        mailSender.send(message);
    }
}

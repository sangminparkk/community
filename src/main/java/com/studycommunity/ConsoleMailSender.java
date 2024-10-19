package com.studycommunity;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Slf4j
@Profile("local")
@Component
public class ConsoleMailSender implements JavaMailSender {


    @Override
    public MimeMessage createMimeMessage() {
        return null;
    }

    @Override
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        return null;
    }

    @Override
    public void send(MimeMessage... mimeMessages) throws MailException {

    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {

    }

    @Override
    public void send(MimeMessage mimeMessage) throws MailException {
        JavaMailSender.super.send(mimeMessage);
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        JavaMailSender.super.send(mimeMessagePreparator);
    }

    @Override
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        JavaMailSender.super.send(mimeMessagePreparators);
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        log.info(">>>> = {} ", simpleMessage.getText()); // 기능 구현 눈으로 확인 (습관화 필요)
        JavaMailSender.super.send(simpleMessage);
    }
}

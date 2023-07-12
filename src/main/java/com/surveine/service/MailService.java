package com.surveine.service;

import com.surveine.repository.EnqRepository;
import com.surveine.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender javaMailSender;
    private final EnqRepository enqRepository;

    public void reportSendEmail(Long enqId) throws MessagingException {
        String enqName = enqRepository.findById(enqId).get().getName();
        // 이메일 내용 및 링크 생성
        String subject = "샌드박스 " + enqName + " 신고 메일입니다.";
        String text = "샌드박스 " + enqName + "신고 누적횟수가 10회가 이상입니다 .\n\n"
             + "ENQUTE ID : " + enqId + " 입니다.";


        // 이메일 전송
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
        helper.setFrom("admin@serveine.com");
        helper.setTo("11ghyeonjin@gachon.ac.kr");
        helper.setSubject(subject);
        helper.setText(text, true);
        javaMailSender.send(message);
    }

}

package com.straysafe.backend.service;

import com.straysafe.backend.api.model.response.UserCredentialResponse;
import com.straysafe.backend.domain.PetDAOResponse;
import com.straysafe.backend.domain.ReportDAORequest;
import com.straysafe.backend.repository.BreedRepository;
import com.straysafe.backend.repository.ImageRepository;
import com.straysafe.backend.repository.PetRepository;
import com.straysafe.backend.repository.PetTypeRepository;
import com.straysafe.backend.util.enums.ReportType;
import com.straysafe.backend.util.templates.Templates;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;

import java.text.SimpleDateFormat;
import java.util.Date;


@Service
public class MailSenderService {
    @Value("${spring.mail.username}")
    private String FROM_EMAIL;
    private final JavaMailSender javaMailSender;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final ImageRepository imageRepository;
    private final ImageParserService imageParserService;
    private final BreedRepository breedRepository;
    private final PdfGeneratorService pdfGeneratorService;


    public MailSenderService(JavaMailSender mailSender,
                             PetRepository petRepository,
                             PetTypeRepository petTypeRepository,
                             ImageRepository imageRepository,
                             ImageParserService imageParserService,
                             BreedRepository breedRepository,
                             PdfGeneratorService pdfGeneratorService){
        this.javaMailSender = mailSender;
        this.petRepository = petRepository;
        this.petTypeRepository = petTypeRepository;
        this.imageRepository = imageRepository;
        this.breedRepository = breedRepository;
        this.imageParserService = imageParserService;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @Async
    public void sendEmail(String to, String subject, String body){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(FROM_EMAIL);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        javaMailSender.send(message);
    }



    public void sendMailWithAttachment(String toEmail,
                                       String body,
                                       String subject,
                                       byte[] attachment) throws MessagingException {
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper=new MimeMessageHelper(mimeMessage,true);
        mimeMessageHelper.setFrom(FROM_EMAIL);
        mimeMessageHelper.setTo(toEmail);
        mimeMessageHelper.setText(body);
        mimeMessageHelper.setSubject(subject);

        InputStreamSource attachmentSource = new ByteArrayResource(attachment);
        mimeMessageHelper.addAttachment("poster.pdf", attachmentSource);
        javaMailSender.send(mimeMessage);

    }

    @Async
    public void sendLostOrSeenEmail(ReportDAORequest reportDAORequest, UserCredentialResponse userData) {
        PetDAOResponse pet = petRepository.getPetById(reportDAORequest.petId());
        String image = imageParserService
                .convertPetImagePathToPetBase64(imageRepository.getImagePathById(reportDAORequest.petId()));

        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = dateFormat.format(currentDate);

        if (reportDAORequest.reportType() == ReportType.LOST) {
            try {
                sendMailWithAttachment(userData.getEmail(),

                        Templates.FOUND_AND_SEEN_EMAIL.formatted(
                                userData.getFirstName(),
                                userData.getLastName(),
                                reportDAORequest.reportType(),
                                reportDAORequest.reportId(),
                                formattedDate,
                                reportDAORequest.address()),

                        Templates.REPORT_SUBMISSION_SUBJECT.formatted(userData.getFirstName()),

                        pdfGeneratorService.generatePdfFromHtml(Templates.LOST_POSTER_HTML_TEMPLATE.formatted(
                                petTypeRepository.getTypeById(pet.petTypeId()),
                                image,
                                pet.petName(),
                                breedRepository.getBreedById(pet.breedId()),
                                formattedDate,
                                reportDAORequest.address(),
                                reportDAORequest.note(),
                                userData.getPhone()))
                );
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }else {
            sendEmail(userData.getEmail(),
                    Templates.REPORT_SUBMISSION_SUBJECT.formatted(userData.getFirstName()),
                    Templates.FOUND_AND_SEEN_EMAIL.formatted(
                            userData.getFirstName(),
                            userData.getLastName(),
                            reportDAORequest.reportType(),
                            reportDAORequest.reportId(),
                            formattedDate,
                            reportDAORequest.address()));
        }

    }
}
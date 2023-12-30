package com.clinital.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.clinital.models.Demande;
import com.clinital.models.User;

import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
@Transactional
@Service

public class EmailSenderService {

	@Autowired
	private JavaMailSender javaMailSender;

	private final Logger LOGGER=LoggerFactory.getLogger(getClass());

	@Async
	public void sendMail(String userEmail, String confirmationToken) {
		
		try{
SimpleMailMessage mailMessage = new SimpleMailMessage();
		final String BaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
			System.out.println("this is the URL Root :"+BaseUrl);
		System.out.println("this is the URL Root :"+userEmail);
		mailMessage.setTo(userEmail);
		mailMessage.setFrom("no-reply@clinital.io");
		mailMessage.setSubject("Activation du compte clinital!");
		mailMessage.setText("Bonjour nous vous souhaiton la bienvenue sur la plateforme Clinital pour confirmer votre compte"
				+ ", merci de cliquer sur le lien: "
				+ BaseUrl+"/api/auth/confirmaccount?token=" + confirmationToken
				+ "   Note: le lien va expirer après 10 minutes.");
		javaMailSender.send(mailMessage);
		LOGGER.info("A New Account has been Created, token activationis sent");

		}catch(Exception e){
			LOGGER.error("Error while sending email : {}",e);
			System.out.println(2);
		}
		
	}

	public void sendMailDemande(Demande demande,String pw ) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(demande.getMail());
		mailMessage.setFrom("no-reply@clinital.io");
		mailMessage.setSubject("Activation de la partie pro pour le médecin :"+demande.getNom_med());
		mailMessage.setText("Le Médecin :"+demande.getNom_med()+"veut accéder à la partie pro"
				+ "\n leurs cordonnées :  \n"
				+ "Medecin:\r\n"
				+ "Nom:"+demande.getNom_med()+"\n"
				+ "\r\n"
				+ "Prenom:"+demande.getPrenom_med()+"\n"
				+ "\r\n"
				+ "\r\n"
				//+ "passwaord provesoire:"+user.getPassword()+"\n"
				+ "Matricule:"+demande.getMatricule()+"\n"
				+ "\r\n"
				+ "Spécialité"+demande.getSpecialite()+"\n"
				+ "\r\n"
				+ "INPE:"+demande.getInpe()+"\n"
				+ "\r\n"
				+ "Cabinet : \r\n"
				+ "\r\n"
				+ "Nom: "+demande.getNom_cab()+"\n"
				+ "\r\n"
				+ "Adresse: "+demande.getAdresse()+"\n"
				+ "\r\n"
				+ "Code postale: "+demande.getCode_postal()
				+ "\r\n"
				+ "Password provisoire: "+pw);
				
		javaMailSender.send(mailMessage);
		LOGGER.info("A New Demande has been created ");
		System.out.println("Email sent");
	}
	public void sendMailDemandeconfirmation(Demande demande) {
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(demande.getMail());
		mailMessage.setFrom("no-reply@clinital.io");
		mailMessage.setSubject("Activation de la partie pro pour le médecin :"+demande.getNom_med());
		mailMessage.setText("Le Médecin :"+demande.getNom_med()+"veut accéder à la partie pro"
				+ "\n leurs cordonnées :  \n"
				+ "Medecin:\r\n"
				+ "Nom:"+demande.getNom_med()+"\n"
				+ "\r\n"
				+ "Prenom:"+demande.getPrenom_med()+"\n"
				+ "\r\n"
				+ "Matricule:"+demande.getMatricule()+"\n"
				+ "\r\n"
				+ "Spécialité"+demande.getSpecialite()+"\n"
				+ "\r\n"
				+ "INPE:"+demande.getInpe()+"\n"
				+ "\r\n"
				+ "Cabinet : \r\n"
				+ "\r\n"
				+ "Nom: "+demande.getNom_cab()+"\n"
				+ "\r\n"
				+ "Adresse: "+demande.getAdresse()+"\n"
				+ "\r\n"
				+ "Code postale: "+demande.getCode_postal());
				
		javaMailSender.send(mailMessage);
		LOGGER.info("A New Pro Account has been created ");
		System.out.println("Email sent");
	}
	public boolean sendSimpleMail(String to, String sub, String body) {
		//log.info(body);
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(to);
		mailMessage.setFrom("");
		mailMessage.setSubject(sub);
		mailMessage.setText(body);
		Boolean isSent = false;
		try {
			javaMailSender.send(mailMessage);
			isSent = true;
		} catch (Exception e) {
			//log.info(e.getMessage());
			e.printStackTrace();
		}

		return isSent;
	}
// send code access 

	public String sendMailCodeAccess(String userEmail, String code) throws Exception {
		try {
			
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		final String BaseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
		
		System.out.println("this is the URL Root :"+BaseUrl);
		mailMessage.setTo(userEmail);
		mailMessage.setFrom("no-reply@clinital.io");
		mailMessage.setSubject("Dossier Medcial clinital Access");
		mailMessage.setText("Bonjour nous vous souhaiton la bienvenue sur la plateforme Clinital :\n"
				+ "merci de Partager ce code avec votre Medecin: "
				+  code);
		javaMailSender.send(mailMessage);
		LOGGER.info("A New Code to Access ressource has been sent "+userEmail);
		return "Your request has been successfully  sent";
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
		throw new Exception(e.getMessage());
		
	}
	}

// get the BaseUrl:
	String getBaseUrl(HttpServletRequest req) {
		return ""
		  + req.getScheme() + "://"
		  + req.getServerName()
		  + ":" + req.getServerPort();
	  }
}
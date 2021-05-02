package pharmacy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pharmacy.model.entity.User;
import pharmacy.service.EmailMessage;
import pharmacy.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private Environment env;

	@Override
	public void sendEmail(EmailMessage msg) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(msg.getFrom());
		message.setTo(msg.getTo());
		message.setSubject(msg.getSubject());
		message.setText(msg.getBody());

		mailSender.send(message);

		System.out.println("Send email  - success");
	}

	@Async
	@Override
	public void sendToken(User user) throws MailException {
		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(user.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Confirm registration to pharmacy");
		mail.setText("http://localhost:8080/auth/verify?id=" + user.getId());
		mailSender.send(mail);
	}

}


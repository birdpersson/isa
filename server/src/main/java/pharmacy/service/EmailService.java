package pharmacy.service;

import pharmacy.model.entity.User;

public interface EmailService {

	void sendEmail(EmailMessage message);

	void sendToken(User user);

}

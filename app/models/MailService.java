package models;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import models.entities.Account;
import play.Logger;
import play.Play;

/**
 * Class for send mail for SendGrid
 * Created by shimizu on 2014/06/06.
 */
public class MailService {

	private static final String SMTP_USERNAME = Play.application().configuration().getString("sendgrid.user");
	private static final String SMTP_USERPASS = Play.application().configuration().getString("sendgrid.pass");

	/**
	 * 登録完了メール送信
	 *
	 * @param account 登録アカウント
	 * @return 処理結果 true 成功　false 失敗
	 */
	public static boolean sendRegisterdMail(Account account) {

		RegisterMail mail = new RegisterMail(account);

		if (SMTP_USERNAME == null || SMTP_USERPASS == null) {
			mail.debugLog();
			return true;
		}

		SendGrid sendgrid = new SendGrid(SMTP_USERNAME, SMTP_USERPASS);
		SendGrid.Email email = mail.getSendGridEmail();

		try {
			SendGrid.Response response = sendgrid.send(email);
			return response.getStatus();
		} catch (SendGridException e) {
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 登録完了メール送信
	 */
	public static class RegisterMail {
		private final String from;
		private final String to;
		private final String subject;
		private final String body;

		public RegisterMail(Account account) {
			to = account.getEmail();
			from = "noreplay@example.com";
			subject = "Regstration has been completed";
			body = "Welcome " + account.getName() + "!!!  Regstration has been completed";
		}

		public void debugLog() {
			Logger.info(">>>Email Send");
			Logger.info("from :\t" + from);
			Logger.info("to :\t" + to);
			Logger.info("Subject :\t" + subject);
			Logger.info("Body :\t" + body);
			Logger.info("Email>>>");
		}

		public SendGrid.Email getSendGridEmail() {
			SendGrid.Email email = new SendGrid.Email();
			email.addTo(to);
			email.setFrom(from);
			email.setSubject(subject);
			email.setText(body);
			return email;
		}
	}
}

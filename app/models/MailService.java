package models;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
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
	 * @param toAddress 送信先アドレス
	 * @return 処理結果 true 成功　false 失敗
	 */
	public static boolean sendRegisterdMail(String toAddress)  {

		SendGrid sendgrid = new SendGrid(SMTP_USERNAME, SMTP_USERPASS);
		SendGrid.Email email = new SendGrid.Email();
		email.addTo(toAddress);
		email.setFrom("noreplay@example.com");
		email.setSubject("登録完了のおしらせ");
		// TODO 引数をユーザオブジェクトに変えて名前ぐらい本文に載せる
		email.setText("登録が完了しました。");

		try {
			SendGrid.Response response = sendgrid.send(email);
			return response.getStatus();
		} catch (SendGridException e) {
			System.out.println(e);
			e.printStackTrace();
			return false;
		}
	}
}

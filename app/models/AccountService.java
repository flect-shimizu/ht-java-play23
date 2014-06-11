package models;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import play.db.jpa.JPA;
import play.libs.Crypto;

import models.entities.Account;

public class AccountService {

	public Optional<Account> login(String email, String password) {
		Optional<Account> ret;
		String encPassword = Crypto.encryptAES(password);
		TypedQuery<Account> query = JPA.em().createNamedQuery("findByEmailAndPass", Account.class);
		query.setParameter("email", email);
		query.setParameter("password", encPassword);
		Account account = null;
		try {
			account = query.getSingleResult();
		} catch (NoResultException e) {
		}
		return Optional.ofNullable(account);
	}

	public Account register(String email, String password, String url) throws EntityExistsException {
		Account account = new Account();
		account.setEmail(email);
		String encPassword = Crypto.encryptAES(password);
		account.setPassword(encPassword);
		account.setImage_url(url);
		account.setTimestamp(DateTime.now().toDate());
		JPA.em().persist(account);

		MailService.sendRegisterdMail(account);

		return account;
	}
}

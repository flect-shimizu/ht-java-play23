package models;

import java.util.Optional;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import play.db.jpa.JPA;
import play.libs.Crypto;

import models.entities.Account;

public class AccountService {

	public Optional<Account> login(String name, String password) {
		Optional<Account> ret;
		String encPassword = Crypto.encryptAES(password);
		TypedQuery<Account> query = JPA.em().createNamedQuery("findByNameAndPass", Account.class);
		query.setParameter("name", name);
		query.setParameter("password", encPassword);
		Account account = null;
		try {
			account = query.getSingleResult();
		} catch (NoResultException e) {
		}
		return Optional.ofNullable(account);
	}

	public void register(String name, String email, String password) throws EntityExistsException {
		Account account = new Account();
		account.setName(name);
		account.setEmail(email);
		String encPassword = Crypto.encryptAES(password);
		account.setPassword(encPassword);
		JPA.em().persist(account);

		MailService.sendRegisterdMail(account);
	}
}

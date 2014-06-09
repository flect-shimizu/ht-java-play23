package controllers;

import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityExistsException;

import models.MailService;
import play.db.jpa.Transactional;

import play.*;
import play.mvc.*;

import security.Auth;
import views.html.*;

import models.AccountService;

public class Account extends Controller {

	public static Result login() {
		return ok(views.html.Account.login.render(""));
	}

	public static Result logout() {
		Auth.clearUUID(session());
		return ok(views.html.Account.login.render(""));
	}

	@Transactional(readOnly=true)
	public static Result doLogin() {
		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String[] name = params.get("name");
		String[] password = params.get("password");
		AccountService as = new AccountService();
		Optional<models.entities.Account> account = as.login(name[0], password[0]);

		if (account.isPresent()) Auth.initUUID(session());

		return ok(index.render("message", account.orElse(new models.entities.Account())));
	}

	public static Result register() {
		return ok(views.html.Account.register.render(""));
	}

	@Transactional
	public static Result doRegister() {
		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String[] name = params.get("name");
		String[] email = params.get("email");
		String[] password = params.get("password");
		AccountService as = new AccountService();
		try {
			as.register(name[0], email[0], password[0]);
			return ok(views.html.Account.doRegister.render());
		} catch (EntityExistsException e) {
			return ok(views.html.Account.register.render("Name " + name[0] + " already exists."));
		}
	}
}

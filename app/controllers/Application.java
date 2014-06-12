package controllers;

import models.AccountService;
import models.CacheService;
import models.entities.Account;
import play.db.jpa.Transactional;
import play.mvc.*;

import security.Auth;
import utils.CloudinaryWrapper;
import views.html.*;

import javax.persistence.EntityExistsException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

public class Application extends Controller {

    public static Result index() {
	    String uuid = session(Auth.UUID_KEY);
	    Account account = (Account) CacheService.get(uuid);
	    if (uuid != null && account != null){
		    return redirect(routes.Application.inquiry());
	    }
	    Auth.clearUUID(session());
        return ok(index.render("Please login or entry into new."));
    }

	public static Result register() {
		Map<String,Object> cloudinary = CloudinaryWrapper.getSignature();
		return ok(entry.render("",cloudinary));
	}

	public static Result logout() {
		Auth.clearUUID(session());
		return ok(index.render("Logout Completed!!"));
	}

	@Security.Authenticated(Auth.class)
	public static Result inquiry() {
		String uuid = session(Auth.UUID_KEY);
		Account account = (Account) CacheService.get(uuid);
		return ok(inquiry.render("This is your entry.",account));
	}


	@Transactional(readOnly = true)
	public static Result doLogin() {
		Map<String, String[]> params = request().body().asFormUrlEncoded();
		String[] email = params.get("email");
		String[] password = params.get("password");
		AccountService as = new AccountService();
		Optional<models.entities.Account> account = as.login(email[0], password[0]);

		if (account.isPresent()){
			String uuid = Auth.initUUID(session());
			CacheService.set(uuid,account.get());
			return redirect(routes.Application.inquiry());
		} else {
			return ok(index.render("Not found Account"));
		}
	}

	@Transactional
	public static Result doRegister() {

		Map<String, String[]> params = request().body().asFormUrlEncoded();

		String[] email = params.get("email");
		String[] password = params.get("password");
		String[] url = params.get("url");

		AccountService as = new AccountService();

		try {
			Account account = as.register(email[0], password[0], url[0]);
			String uuid = Auth.initUUID(session());
			CacheService.set(uuid,account);
			return redirect(routes.Application.inquiry());

		} catch (EntityExistsException e) {
			Map<String,Object> cloudinary = CloudinaryWrapper.getSignature();
			return ok(views.html.entry.render("Name " + email[0] + " already exists.", cloudinary));
		}
	}
}

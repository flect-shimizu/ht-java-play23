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
		return ok(entry.render(""));
	}

	public static Result logout() {
		Auth.clearUUID(session());
		return ok(index.render("Logout Completed!!"));
	}

	@Security.Authenticated(Auth.class)
	public static Result inquiry() {
		String uuid = session(Auth.UUID_KEY);
		Account account = (Account) CacheService.get(uuid);
		return ok(inquiry.render("You are already entry.",account));
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

		Http.MultipartFormData multipartFormData = request().body().asMultipartFormData();
		Map<String, String[]> params = multipartFormData.asFormUrlEncoded();

		String[] email = params.get("email");
		String[] password = params.get("password");

		Http.MultipartFormData.FilePart picture = multipartFormData.getFile("file");

		AccountService as = new AccountService();
		try {
			String url = "";
			if (picture != null) {
				url = CloudinaryWrapper.uplaod(picture.getFile());
			}
			Account account = as.register(email[0], password[0], url);
			String uuid = Auth.initUUID(session());
			CacheService.set(uuid,account);
			return redirect(routes.Application.inquiry());

		} catch (EntityExistsException e) {
			return ok(views.html.entry.render("Name " + email[0] + " already exists."));
		} catch (IOException e) {
			return internalServerError();
		}
	}
}

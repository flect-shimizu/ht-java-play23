package controllers;

import play.*;
import play.mvc.*;

import security.Auth;
import views.html.*;

@Security.Authenticated(Auth.class)
public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Your new application is ready.",new models.entities.Account()));
    }

}

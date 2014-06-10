package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class FileUpload extends Controller {

	public static Result index() {
		return ok(views.html.FileUpload.index.render());
	}

	public static Result url() {
		// TODO
		return ok("hoge").as("text/plain");
	}
}

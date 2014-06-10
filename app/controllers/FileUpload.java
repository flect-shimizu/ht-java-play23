package controllers;

import java.util.Map;
import java.util.Optional;

import play.*;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;

import views.html.*;

import utils.S3Wrapper;

public class FileUpload extends Controller {

	public static Result index() {
		return ok(views.html.FileUpload.index.render());
	}

	public static Result url() {
		DynamicForm form = Form.form().bindFromRequest(request());
		String name = form.get("name");
		Optional<String> url = S3Wrapper.generatePresignedRequestUrl(name, "PUT");
		if(url.isPresent()) {
			return ok(url.get()).as("text/plain");
		}
		return internalServerError();
	}
}

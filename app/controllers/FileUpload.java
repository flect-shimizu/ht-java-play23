package controllers;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Optional;

import play.*;
import play.libs.Json;
import play.mvc.*;
import play.data.Form;
import play.data.DynamicForm;

import utils.CloudinaryWrapper;
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

	public static Result upload() {
		Http.MultipartFormData body = request().body().asMultipartFormData();
		Http.MultipartFormData.FilePart picture = body.getFile("picture");
		if (picture != null) {
			File file = picture.getFile();
			try {
				String url = CloudinaryWrapper.uplaod(file);
				return ok(Json.toJson(url));
			} catch (IOException e) {
				return internalServerError();
			}
		} else {
			return internalServerError();
		}
	}
}

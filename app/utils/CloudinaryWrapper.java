package utils;

import com.amazonaws.services.ec2.model.Route;
import com.cloudinary.*;
import controllers.Assets;
import play.Logger;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shimizu on 2014/06/10.
 */
public class CloudinaryWrapper {

	private static final String CLOUDINARY_URL = Play.application().configuration().getString("cloudinary_url", "");

	private static Cloudinary cloudinary;

	private static Cloudinary getSingleton(){
		if (cloudinary == null) {
			cloudinary = new Cloudinary();
		}
		return cloudinary;
	}

	public static String uplaod(File toUpload) throws IOException {
		if (CLOUDINARY_URL.isEmpty()){
			debugLog(toUpload);
			return "/assets/images/dummy.svg";
		}
		Map uploadResult = getSingleton().uploader().upload(toUpload, cloudinary.emptyMap());
		return String.valueOf(uploadResult.get("url"));
	}

	public static void debugLog(File file) {
		Logger.info(">>>File upload");
		Logger.info("FileName :\t" + file.getPath());
		Logger.info("File upload>>>");
	}

	public static Map<String,Object> getSignature() {
		String timestamp=(new Long(System.currentTimeMillis() / 1000L)).toString();
		Map<String, Object> params = new HashMap<String, Object>();
		Map options = Cloudinary.asMap("resource_type", "auto");
		boolean returnError = Cloudinary.asBoolean(options.get("return_error"), false);
		String apiKey = Cloudinary.asString(options.get("api_key"), getSingleton().getStringConfig("api_key"));
		if (apiKey == null)
			throw new IllegalArgumentException("Must supply api_key");
		String apiSecret = Cloudinary.asString(options.get("api_secret"), getSingleton().getStringConfig("api_secret"));
		if (apiSecret == null)
			throw new IllegalArgumentException("Must supply api_secret");
		params.put("timestamp", timestamp);
		String expected_signature = getSingleton().apiSignRequest(params, apiSecret);
		params.put("signature", expected_signature);
		params.put("api_key", apiKey);
		return params;
	}
}

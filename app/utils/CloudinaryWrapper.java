package utils;

import com.amazonaws.services.ec2.model.Route;
import com.cloudinary.*;
import controllers.Assets;
import play.Logger;
import play.Play;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by shimizu on 2014/06/10.
 */
public class CloudinaryWrapper {

	private static final String CLOUDINARY_URL = Play.application().configuration().getString("CLOUDINARY_URL", "");

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
}

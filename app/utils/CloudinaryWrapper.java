package utils;

import com.cloudinary.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created by shimizu on 2014/06/10.
 */
public class CloudinaryWrapper {

	public static String uplaod(File toUpload) throws IOException {
		Map config = Cloudinary.asMap(
				"cloud_name", "hcihq7hbe",
				"api_key", "916853881888261",
				"api_secret", "8fbbQ4Xi3S2br_c_xwy7OUQiIfw");
		Cloudinary cloudinary = new Cloudinary(config);
		Map uploadResult = cloudinary.uploader().upload(toUpload, config);
		return String.valueOf(uploadResult.get("url"));
	}

}

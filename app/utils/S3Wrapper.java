package utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

import play.Play;

public class S3Wrapper {

	private static final String BUCKET_NAME = Play.application().configuration().getString("aws.s3.bucketname");
	private static final int EXPIRED = Play.application().configuration().getInt("aws.s3.upload.expires");
	private static final String ACCESSKEY = Play.application().configuration().getString("aws.s3.upload.accesskey");
	private static final String SECRETKEY = Play.application().configuration().getString("aws.s3.upload.secretkey");

	public static Optional<String> generatePresignedRequestUrl(String path, String method) {
		ZonedDateTime expired = ZonedDateTime.now().plusSeconds((long)EXPIRED);
		String url = generatePresignedRequestUrl(BUCKET_NAME, Date.from(expired.toInstant()), path, method);
		try {
			return Optional.of(URLEncoder.encode(url, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			return Optional.empty();
		}
	}

	public static String generatePresignedRequestUrl(String bucketName, Date expired, String path, String method) {
		AmazonS3Client client = makeS3Client();
		return client.generatePresignedUrl(bucketName, path, expired, HttpMethod.valueOf(method)).toString();
	}

	public static AmazonS3Client makeS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(ACCESSKEY, SECRETKEY);
		return new AmazonS3Client(credentials);
	}
}

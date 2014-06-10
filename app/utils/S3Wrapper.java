package utils;

import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import play.Play;

public class S3Wrapper {

	private static final String BUCKET_NAME = Play.application().configuration().getString("aws.s3.bucketname");
	private static final int EXPIRED = Play.application().configuration().getInt("aws.s3.upload.expires");
	private static final String ACCESSKEY = Play.application().configuration().getString("aws.s3.upload.accesskey");
	private static final String SECRETKEY = Play.application().configuration().getString("aws.s3.upload.secretkey");

	public static Optional<String> generatePresignedRequestUrl(String path, String method, String type) {
		ZonedDateTime expired = ZonedDateTime.now().plusSeconds((long)EXPIRED);
		return generatePresignedRequestUrl(BUCKET_NAME, Date.from(expired.toInstant()), path, method, type);
	}

	public static Optional<String> generatePresignedRequestUrl(String bucketName, Date expired, String path, String method, String type) {
		AmazonS3Client client = makeS3Client();
		GeneratePresignedUrlRequest gpur = new GeneratePresignedUrlRequest(bucketName, path);
		gpur.setMethod(HttpMethod.valueOf(method));
		gpur.setExpiration(expired);
		gpur.setContentType(type);
		gpur.addRequestParameter("x-amz-acl", "public-read");
		String url = client.generatePresignedUrl(gpur).toString();
		System.out.println("url = " + url);
		return Optional.of(url);
	}

	public static AmazonS3Client makeS3Client() {
		AWSCredentials credentials = new BasicAWSCredentials(ACCESSKEY, SECRETKEY);
		return new AmazonS3Client(credentials);
	}
}

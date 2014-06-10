package utils;

import java.util.Date;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;

public class S3Wrapper {

	public static String generatePresignedRequestUrl(String path, String method) {
		// TODO String bucketname, Date expired
		String bucketName = "";
		Date expired = new Date();
		return generatePresignedRequestUrl(bucketName, expired, path, method);
	}

	public static String generatePresignedRequestUrl(String bucketName, Date expired, String path, String method) {
		AmazonS3Client client = makeS3Client();
		return client.generatePresignedUrl(bucketName, path, expired, HttpMethod.valueOf(method)).toString();
	}

	public static AmazonS3Client makeS3Client() {
		// TODO accessKey, accessSecretKey
		String accessKey = "";
		String accessSecretKey = "";
		AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecretKey);
		return new AmazonS3Client(credentials);
	}
}

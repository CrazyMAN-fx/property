package file_storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import play.Application;
import play.Logger;
import play.Plugin;

/**
 * Created by PENTAGON on 15.08.14.
 */
public class S3Plugin extends Plugin {

    private static final String AWS_S3_IMAGES_BUCKET = "aws.s3.images.bucket";
    private static final String AWS_S3_PANO_BUCKET = "aws.s3.pano.bucket";
    private static final String AWS_ACCESS_KEY = "aws.access.key";
    private static final String AWS_SECRET_KEY = "aws.secret.key";
    private final Application application;

    public static AmazonS3 amazonS3;

    public static String imagesBucket;
    public static String panoBucket;

    public S3Plugin(Application application) {
        this.application = application;
    }

    @Override
    public void onStart() {
        String accessKey = application.configuration().getString(AWS_ACCESS_KEY);
        String secretKey = application.configuration().getString(AWS_SECRET_KEY);
        imagesBucket = application.configuration().getString(AWS_S3_IMAGES_BUCKET);
        panoBucket = application.configuration().getString(AWS_S3_PANO_BUCKET);

        if ((accessKey != null) && (secretKey != null)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            amazonS3 = new AmazonS3Client(awsCredentials);
            Logger.info("Amazon s3 client created");
        }
    }

    @Override
    public boolean enabled() {
        return (application.configuration().keys().contains(AWS_ACCESS_KEY) &&
                application.configuration().keys().contains(AWS_SECRET_KEY) &&
                application.configuration().keys().contains(AWS_S3_IMAGES_BUCKET) &&
                application.configuration().keys().contains(AWS_S3_PANO_BUCKET));
    }

}
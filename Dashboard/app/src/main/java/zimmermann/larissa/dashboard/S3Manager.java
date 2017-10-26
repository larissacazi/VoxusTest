package zimmermann.larissa.dashboard;

import android.os.AsyncTask;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.auth.*;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

/**
 * Created by laris on 24/10/2017.
 */

public class S3Manager {
    private static final String ACCESS_KEY_ID = "AKIAI4N7XQLSCQZK2X6Q";
    private static final String SECRET_KEY_ID = "+anp5ChmsPHZvg8nhXi2RqXq27lVLg2+aq0mKPv/";
    private static final String SUFFIX = "/";

    private static String bucketName    = "voxus-dashboard-teste";
    private static String folderName    = "testFolder";
    private static String keyName       = "sample";

    private BasicAWSCredentials awsCreds;
    private AmazonS3 s3Client;

    public S3Manager() {
        this.awsCreds = new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY_ID);
        this.s3Client = new AmazonS3Client(this.awsCreds);
    }

    public void createBucket() {
        this.s3Client.createBucket(bucketName);
    }

    public void createFolder() {
        // create meta-data for your folder and set content-length to 0
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(0);
        // create empty content
        InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
        // create a PutObjectRequest passing the folder name suffixed by /
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName + SUFFIX, emptyContent, metadata);
        // send request to S3 to create folder
        this.s3Client.putObject(putObjectRequest);
    }

    public void uploadData(String filePath) {
        String fileName = folderName + SUFFIX + "testvideo.mp4";
        this.s3Client.putObject(new PutObjectRequest(bucketName, fileName, new File(filePath)).withCannedAcl(CannedAccessControlList.PublicRead));
    }
}

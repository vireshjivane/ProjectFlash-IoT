package com.sjsu.flash.s3;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.apache.commons.io.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Properties;

public class S3FileUploader {

    void updateThresholds(Integer cpu, Integer memeory) {

        String accessKey = "xxxxxx";
        String secretKey = "xxxxxxx";
        String bucketName = "flash-properties";
        String keyName = "flash.config";

        try {
            AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
            Properties properties = new Properties();
            properties
                    .load(new StringReader(IOUtils.toString(client.getObject(new GetObjectRequest(bucketName, keyName))
                            .getObjectContent(), "UTF-8")));
            properties.setProperty("cpu.max.threshold", cpu.toString());
            properties.setProperty("memory.max.threshold", memeory.toString());
            File file = new File(keyName);
            FileOutputStream fr = new FileOutputStream(file);
            properties.store(fr, null);
            fr.close();
            client.putObject(new PutObjectRequest(bucketName, keyName, file));
        } catch (Exception e) {e.printStackTrace();}
    }
}

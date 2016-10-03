package com.sjsu.flash.s3;

/**
 * Created by vjivane on 4/21/16.
 */
public class S3FileUploaderTest {

    public static void main(String[] args) {
        S3FileUploader s3 = new S3FileUploader();
        s3.updateThresholds(555,777);
    }

}

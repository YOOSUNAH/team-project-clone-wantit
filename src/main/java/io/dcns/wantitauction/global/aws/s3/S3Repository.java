package io.dcns.wantitauction.global.aws.s3;

import static java.lang.System.exit;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import io.dcns.wantitauction.global.utils.MultiPartUtils;

import java.io.File;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Repository {
    private final AmazonS3 amazonS3;
    private final String bucketName = "wantit-bucket";

    public String store(String fullPath,
                        MultipartFile file) {
        File localFile = new File(MultiPartUtils.getLocalHomeDirectory(), fullPath);

        try {
            file.transferTo(localFile);
            amazonS3.putObject(new PutObjectRequest(bucketName, fullPath, localFile)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Failed to store file");
        } finally {
            if (localFile.exists()) {
                removeNewFile(localFile);
            }
        }
        return amazonS3.getUrl(bucketName, fullPath).toString();
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File deleted successfully");
        } else {
            log.info("Failed to delete file");
        }
    }

    public void delete(String fileUrl) {
        try {
            String key = fileUrl.replace("https://wantit-bucket.s3.ap-northeast-2.amazonaws.com/",
                    "");
            try {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
            } catch (AmazonServiceException e) {
                log.error(e.getMessage());
                exit(1);
            }
            log.info(String.format("File deleted successfully: %s", key));
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalArgumentException("Failed to delete file");
        }
    }
}


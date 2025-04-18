package io.dcns.wantitauction.global.aws.s3;

import com.amazonaws.SdkBaseException;
import io.dcns.wantitauction.global.utils.MultiPartUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Repository s3Repository;

    public String uploadFile(MultipartFile file) {
        verifiedFile(file);
        String fullPath = MultiPartUtils.createPath(file);
        return s3Repository.store(fullPath, file);
    }

    public void delete(String fileUrl) throws SdkBaseException {
        s3Repository.delete(fileUrl);
    }

    private void verifiedFile(MultipartFile file) throws IllegalArgumentException {
        String contentType = file.getContentType();
        assert contentType != null;

        if (ObjectUtils.isEmpty(contentType) |
                (!contentType.contains("image/jpeg") && !contentType.contains("image/png"))) {
            throw new IllegalArgumentException("File is empty");
        }
    }
}

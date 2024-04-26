package io.dcns.wantitauction.global.utils;

import java.util.UUID;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class MultiPartUtils {

    private static final String BASE_DIR = "images";

    public static String getLocalHomeDirectory() {
        return System.getProperty("user.home");
    }

    public static String createFieldId() {
        return UUID.randomUUID().toString();
    }

    public static String getFormat(String contentType) {
        if (StringUtils.hasText(contentType)) {
            return contentType.split("/")[1];
        }
        return null;
    }

    public static String createPath(MultipartFile file) {
        final String fieldId = MultiPartUtils.createFieldId();
        final String format = MultiPartUtils.getFormat(file.getContentType());

        return String.format("%s/%s.%s", BASE_DIR, fieldId, format);
    }

}

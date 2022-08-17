package tech.theraven.cloudrender.util;

import org.apache.commons.io.FilenameUtils;

public class UrlUtils {

    private static final String STORAGE_URL = "https://storage.googleapis.com/";
    private static final String BUCKET = "cloud_render/";

    private static final String UPLOAD_FOLDER = "result/";

    public static String generateFileUrl(String fileName, String email) {
        return createAbsolutePath(email + "/" +fileName);
    }

    public static String makeRelative(String fileUrl) {
        return fileUrl.replace(STORAGE_URL + BUCKET, "");
    }

    public static String makeAbsolute(String relativeFileUrl) {
        return STORAGE_URL + BUCKET + relativeFileUrl;
    }

    public static String getUploadFolderPath(String fileUrl) {
        return fileUrl.replace(getFileName(fileUrl), UPLOAD_FOLDER + getFileName(fileUrl));
    }

    public static String getFileName(String fileUrl) {
        return FilenameUtils.getBaseName(fileUrl);
    }

    public static String getFileBaseName(String fileUrl) {
        return FilenameUtils.getBaseName(fileUrl).replace(getFileExtension(fileUrl), "");
    }

    public static String getFileExtension(String fileUrl) {
        return "." + FilenameUtils.getExtension(fileUrl);
    }

    public static String getFramePath(String fileUrl, long frame, String resultFileExtension) {
        var frameUrl = fileUrl.replace(getFileExtension(fileUrl), String.format("_%04d", frame) + resultFileExtension);
        return getUploadFolderPath(frameUrl);
    }

    public static String getBucketName() {
        return BUCKET;
    }

    private static String createAbsolutePath(String fileName) {
        return STORAGE_URL + BUCKET + fileName;
    }
}

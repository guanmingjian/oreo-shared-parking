package cn.oreo.common.core.exception;

/**
 * 文件下载异常
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class FileDownloadException extends Exception {
    private static final long serialVersionUID = -4353976687870027960L;

    public FileDownloadException(String message) {
        super(message);
    }
}

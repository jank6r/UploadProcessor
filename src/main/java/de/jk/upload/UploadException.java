package de.jk.upload;

public class UploadException extends RuntimeException {

    private static final long serialVersionUID = -2664824308356956776L;

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }

}

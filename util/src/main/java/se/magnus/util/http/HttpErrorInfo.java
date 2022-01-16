package se.magnus.util.http;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Getter
public class HttpErrorInfo {

    private final ZonedDateTime timestamp = ZonedDateTime.now();
    private final HttpStatus httpStatus;
    private final String path;
    private final String message;

    public HttpErrorInfo() {
        this.httpStatus = HttpStatus.PROCESSING;
        this.path = null;
        this.message = null;
    }

    public int getStatus() {
        return httpStatus.value();
    }

    public String getError() {
        return httpStatus.getReasonPhrase();
    }

}

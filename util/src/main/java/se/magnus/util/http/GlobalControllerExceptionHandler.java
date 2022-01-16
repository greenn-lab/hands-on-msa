package se.magnus.util.http;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import se.magnus.util.exceptions.InvalidInputException;
import se.magnus.util.exceptions.NotFoundException;

@RestControllerAdvice
@Slf4j
class GlobalControllerExceptionHandler {

  @ResponseStatus(NOT_FOUND)
  @ExceptionHandler(NotFoundException.class)
  @ResponseBody
  public HttpErrorInfo handleNotFoundExceptions(ServerHttpRequest request, Exception ex) {
    return createHttpErrorInfo(NOT_FOUND, request, ex);
  }

  @ResponseStatus(UNPROCESSABLE_ENTITY)
  @ExceptionHandler(InvalidInputException.class)
  @ResponseBody
  public HttpErrorInfo handleInvalidInputException(ServerHttpRequest request, Exception ex) {
    return createHttpErrorInfo(UNPROCESSABLE_ENTITY, request, ex);
  }

  private HttpErrorInfo createHttpErrorInfo(HttpStatus httpStatus, ServerHttpRequest request,
      Exception ex) {
    final String path = request.getPath().pathWithinApplication().value();
    final String message = ex.getMessage();

    logger.debug("Returning HTTP status: {} for path: {}, message: {}", httpStatus, path, message);
    return new HttpErrorInfo(httpStatus, path, message);
  }

}

package de.muenchen.isi.api.advice;

import de.muenchen.isi.api.dto.enums.InformationResponseType;
import de.muenchen.isi.api.dto.error.InformationResponseDto;
import de.muenchen.isi.domain.exception.AbfrageStatusNotAllowedException;
import de.muenchen.isi.domain.exception.CsvAttributeErrorException;
import de.muenchen.isi.domain.exception.EntityIsReferencedException;
import de.muenchen.isi.domain.exception.EntityNotFoundException;
import de.muenchen.isi.domain.exception.FileHandlingFailedException;
import de.muenchen.isi.domain.exception.FileHandlingWithS3FailedException;
import de.muenchen.isi.domain.exception.FileImportFailedException;
import de.muenchen.isi.domain.exception.KoordinatenException;
import de.muenchen.isi.domain.exception.MimeTypeExtractionFailedException;
import de.muenchen.isi.domain.exception.MimeTypeNotAllowedException;
import de.muenchen.isi.domain.exception.OptimisticLockingException;
import de.muenchen.isi.domain.exception.UniqueViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Zur Behandlung der in den Controller geworfenen Exceptions.
 * Erforderlich um ein einheitliches {@link InformationResponseDto} an das Frontend zurückzugeben.
 * <p>
 * Die Methoden annotiert mit {@link Override} überschreiben alle handleXXX-Methoden des {@link ResponseEntityExceptionHandler}s,
 * um bei jeder Spring-Exception einen einheitlichen Response-Body vom Typ {@link InformationResponseDto} zu gewährleisten.
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int CUSTOM_INTERNAL_SERVER_ERROR = 555;

    private final Tracer tracer;

    @ExceptionHandler(OptimisticLockingException.class)
    public ResponseEntity<Object> handleOptimisticLockingException(final OptimisticLockingException ex) {
        final var httpStatus = HttpStatus.PRECONDITION_FAILED;
        final InformationResponseDto errorResponseDto = new InformationResponseDto();
        errorResponseDto.setMessages(List.of(ex.getMessage()));
        errorResponseDto.setHttpStatus(httpStatus.value());
        errorResponseDto.setType(InformationResponseType.ERROR);
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException ex) {
        final var httpStatus = HttpStatus.BAD_REQUEST;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(EntityIsReferencedException.class)
    public ResponseEntity<Object> handleEntityIsReferencedException(final EntityIsReferencedException ex) {
        final var httpStatus = HttpStatus.CONFLICT;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(CsvAttributeErrorException.class)
    public ResponseEntity<Object> handleCsvAttributeErrorException(final CsvAttributeErrorException ex) {
        final var httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(FileImportFailedException.class)
    public ResponseEntity<Object> handleFileImportFailedException(final FileImportFailedException ex) {
        final var httpStatus = CUSTOM_INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus,
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(MimeTypeNotAllowedException.class)
    public ResponseEntity<Object> handleMimeTypeNotAllowedException(final MimeTypeNotAllowedException ex) {
        final var httpStatus = HttpStatus.NOT_ACCEPTABLE;
        final InformationResponseDto errorResponseDto = new InformationResponseDto();
        errorResponseDto.setMessages(List.of(ex.getMessage()));
        errorResponseDto.setHttpStatus(httpStatus.value());
        errorResponseDto.setType(InformationResponseType.ERROR);
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(MimeTypeExtractionFailedException.class)
    public ResponseEntity<Object> handleMimeTypeExtractionFailedException(final MimeTypeExtractionFailedException ex) {
        final var httpStatus = CUSTOM_INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus,
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(FileHandlingFailedException.class)
    public ResponseEntity<Object> handleFileHandlingFailedException(final FileHandlingFailedException ex) {
        final var httpStatus = CUSTOM_INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus,
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(FileHandlingWithS3FailedException.class)
    public ResponseEntity<Object> handleFileHandlingWithS3FailedException(final FileHandlingWithS3FailedException ex) {
        // 404/409 falls im S3-Storage ein gesuchtes Dokument nicht vorhanden ist oder beim initialen Speichern bereits existiert.
        // Ansonsten 555.
        final var exceptionStatus = ex.getStatusCode();
        final var httpStatus = exceptionStatus == HttpStatus.NOT_FOUND || exceptionStatus == HttpStatus.CONFLICT
                ? exceptionStatus.value()
                : CUSTOM_INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus,
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(KoordinatenException.class)
    public ResponseEntity<Object> handleKoordinatenException(final KoordinatenException ex) {
        final var httpStatus = CUSTOM_INTERNAL_SERVER_ERROR;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus,
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(AbfrageStatusNotAllowedException.class)
    public ResponseEntity<Object> handleAbfrageStatusNotAllowedException(final AbfrageStatusNotAllowedException ex) {
        final var httpStatus = HttpStatus.CONFLICT;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(final EntityNotFoundException ex) {
        final var httpStatus = HttpStatus.NOT_FOUND;
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(
                ex,
                httpStatus.value(),
                List.of(ex.getMessage())
        );
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    @ExceptionHandler(UniqueViolationException.class)
    public ResponseEntity<Object> handleUniqueViolationException(final UniqueViolationException ex) {
        final var httpStatus = HttpStatus.CONFLICT;
        final InformationResponseDto errorResponseDto = new InformationResponseDto();
        errorResponseDto.setMessages(List.of(ex.getMessage()));
        errorResponseDto.setHttpStatus(httpStatus.value());
        errorResponseDto.setType(InformationResponseType.ERROR);
        return ResponseEntity
                .status(httpStatus)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(final HttpRequestMethodNotSupportedException ex,
                                                                         final HttpHeaders headers,
                                                                         final HttpStatus status,
                                                                         final WebRequest request) {
        super.handleHttpRequestMethodNotSupported(ex, headers, status, request);
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Die HTTP-Methode " + ex.getMethod() + " wird nicht unterstützt."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException ex,
                                                                     final HttpHeaders headers,
                                                                     final HttpStatus status,
                                                                     final WebRequest request) {
        super.handleHttpMediaTypeNotSupported(ex, headers, status, request);
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Content-Type " + ex.getContentType() + " wird nicht unterstützt."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(final HttpMediaTypeNotAcceptableException ex,
                                                                      final HttpHeaders headers,
                                                                      final HttpStatus status,
                                                                      final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Media-Type des Requests wird nicht unterstützt."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }


    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(final MissingPathVariableException ex,
                                                               final HttpHeaders headers,
                                                               final HttpStatus status,
                                                               final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Pfadvariable " + ex.getVariableName() + " ist nicht gesetzt."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(final MissingServletRequestParameterException ex,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatus status,
                                                                          final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Requestparameter " + ex.getParameterName() + " ist nicht gesetzt."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(final ServletRequestBindingException ex,
                                                                          final HttpHeaders headers,
                                                                          final HttpStatus status,
                                                                          final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Fehler aufgetreten."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(final ConversionNotSupportedException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Fehler aufgetreten."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(final TypeMismatchException ex,
                                                        final HttpHeaders headers,
                                                        final HttpStatus status,
                                                        final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Das Attribut " + ex.getPropertyName() + " besitzt nicht den korrekten Datentyp."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Der Nutzlast der Anfrage an das Backend konnte nicht verarbeitet werden."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(final HttpMessageNotWritableException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Die Nutzlast des Antwort vom Backend konnte nicht verarbeitet werden."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                  final HttpHeaders headers,
                                                                  final HttpStatus status,
                                                                  final WebRequest request) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            final FieldError fieldError = (FieldError) error;
            final String fieldName = fieldError.getField();
            final String errorMessage = fieldError.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        final List<String> errorMessages = errors.entrySet().stream()
                .map(errorEntry -> "Attribut " + errorEntry.getKey() + ": " + StringUtils.capitalize(errorEntry.getValue()))
                .collect(Collectors.toList());

        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(errorMessages);
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(final MissingServletRequestPartException ex,
                                                                     final HttpHeaders headers,
                                                                     final HttpStatus status,
                                                                     final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Beim Hochladen der Datei " + ex.getRequestPartName() + " ist ein Fehler aufgetreten."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex,
                                                         final HttpHeaders headers,
                                                         final HttpStatus status,
                                                         final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Fehler aufgetreten."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(final NoHandlerFoundException ex,
                                                                   final HttpHeaders headers,
                                                                   final HttpStatus status,
                                                                   final WebRequest request) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Die URL " + ex.getRequestURL() + " konnte nicht mit der HTTP-Methode " + ex.getHttpMethod() + " aufgerufen werden."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * Überschreibt die Methode im {@link ResponseEntityExceptionHandler},
     * um ein einheitliches {@link InformationResponseDto} zurückzugeben.
     *
     * @param ex         the exception
     * @param headers    the headers to be written to the response
     * @param status     the selected response status
     * @param webRequest the current request
     * @return das {@link InformationResponseDto}.
     */
    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(final AsyncRequestTimeoutException ex,
                                                                        final HttpHeaders headers,
                                                                        final HttpStatus status,
                                                                        final WebRequest webRequest) {
        super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest);
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(status.value());
        errorResponseDto.setMessages(List.of("Im Backend ist ein Timeout aufgetreten."));
        return ResponseEntity
                .status(errorResponseDto.getHttpStatus())
                .headers(headers)
                .body(errorResponseDto);
    }

    /**
     * @param httpStatus Falls es sich um den Status 500 wird dieser durch den
     *                   Status {@link RestExceptionHandler#CUSTOM_INTERNAL_SERVER_ERROR} ersetzt.
     */
    protected InformationResponseDto createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionNameAndStatusAndMessage(final Exception ex,
                                                                                                                                             final int httpStatus,
                                                                                                                                             final List<String> messages) {
        final var errorResponseDto = this.createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(ex);
        errorResponseDto.setHttpStatus(httpStatus == HttpStatus.INTERNAL_SERVER_ERROR.value() ? CUSTOM_INTERNAL_SERVER_ERROR : httpStatus);
        errorResponseDto.setMessages(messages);
        return errorResponseDto;
    }

    protected InformationResponseDto createInformationResponseDtoWithTraceInformationAndTimestampAndOriginalExceptionName(final Exception ex) {
        final var span = this.tracer.currentSpan();
        final var errorResponseDto = new InformationResponseDto();
        errorResponseDto.setType(InformationResponseType.ERROR);
        errorResponseDto.setTimestamp(LocalDateTime.now());
        errorResponseDto.setOriginalException(ex.getClass().getSimpleName());
        if (ObjectUtils.isNotEmpty(span)) {
            errorResponseDto.setTraceId(span.context().traceId());
            errorResponseDto.setSpanId(span.context().spanId());
        }
        return errorResponseDto;
    }

}

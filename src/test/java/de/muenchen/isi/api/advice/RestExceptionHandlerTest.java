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
import de.muenchen.isi.domain.exception.UniqueViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.TraceContext;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RestExceptionHandlerTest {

    @Mock
    private Tracer tracer;

    @Mock
    private Span span;

    @Mock
    private TraceContext traceContext;

    @Mock
    private BindingResult bindingResult;

    private RestExceptionHandler restExceptionHandler;

    @BeforeEach
    public void beforeEach() {
        this.restExceptionHandler = new RestExceptionHandler(this.tracer);
        Mockito.reset(this.tracer, this.span, this.traceContext, this.bindingResult);
        Mockito.when(this.tracer.currentSpan()).thenReturn(this.span);
        Mockito.when(this.span.context()).thenReturn(this.traceContext);
        Mockito.when(this.traceContext.spanId()).thenReturn("ffffffffffffffff");
        Mockito.when(this.traceContext.traceId()).thenReturn("1111111111111111");
    }

    @Test
    void handleEntityIsReferencedException() {

        final EntityIsReferencedException entityIsReferencedException = new EntityIsReferencedException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleEntityIsReferencedException(entityIsReferencedException);

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.CONFLICT)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("EntityIsReferencedException")
        );

    }

    @Test
    void handleCsvAttributeErrorException() {
        final CsvAttributeErrorException csvAttributeErrorException = new CsvAttributeErrorException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleCsvAttributeErrorException(csvAttributeErrorException);

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.UNPROCESSABLE_ENTITY)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("CsvAttributeErrorException")
        );
    }

    @Test
    void testhandleFileImportFailedException() {
        final FileImportFailedException fileImportFailedException = new FileImportFailedException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleFileImportFailedException(fileImportFailedException);

        assertThat(
                response.getStatusCodeValue(),
                is(555)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("FileImportFailedException")
        );
    }

    @Test
    void handleFileHandlingFailedException() {
        final FileHandlingFailedException fileImportFailedException = new FileHandlingFailedException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleFileHandlingFailedException(fileImportFailedException);

        assertThat(
                response.getStatusCodeValue(),
                is(555)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("FileHandlingFailedException")
        );
    }

    @Test
    void handleFileHandlingWithS3FailedException() {
        FileHandlingWithS3FailedException fileHandlingWithS3FailedException = new FileHandlingWithS3FailedException("test", HttpStatus.NOT_FOUND);
        ResponseEntity<Object> response = this.restExceptionHandler.handleFileHandlingWithS3FailedException(fileHandlingWithS3FailedException);

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_FOUND)
        );

        InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("FileHandlingWithS3FailedException")
        );


        fileHandlingWithS3FailedException = new FileHandlingWithS3FailedException("test", HttpStatus.CONFLICT);
        response = this.restExceptionHandler.handleFileHandlingWithS3FailedException(fileHandlingWithS3FailedException);

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.CONFLICT)
        );

        responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("FileHandlingWithS3FailedException")
        );


        fileHandlingWithS3FailedException = new FileHandlingWithS3FailedException("test", HttpStatus.NOT_ACCEPTABLE);
        response = this.restExceptionHandler.handleFileHandlingWithS3FailedException(fileHandlingWithS3FailedException);

        assertThat(
                response.getStatusCodeValue(),
                is(555)
        );

        responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("FileHandlingWithS3FailedException")
        );
    }

    @Test
    void handleKoordinatenException() {
        final KoordinatenException koordinatenException = new KoordinatenException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleKoordinatenException(koordinatenException);

        assertThat(
                response.getStatusCodeValue(),
                is(555)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("KoordinatenException")
        );
    }

    @Test
    void handleAbfrageStatusNotAllowedException() {
        final AbfrageStatusNotAllowedException abfrageStatusNotAllowedException = new AbfrageStatusNotAllowedException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleAbfrageStatusNotAllowedException(abfrageStatusNotAllowedException);

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.CONFLICT)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("AbfrageStatusNotAllowedException")
        );
    }

    @Test
    void handleEntityNotFoundException() {
        final EntityNotFoundException entityNotFoundException = new EntityNotFoundException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleEntityNotFoundException(entityNotFoundException);

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_FOUND)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("test"))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("EntityNotFoundException")
        );
    }

    @Test
    void handleUniqueViolationExceptionTest() {

        final UniqueViolationException uniqueViolationException = new UniqueViolationException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleUniqueViolationException(uniqueViolationException);

        assertThat(response.getStatusCode(), is(HttpStatus.CONFLICT));

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(responseDto.getMessages(), is(List.of("test")));
        assertThat(responseDto.getType(), is(InformationResponseType.ERROR));
    }

    @Test
    void handleHttpRequestMethodNotSupported() {
        final HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException = new HttpRequestMethodNotSupportedException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleHttpRequestMethodNotSupported(
                httpRequestMethodNotSupportedException,
                new HttpHeaders(),
                HttpStatus.NOT_FOUND,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_FOUND)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Die HTTP-Methode test wird nicht unterstützt."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("HttpRequestMethodNotSupportedException")
        );
    }

    @Test
    void handleHttpMediaTypeNotSupported() {
        final HttpMediaTypeNotSupportedException httpMediaTypeNotSupportedException = new HttpMediaTypeNotSupportedException(MediaType.APPLICATION_JSON, List.of(MediaType.ALL));

        final ResponseEntity<Object> response = this.restExceptionHandler.handleHttpMediaTypeNotSupported(
                httpMediaTypeNotSupportedException,
                new HttpHeaders(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Der Content-Type " + MediaType.APPLICATION_JSON + " wird nicht unterstützt."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("HttpMediaTypeNotSupportedException")
        );
    }

    @Test
    void handleHttpMediaTypeNotAcceptable() {
        final HttpMediaTypeNotAcceptableException httpMediaTypeNotAcceptableException = new HttpMediaTypeNotAcceptableException(List.of(MediaType.ALL));

        final ResponseEntity<Object> response = this.restExceptionHandler.handleHttpMediaTypeNotAcceptable(
                httpMediaTypeNotAcceptableException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Der Media-Type des Requests wird nicht unterstützt."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("HttpMediaTypeNotAcceptableException")
        );
    }

    @Test
    void handleMissingPathVariable() {
        final MissingPathVariableException missingPathVariableException = new MissingPathVariableException("nameOfVariable", null);

        final ResponseEntity<Object> response = this.restExceptionHandler.handleMissingPathVariable(
                missingPathVariableException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Der Pfadvariable nameOfVariable ist nicht gesetzt."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("MissingPathVariableException")
        );
    }

    @Test
    void handleMissingServletRequestParameter() {
        final MissingServletRequestParameterException missingServletRequestParameterException = new MissingServletRequestParameterException("nameParameter", "typeParameter");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleMissingServletRequestParameter(
                missingServletRequestParameterException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Der Requestparameter nameParameter ist nicht gesetzt."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("MissingServletRequestParameterException")
        );
    }

    @Test
    void handleServletRequestBindingException() {
        final ServletRequestBindingException servletRequestBindingException = new ServletRequestBindingException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleServletRequestBindingException(
                servletRequestBindingException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Im Backend ist ein Fehler aufgetreten."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("ServletRequestBindingException")
        );
    }

    @Test
    void handleConversionNotSupported() {
        final ConversionNotSupportedException conversionNotSupportedException = new ConversionNotSupportedException(new Object(), null, null);

        final ResponseEntity<Object> response = this.restExceptionHandler.handleConversionNotSupported(
                conversionNotSupportedException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Im Backend ist ein Fehler aufgetreten."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("ConversionNotSupportedException")
        );
    }

    @Test
    void handleTypeMismatch() {
        final TypeMismatchException typeMismatchException = new TypeMismatchException(new Object(), Object.class);
        typeMismatchException.initPropertyName("thePropertyName");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleTypeMismatch(
                typeMismatchException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Das Attribut thePropertyName besitzt nicht den korrekten Datentyp."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("TypeMismatchException")
        );
    }

    @Test
    void handleHttpMessageNotReadable() {
        final HttpMessageNotReadableException httpMessageNotReadableException = new HttpMessageNotReadableException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleHttpMessageNotReadable(
                httpMessageNotReadableException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Der Nutzlast der Anfrage an das Backend konnte nicht verarbeitet werden."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("HttpMessageNotReadableException")
        );
    }

    @Test
    void handleHttpMessageNotWritable() {
        final HttpMessageNotWritableException httpMessageNotWritableException = new HttpMessageNotWritableException("test");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleHttpMessageNotWritable(
                httpMessageNotWritableException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Der Nutzlast des Antwort vom Backend konnte nicht verarbeitet werden."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("HttpMessageNotWritableException")
        );
    }

    @Test
    void handleMethodArgumentNotValid() {
        Mockito.when(this.bindingResult.getAllErrors()).thenReturn(List.of(
                new FieldError("theObjectName1", "theFieldError1", "theDefaultMessage1"),
                new FieldError("theObjectName2", "theFieldError2", "theDefaultMessage2")
        ));
        final MethodArgumentNotValidException methodArgumentNotValidException = new MethodArgumentNotValidException(null, this.bindingResult);

        final ResponseEntity<Object> response = this.restExceptionHandler.handleMethodArgumentNotValid(
                methodArgumentNotValidException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of(
                        "Attribut " + "theFieldError1" + ": " + "TheDefaultMessage1",
                        "Attribut " + "theFieldError2" + ": " + "TheDefaultMessage2"
                ))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("MethodArgumentNotValidException")
        );
    }

    @Test
    void handleMissingServletRequestPart() {
        final MissingServletRequestPartException missingServletRequestPartException = new MissingServletRequestPartException("requestPartName");

        final ResponseEntity<Object> response = this.restExceptionHandler.handleMissingServletRequestPart(
                missingServletRequestPartException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Beim Hochladen der Datei requestPartName ist ein Fehler aufgetreten."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("MissingServletRequestPartException")
        );
    }

    @Test
    void handleBindException() {
        final BindException bindException = new BindException(this.bindingResult);

        final ResponseEntity<Object> response = this.restExceptionHandler.handleBindException(
                bindException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Im Backend ist ein Fehler aufgetreten."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("BindException")
        );
    }

    @Test
    void handleNoHandlerFoundException() {
        final NoHandlerFoundException noHandlerFoundException = new NoHandlerFoundException("GET", "the-url", null);

        final ResponseEntity<Object> response = this.restExceptionHandler.handleNoHandlerFoundException(
                noHandlerFoundException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Die URL the-url konnte nicht mit der HTTP-Methode GET aufgerufen werden."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("NoHandlerFoundException")
        );
    }

    @Test
    void handleAsyncRequestTimeoutException() {
        final AsyncRequestTimeoutException asyncRequestTimeoutException = new AsyncRequestTimeoutException();

        final ResponseEntity<Object> response = this.restExceptionHandler.handleAsyncRequestTimeoutException(
                asyncRequestTimeoutException,
                new HttpHeaders(),
                HttpStatus.NOT_ACCEPTABLE,
                null
        );

        assertThat(
                response.getStatusCode(),
                is(HttpStatus.NOT_ACCEPTABLE)
        );

        final InformationResponseDto responseDto = (InformationResponseDto) response.getBody();

        assertThat(
                responseDto.getTraceId(),
                is("1111111111111111")
        );
        assertThat(
                responseDto.getSpanId(),
                is("ffffffffffffffff")
        );
        assertThat(
                responseDto.getMessages(),
                is(List.of("Im Backend ist ein Timeout aufgetreten."))
        );
        assertThat(
                responseDto.getOriginalException(),
                is("AsyncRequestTimeoutException")
        );
    }
}
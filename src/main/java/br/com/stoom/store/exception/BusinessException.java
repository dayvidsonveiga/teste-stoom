package br.com.stoom.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class BusinessException extends RuntimeException {

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Stoom internal server error");
        return problemDetail;
    }

}

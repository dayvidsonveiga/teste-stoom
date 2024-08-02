package br.com.stoom.store.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@AllArgsConstructor
public class CategoryNotFoundException extends BusinessException {

    private Long categoryId;

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Category not found");
        problemDetail.setDetail("There is no category with id " + categoryId + ".");
        return problemDetail;
    }

}

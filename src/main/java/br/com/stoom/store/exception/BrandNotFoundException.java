package br.com.stoom.store.exception;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

@AllArgsConstructor
public class BrandNotFoundException extends BusinessException {

    private Long brandId;

    @Override
    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Brand not found");
        problemDetail.setDetail("There is no brand with id " + brandId + ".");
        return problemDetail;
    }

}

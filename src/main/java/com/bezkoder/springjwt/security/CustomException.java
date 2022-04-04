package com.bezkoder.springjwt.security;

import com.bezkoder.springjwt.Utils.ErrorCode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CustomException extends RuntimeException {
    private HttpStatus status;

    private List<ErrorCode> errorCodes = Lists.newArrayList();

    public CustomException(HttpStatus status, String error, List<ErrorCode> errorCodes) {
        super(error);
        this.status = status;
        this.errorCodes = errorCodes;
    }

    public CustomException(HttpStatus status) {
        this.status = status;
    }

    public CustomException addError(ErrorCode error) {
        List<ErrorCode> newList = Lists.newArrayList(this.errorCodes);
        newList.add(Preconditions.checkNotNull(error));
        return new CustomException(this.status, this.getMessage(), newList);
    }

    public CustomException addErrors(List<ErrorCode> errorCodes) {
        List<ErrorCode> newList = Lists.newArrayList(this.errorCodes);
        newList.addAll(Preconditions.checkNotNull(errorCodes));
        return new CustomException(this.status, this.getMessage(), newList);
    }

    public CustomException withMessage(String message) {
        Preconditions.checkArgument(!Strings.isNullOrEmpty(message));
        return new CustomException(this.status, message, this.errorCodes);
    }
}

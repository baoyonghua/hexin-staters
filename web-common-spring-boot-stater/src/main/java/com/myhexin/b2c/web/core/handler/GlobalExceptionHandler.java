package com.myhexin.b2c.web.core.handler;

import com.myhexin.b2c.web.enums.ResponseStatusEnum;
import com.myhexin.b2c.web.exceptions.SystemException;
import com.myhexin.b2c.web.vo.Response;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author baoyh
 * @since 2023/10/14
 */
//@ConditionalOnProperty()
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handlerRequestParamNotValidException(ConstraintViolationException e) {
        // 获取验证期间报告的一组ConstraintViolation<约束违规>对象
        Set<ConstraintViolation<?>> cvs = e.getConstraintViolations();
        if (cvs == null || cvs.isEmpty()) {
            return Response.error(ResponseStatusEnum.SYSTEM_ERROR);
        }
        // 循环迭代出所有的约束违规信息
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        for (ConstraintViolation<?> cv : cvs) {
            // 对校验错误的信息进行拼接
            stringBuilder.append(cv.getPropertyPath()).append(": ").append(cv.getMessage());
            if (++count < cvs.size()) {
                stringBuilder.append(",");
            }
        }
        // 返回给前端统一的错误返回
        return Response.error(
                ResponseStatusEnum.PARAM_VALID_ERROR.getCode(),
                stringBuilder.toString()
        );
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<Void> handlerRequestParamNotValidException(Exception e) {
        BindingResult bindingResult = null;
        if (e instanceof BindException) {
            //获取BindingResult对象<该对象中封装了校验结果>
            bindingResult = ((BindException) e).getBindingResult();
        }
        // 如果不存在bindingResult则直接返回通用的结果
        if (Objects.isNull(bindingResult)) {
            return Response.error(ResponseStatusEnum.PARAM_VALID_ERROR);
        }
        StringBuilder stringBuilder = new StringBuilder();
        int count = 0;
        // 获取与字段关联的所有错误
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            // 拼接校验错误信息
            stringBuilder.append(fieldError.getField()).append(": ")
                    .append(fieldError.getDefaultMessage());
            if (++count < fieldErrors.size()) {
                stringBuilder.append(", ");
            }
        }
        // 返回给前端统一的错误返回
        return Response.error(
                ResponseStatusEnum.PARAM_VALID_ERROR.getCode(),
                stringBuilder.toString()
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public Response<Void> handlerDefaultException(Exception e) {
        Integer code = ResponseStatusEnum.SYSTEM_ERROR.getCode();
        if (e instanceof SystemException) {
            // 如果为业务异常则获取code
            SystemException exception = (SystemException) e;
            if (!Objects.isNull(exception.getCode())) {
                code = exception.getCode();
            }
        }
        // 返回给前端统一的错误返回
        return Response.error(code, e.toString());
    }
}

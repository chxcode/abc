package com.abc.cx.exception;

import com.abc.cx.vo.Ret;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

/**
 * @Author: ChangXuan
 * @Decription:
 * @Date: 22:12 2021/11/6
 **/
@RestControllerAdvice //包含@ControllerAdvice和@ResponseBody
public class GlobalExceptionHandler {

    /**
     * 处理SpringBoot中Bean Validation抛出的异常
     *
     * @return ResultInfo
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Ret validationExceptionHandler(HttpServletRequest req, ValidationException e) {
        StringBuilder stringBuilder = new StringBuilder();
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                // TODO 需要优化返回的错误提示信息
                stringBuilder.append(item.getPropertyPath());
                stringBuilder.append(item.getMessage());
                stringBuilder.append(";");
            }
        }
        return Ret.failMsg(stringBuilder.toString());
    }

    /**
     * 处理 FileException 异常
     *
     */
    @ExceptionHandler(FileException.class)
    public void unknownExceptionHandler(HttpServletRequest req, HttpServletResponse response, FileException e) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(e.getMessage());
    }


    /**
     * 处理所有其它异常
     *
     * @return ResultInfo
     */
    @ExceptionHandler(Exception.class)
    public Ret unknownExceptionHandler(HttpServletRequest req, Exception e) {
        return Ret.failMsg(e.getMessage());
    }
}

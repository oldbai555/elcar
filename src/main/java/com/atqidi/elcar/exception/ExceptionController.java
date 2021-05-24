package com.atqidi.elcar.exception;

import com.atqidi.elcar.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理
 *
 * @author 老白
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handlerException(Exception e) {
        e.printStackTrace();
        log.info(e.getMessage());
        return Result.fail().message("服务器繁忙");
    }
}
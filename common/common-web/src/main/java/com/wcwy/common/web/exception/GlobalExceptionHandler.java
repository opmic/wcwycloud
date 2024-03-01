package com.wcwy.common.web.exception;

import com.mysql.cj.jdbc.exceptions.MysqlDataTruncation;
import com.taobao.api.ApiException;
import com.wcwy.common.base.result.R;
import com.wcwy.common.base.utils.SendDingDing;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.util.Objects;

/**
 * @author： 乐哥聊编程(全平台同号)
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(IllegalArgumentException.class)
    public <T> R<T> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("非法参数异常，异常原因：{}", e.getMessage(), e);
        e.printStackTrace();
        return R.fail(e.getMessage());
    }
    private final static String EXCEPTION_MSG_KEY = "Exception message : ";

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R handleValidException(MethodArgumentNotValidException e){
        //日志记录错误信息
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        //将错误信息返回给前台
        return R.error(103, Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<String> globalExceptionHandle(Exception e) {
        log.error("===========全局统一异常处理============");
        log.error(getExceptionInfo(e));
        if(e instanceof InterruptedException){
            return R.error(500,"上一步操作还在进行中，请不要重复操作！");
        }else  if(e instanceof MysqlDataTruncation){
            return R.error(500,"请联系管理员！谢谢您咯");
        }
        else  if(e instanceof SQLException){
            return R.error(500,"数据库繁忙,请稍后操作！");
        }else if(e instanceof SQLSyntaxErrorException){
            return R.error(500,"语句错误，请联系管理员！");
        }else if(e instanceof IllegalMonitorStateException){
            return R.error(500,"处理时间过长,请从新发送请求！");
        }
        try {
            SendDingDing.sendText("报错信息："+e.getMessage());
        } catch (ApiException apiException) {
            apiException.printStackTrace();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            unsupportedEncodingException.printStackTrace();
        } catch (InvalidKeyException invalidKeyException) {
            invalidKeyException.printStackTrace();
        }
        return  R.error(500, e.getMessage());
    }

    private static String getExceptionInfo(Exception ex) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(out);
        ex.printStackTrace(printStream);
        String rs = new String(out.toByteArray());
        try {
            printStream.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }
}

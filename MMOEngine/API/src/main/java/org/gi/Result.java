package org.gi;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.spec.ECField;

@Getter
@AllArgsConstructor
public class Result {
    int code;
    String message;

    public static Result SUCCESS = new Result(1,"Success");

    public static Result FAIL = new Result(-1,"Fail");

    public static Result CONNECTED = new Result(100,"Connected");

    public static Result DISCONNECTED = new Result(100,"Disconnected");

    public static Result NULL = new Result(-100,"Null");

    public static Result EMPTY = new Result(-101,"Empty");

    public static Result ERROR(int code,String message){
        return new Result(code,message);
    }

    public static Result ERROR(String message){
        return ERROR(-9000,message);
    }

    public static Result EXCEPTION(int code, Exception e){
        return new Result(code,e.getMessage());
    }

    public static Result EXCEPTION(Exception e){
        return EXCEPTION(-9999,e);
    }

    public boolean isSuccess(){
        return this.equals(SUCCESS);
    }
}

package com.example.studybatch.exception;

import org.springframework.stereotype.Component;

@Component
public class MyException {
    public void callMyException() throws Exception{
        throw new Exception(" ---<<< My Exception >>>---");
    }
}

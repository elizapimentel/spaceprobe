package com.br.eliza.spaceprobe.exceptions;

public class RoverUnavailableException extends RuntimeException {

    public RoverUnavailableException() {
        super();
    }
    public RoverUnavailableException(String msg) {
        super(msg);
    }
    public RoverUnavailableException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

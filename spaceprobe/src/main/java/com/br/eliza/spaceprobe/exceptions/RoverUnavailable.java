package com.br.eliza.spaceprobe.exceptions;

public class RoverUnavailable extends RuntimeException {

    public RoverUnavailable() {
        super();
    }
    public RoverUnavailable(String msg) {
        super(msg);
    }
    public RoverUnavailable(String msg, Throwable cause) {
        super(msg, cause);
    }
}

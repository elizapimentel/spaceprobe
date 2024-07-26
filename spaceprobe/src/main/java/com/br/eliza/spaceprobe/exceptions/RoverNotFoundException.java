package com.br.eliza.spaceprobe.exceptions;

import java.io.Serial;

public class RoverNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1193041487833121712L;

    public RoverNotFoundException() {
        super();
    }
    public RoverNotFoundException(String msg) {
        super(msg);
    }

    public RoverNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

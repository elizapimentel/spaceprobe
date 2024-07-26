package com.br.eliza.spaceprobe.exceptions;

import java.io.Serial;

public class InvalidCommandException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 2313896878219683094L;

    public InvalidCommandException() {
        super();
    }

    public InvalidCommandException(String msg) {
        super(msg);
    }

    public InvalidCommandException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

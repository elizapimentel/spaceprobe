package com.br.eliza.spaceprobe.exceptions;

import java.io.Serial;

public class PlanetFullException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 4243982735232468661L;

    public PlanetFullException() {
        super();
    }

    public PlanetFullException(String msg) {
        super(msg);
    }

    public PlanetFullException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

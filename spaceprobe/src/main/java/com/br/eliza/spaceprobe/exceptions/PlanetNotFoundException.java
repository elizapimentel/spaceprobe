package com.br.eliza.spaceprobe.exceptions;

import java.io.Serial;

public class PlanetNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1193041487833121712L;

    public PlanetNotFoundException() {
        super();
    }
    public PlanetNotFoundException(String msg) {
        super(msg);
    }

    public PlanetNotFoundException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

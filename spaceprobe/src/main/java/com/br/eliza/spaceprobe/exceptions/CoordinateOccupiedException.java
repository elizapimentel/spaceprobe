package com.br.eliza.spaceprobe.exceptions;

import java.io.Serial;

public class CoordinateOccupiedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -1193041487833121712L;

    public CoordinateOccupiedException() {
        super();
    }
    public CoordinateOccupiedException(String msg) {
        super(msg);
    }

    public CoordinateOccupiedException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

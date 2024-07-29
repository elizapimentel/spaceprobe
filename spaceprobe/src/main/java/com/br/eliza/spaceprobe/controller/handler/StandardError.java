package com.br.eliza.spaceprobe.controller.handler;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class StandardError {
    private Integer code;
    private String error;
}
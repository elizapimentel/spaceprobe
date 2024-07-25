package com.br.eliza.spaceprobe.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class RoverCommandDTO implements Serializable {

    private Long roverId;
    private List<Character> commands;
}

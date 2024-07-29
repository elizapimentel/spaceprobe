package com.br.eliza.spaceprobe.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class CommandDTO extends RepresentationModel<CommandDTO> {

    @Valid
    @NotNull(message="Rover id must be informed")
    private Long roverId;

    @Valid
    @NotNull(message="Commands must be informed")
    private List<Character> commands;

}

package com.br.eliza.spaceprobe.dto;
import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.model.Rover;
import org.modelmapper.ModelMapper;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoverDTO extends RepresentationModel<RoverDTO> {

    private Long roverId;
    @NotNull(message="Coordinates must be informed")
    private Coordinates coordinates;
    @NotNull(message="Direction must be informed")
    private Direction direction;
    private boolean isOn;
    private Long planetId;

    public Rover convertDtoToEntity() {
        return new ModelMapper().map(this, Rover.class);
    }
}

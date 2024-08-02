package com.br.eliza.spaceprobe.controller.rover;

import com.br.eliza.spaceprobe.dto.CommandDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.RoverNotFoundException;
import com.br.eliza.spaceprobe.service.rover.RoverService;
import com.br.eliza.spaceprobe.util.config.LinkConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static com.br.eliza.spaceprobe.common.Constants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@WebMvcTest(controllers = RoverController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = LinkConfig.class))
public class RoverControllerTest {

    @MockBean
    private RoverService service;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void mustAddRover() throws Exception {
        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);

        when(service.save(any(RoverDTO.class))).thenReturn(roverDTO);

        mockMvc.perform(post(ROVER_BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roverId\":1,\"coordinates\":{\"x\":1,\"y\":1},\"direction\":\"NORTH\",\"isOn\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roverId").value(ROVER_ID))
                .andReturn();

        verify(service, times(1)).save(any(RoverDTO.class));
    }

    @Test
    public void mustFindAllRovers() throws Exception {
        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        List<RoverDTO> rovers = new ArrayList<>();
        rovers.add(roverDTO);

        when(service.findAll()).thenReturn(rovers);

        mockMvc.perform(get(ROVER_BASE_URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).findAll();
    }


    @Test
    public void mustMoveRover() throws Exception {
        CommandDTO commandDTO = new CommandDTO();
        commandDTO.setRoverId(ROVER_ID);
        RoverDTO updatedRoverDTO = new RoverDTO();
        updatedRoverDTO.setRoverId(ROVER_ID);

        when(service.moveRover(any(CommandDTO.class))).thenReturn(updatedRoverDTO);

        mockMvc.perform(post(ROVER_BASE_URL + "/move")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roverId\":1,\"commands\":[\"2\",\"3\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roverId").value(ROVER_ID))
                .andReturn();

        verify(service, times(1)).moveRover(any(CommandDTO.class));
    }

    @Test
    public void mustFindRoverById() throws Exception {
        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);

        when(service.findById(eq(ROVER_ID))).thenReturn(roverDTO);

        mockMvc.perform(get(ROVER_BASE_URL + "/{roverId}", ROVER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roverId").value(ROVER_ID))
                .andReturn();

        verify(service, times(1)).findById(eq(ROVER_ID));
    }

    @Test
    public void mustReturnRoverNotFoundError() throws Exception {
        when(service.findById(eq(ROVER_ID))).thenThrow(new RoverNotFoundException("Rover not found"));

        mockMvc.perform(get(ROVER_BASE_URL + "/{roverId}", ROVER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(service, times(1)).findById(eq(ROVER_ID));
    }

    @Test
    public void mustUpdateRoverPlanet() throws Exception {
        RoverDTO updatedRoverDTO = new RoverDTO();
        updatedRoverDTO.setRoverId(ROVER_ID);

        when(service.updatePlanet(eq(ROVER_ID), eq(PLANET_ID))).thenReturn(updatedRoverDTO);

        mockMvc.perform(put(ROVER_BASE_URL + "/{roverId}/planet/{newPlanetId}", ROVER_ID, PLANET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roverId").value(ROVER_ID))
                .andReturn();

        verify(service, times(1)).updatePlanet(eq(ROVER_ID), eq(PLANET_ID));
    }

    @Test
    public void mustTurnOnOffRover() throws Exception {
        RoverDTO updatedRoverDTO = new RoverDTO();
        updatedRoverDTO.setRoverId(ROVER_ID);

        when(service.turnOnOff(eq(ROVER_ID))).thenReturn(updatedRoverDTO);

        mockMvc.perform(put(ROVER_BASE_URL + "/{roverId}/plug", ROVER_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roverId").value(ROVER_ID))
                .andReturn();

        verify(service, times(1)).turnOnOff(eq(ROVER_ID));
    }

}

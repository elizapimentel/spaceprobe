package com.br.eliza.spaceprobe.controller.planet;

import com.br.eliza.spaceprobe.dto.PlanetDTO;
import com.br.eliza.spaceprobe.dto.RoverDTO;
import com.br.eliza.spaceprobe.exceptions.PlanetNotFoundException;
import com.br.eliza.spaceprobe.model.Coordinates;
import com.br.eliza.spaceprobe.service.planet.PlanetService;
import com.br.eliza.spaceprobe.util.LinkUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.br.eliza.spaceprobe.common.Constants.*;
import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@WebMvcTest(controllers = PlanetController.class,
        includeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = LinkUtil.class))
@TestPropertySource(properties = "server.port=8080")
public class PlanetControllerTest {

    @MockBean
    private PlanetService service;

    @Autowired
    private MockMvc mockMvc;


    @Test
    public void mustReturnAllPlanets() throws Exception{
        PlanetDTO planetDTO = new PlanetDTO();
        planetDTO.setPlanetId(PLANET_ID);
        planetDTO.setPlanetName(PLANET_NAME);
        planetDTO.setWidth(WIDTH);
        planetDTO.setHeight(HEIGHT);

        RoverDTO roverDTO = new RoverDTO();
        roverDTO.setRoverId(ROVER_ID);
        roverDTO.setCoordinates(COORDINATES);
        roverDTO.setDirection(DIRECTION);
        roverDTO.setIsOn(IS_ON);

        planetDTO.setRovers(singletonList(roverDTO));

        when(service.findAll()).thenReturn(singletonList(planetDTO));

        mockMvc.perform(get(PLANET_BASE_URL + "/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].planetName").value(PLANET_NAME))
                .andReturn();

        verify(service, times(1)).findAll();
    }

    @Test
    public void mustAddPlanet() throws Exception {
        when(service.save(any(PlanetDTO.class))).thenReturn(new PlanetDTO());

        mockMvc.perform(post(PLANET_BASE_URL + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"planetName\":\"Mars\",\"width\":5,\"height\":5}"))
                .andExpect(status().isCreated())
                .andReturn();
        verify(service, times(1)).save(any(PlanetDTO.class));
    }

    @Test
    public void mustReturnPlanetById() throws Exception {
        when(service.findById(eq(PLANET_ID))).thenReturn(new PlanetDTO());

        mockMvc.perform(get(PLANET_BASE_URL + "/{id}", PLANET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).findById(eq(PLANET_ID));
    }

    @Test
    public void mustReturnPlanetNotFoundError() throws Exception {
        when(service.findById(eq(PLANET_ID))).thenThrow(new PlanetNotFoundException("Planet not found"));

        mockMvc.perform(get(PLANET_BASE_URL + "/{id}", PLANET_ID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(service, times(1)).findById(eq(PLANET_ID));
    }

    @Test
    public void mustAddRoverToPlanet() throws Exception {
        when(service.addRover(eq(PLANET_ID), any(RoverDTO.class)))
                .thenReturn(new PlanetDTO());

        ResultActions resultActions = mockMvc.perform(post(PLANET_BASE_URL + "/{planetId}", PLANET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"roverId\":1,\"coordinates\":{\"x\":1,\"y\":1},\"direction\":\"NORTH\",\"isOn\":true}"))
                .andExpect(status().isOk());

        verify(service, times(1)).addRover(eq(PLANET_ID), any(RoverDTO.class));

    }

    @Test
    public void mustReturnIsOccupiedTrue() throws Exception {
        when(service.isOccupied(eq(PLANET_ID), any(Coordinates.class))).thenReturn(true);

        mockMvc.perform(get(PLANET_BASE_URL + "/{planetId}/isOccupied", PLANET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":1,\"y\":1}"))
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).isOccupied(eq(PLANET_ID), any());

    }

    @Test
    public void mustReturnIsOccupiedFalse() throws Exception {
        when(service.isOccupied(eq(PLANET_ID), any(Coordinates.class))).thenReturn(false);

        mockMvc.perform(get(PLANET_BASE_URL + "/{planetId}/isOccupied", PLANET_ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"x\":1,\"y\":1}"))
                .andExpect(status().isOk())
                .andReturn();

        verify(service, times(1)).isOccupied(eq(PLANET_ID), any());

    }


}

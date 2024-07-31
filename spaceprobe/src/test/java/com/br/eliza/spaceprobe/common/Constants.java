package com.br.eliza.spaceprobe.common;

import com.br.eliza.spaceprobe.enums.Direction;
import com.br.eliza.spaceprobe.model.Coordinates;

public class Constants {

    //planet
    public static final Long PLANET_ID                 = 1L;
    public static final String PLANET_NAME             = "Mars";
    public static final int WIDTH                      = 5;
    public static final int HEIGHT                     = 5;



    //rover
    public static final Long ROVER_ID                  = 1L;
    public static final int X                          = 1;
    public static final int Y                          = 2;
    public static final int NEGATIVE_X                 = -1;
    public static final int NEGATIVE_Y                 = -1;
    public static final Direction DIRECTION            = Direction.NORTH;
    public static final boolean IS_ON                  = true;
    public static final Coordinates COORDINATES        = new Coordinates(X, Y);

    public static final String PLANET_BASE_URL               = "/v2/planets";
    public static final String ROVER_BASE_URL                = "/v2/rovers";

    public static final String INVALID_COORDINATES_MESSAGE = "Coordinates must not be negative.";
    public static final String PLANET_NOT_FOUND_MESSAGE = "Planet not found";
    public static final String ROVER_NOT_FOUND_MESSAGE = "Rover not found";

}

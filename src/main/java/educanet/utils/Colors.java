package educanet.utils;

import java.util.Locale;

public class Colors {


    public final static float[] WHITE = {
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
            1.0f, 1.0f, 1.0f,
    };

    public final static float[] BLACK = {
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 0.0f, 0.0f,
    };

    public final static float[] INTERPOLATED = {
            1.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 0.0f,
    };

    public final static float[] RED = {
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
    };

    public final static float[] GREEN = {
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
    };

    public final static float[] BLUE = {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
    };

    public static float[] add(float[] color, float amount, String channel) {
        int remainder = 0; //default RED channel || first column in the 3x4 array

             if  (channel.toUpperCase(Locale.ROOT).equals("GREEN") || channel.toUpperCase(Locale.ROOT).equals("G")) remainder = 1;
        else if  (channel.toUpperCase(Locale.ROOT).equals("BLUE")  || channel.toUpperCase(Locale.ROOT).equals("B")) remainder = 2;



        return color;
    };


}

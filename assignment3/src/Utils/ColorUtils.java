package Utils;

import java.awt.*;

public class ColorUtils {
    public static Color mult(Color a, Color b){
        float red = (a.getRed() / 255.0F) * (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) * (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) * (b.getGreen() / 255.0F);
        return new Color(red,green,blue);

    }

    public static Color mult(Color a, float num){
        float red = (a.getRed() / 255.0F) * num;
        float blue = (a.getBlue() / 255.0F) * num;
        float green = (a.getGreen() / 255.0F) * num;
        return new Color(red,green,blue);

    }

    public static Color plus(Color a, Color b){
        float red = (a.getRed() / 255.0F) + (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) + (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) + (b.getGreen() / 255.0F);
        return new Color(red,green,blue);

    }
}

package Utils;

import java.awt.*;

public class ColorUtils {
    public static Color mult(Color a, Color b){
        float red = (a.getRed() / 255.0F) * (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) * (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) * (b.getGreen() / 255.0F);
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));

    }

    public static Color mult(Color a, float num){
        float red = (a.getRed() / 255.0F) * num;
        float blue = (a.getBlue() / 255.0F) * num;
        float green = (a.getGreen() / 255.0F) * num;
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));

    }

    public static Color plus(Color a, Color b){
        float red = (a.getRed() / 255.0F) + (b.getRed() / 255.0F);
        float blue = (a.getBlue() / 255.0F) + (b.getBlue() / 255.0F);
        float green = (a.getGreen() / 255.0F) + (b.getGreen() / 255.0F);
        return new Color(Math.min(red,1),Math.min(green,1),Math.min(blue,1));

    }

    public static Color avgColor(java.util.List<Color> colorList){
        float red = 0;
        float green = 0;
        float blue = 0;
        int count = 0;
        for(Color c: colorList){
            red += (c.getRed() / 255.0F);
            green += (c.getGreen() / 255.0F);
            blue += (c.getBlue() / 255.0F);
            count++;
        }
        red = red / count;
        green = green / count;
        blue = blue / count;
        return new Color(red,green,blue);

    }
}

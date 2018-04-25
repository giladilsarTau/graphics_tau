import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Energy {

    private static final int ENERGY_REGULAR = 0;
    private static final int ENERGY_ENTROPY = 1;
    private static final int ENERGY_FORWARD = 2;


    public static Double[][] calcEnergy(BufferedImage bi, int type) throws IOException{
        switch (type){
            case ENERGY_REGULAR: return calcRegularEnergy(bi);
            case ENERGY_ENTROPY: return calcEntropyEnergy(bi);
            default: return null;
        }

    }

    private static Double[][] calcRegularEnergy(BufferedImage bi) throws IOException{

        Double[][] energyMat = new Double[bi.getHeight()][bi.getWidth()];
        int height = bi.getHeight();
        int width = bi.getWidth();
        for(int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                energyMat[y][x] = calcRGBDiff(bi,x,y);

        return energyMat;
    }

    private static Double[][] calcEntropyEnergy(BufferedImage bi) throws IOException{
        Double[][] energyMat = new Double[bi.getHeight()][bi.getWidth()];
        int height = bi.getHeight();
        int width = bi.getWidth();
        for(int y = 0; y < height; y++)
            for (int x = 0; x < width; x++) {
                energyMat[y][x] = calcRGBDiff(bi, x, y);
                energyMat[y][x] += calcLocalEntropy(bi, x, y);
            }
        return energyMat;
    }

    private static double calcRGBDiff(BufferedImage bi, int x , int y){
        int count = 0;
        int diff = 0;
        Color myColor = new Color(bi.getRGB(x, y));
        for(int i = -1; i < 2; i++){
            for (int j = -1; j < 2; j++){
                if(!(i == 0 && j == 0)) {
                    try {
                        Color nColor = new Color(bi.getRGB(x + i, y + j));
                        diff = diff + Math.abs(myColor.getRed() - nColor.getRed()) +
                                Math.abs(myColor.getBlue() - nColor.getBlue()) +  Math.abs(myColor.getGreen() - nColor.getGreen());
                        count++;
                    }catch (ArrayIndexOutOfBoundsException e){} //catch "non neighbors"
                }
            }
        }
        return diff / count;
    }

    private static double calcLocalEntropy(BufferedImage bi, int x , int y){
        //calc the sum of the window's grayscale value for the rho calc
        int gray = 0;
        for(int i = -4; i <= 4; i++){
            for(int j = -4; j <=4; j++){
                try{
                    Color nColor = new Color(bi.getRGB(x + i, y - j));
                    gray += rgbToGray(nColor);
                }catch (ArrayIndexOutOfBoundsException e) {} //catch "non neighbors"
            }
        }
        //calc the entropy
        double H_ij = 0.0;
        for(int i = -4; i <= 4; i++){
            for(int j = -4; j <=4; j++){
                try{
                    Color myColor = new Color(bi.getRGB(x + i, y - j));
                    double myGray = rgbToGray(myColor);
                    double rho = myGray / gray;
                    if(rho != 0) //if rho = 0 than log(rho) is not defined (and result should be zero)
                        H_ij += rho * Math.log(rho);
                }catch (ArrayIndexOutOfBoundsException e) {} //catch "non neighbors"
            }
        }
        return (-1.0) * H_ij;
    }

    //weighted grayscale, form stack overflow
    private static int rgbToGray(Color c){
        int red = (int)(c.getRed() * 0.299);
        int green = (int)(c.getGreen() * 0.587);
        int blue =  (int)(c.getBlue() *0.114);
        return red + green + blue;
    }
}

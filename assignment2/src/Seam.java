import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Seam {

    public static Double[][] getSeamMap(Double[][] energyMat) {
        int height = energyMat.length;
        int width = energyMat[0].length;
        Double[][] seamMap = new Double[height][width];
        //first, init the top row
        for (int i = 0; i < width; i++)
            seamMap[0][i] = energyMat[0][i];
        //now dynamically program the entire table
        for (int i = 1; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //seamMap[i][j] = energyMat[i][j] + seamMap[i-1][j];   <-- straight seam
                if (j == 0)
                    seamMap[i][j] = energyMat[i][j] + Math.min(seamMap[i - 1][j], seamMap[i - 1][j + 1]);
                else if (j == width - 1)
                    seamMap[i][j] = energyMat[i][j] + Math.min(seamMap[i - 1][j - 1], seamMap[i - 1][j]);
                else
                    seamMap[i][j] = energyMat[i][j] + Math.min(seamMap[i - 1][j - 1], Math.min(seamMap[i - 1][j], seamMap[i - 1][j + 1]));
            }
        }

        return seamMap;
    }

    public static int[] getStraightSeam(Double[][] energyMat) {
        int[] seam = new int[energyMat.length];
        //find the min in the bottom
        seam[seam.length - 1] = minIndex(energyMat[energyMat.length - 1]);
        for (int i = 0; i < energyMat.length - 1; i++)
            seam[i] = seam[seam.length - 1];
        return seam;
    }

    public static int[] getSeam(Double[][] energyMat) {
        int[] seam = new int[energyMat.length];
        int width = energyMat[0].length;
        //find the min in the bottom
        seam[seam.length - 1] = minIndex(energyMat[energyMat.length - 1]);
        for (int i = seam.length - 2; i >= 0; i--) {
            int myDex = seam[i + 1];
            Double minVal = energyMat[i][myDex];
            int res = myDex;
            if (myDex > 0) {
                if (energyMat[i][myDex - 1] < minVal) {
                    res = myDex - 1;
                    minVal = energyMat[i][myDex - 1];
                }
            }
            if (myDex < width -1 ){
                if (energyMat[i][myDex + 1] < minVal) {
                    res = myDex + 1;
                    minVal = energyMat[i][myDex + 1];
                }
            }
            seam[i] = res;
        }
        return seam;
    }

    public static BufferedImage cutSeam(BufferedImage bi, int[] seam) {
        int height = bi.getHeight();
        int width = bi.getWidth();
        BufferedImage copy = new BufferedImage(width - 1, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            //copy up to the seam
            if (seam[y] != 0) {
                for (int x = 0; x < seam[y]; x++)
                    copy.setRGB(x, y, bi.getRGB(x, y));
            }
            //from the seam forward
            if (seam[y] != width - 1) {
                for (int x = seam[y]; x < width - 1; x++)
                    copy.setRGB(x, y, bi.getRGB(x + 1, y));
            }
        }
        return copy;
    }

    public static BufferedImage addSeam(List<int[]> oldSeams, BufferedImage bi, int[] seam, boolean isAleterd){
        int height = bi.getHeight();
        int width = bi.getWidth();
        BufferedImage copy = new BufferedImage(width + 1, height, BufferedImage.TYPE_INT_RGB);
        for(int y = 0; y < height; y++){
            List<Integer> offSets = new ArrayList<>();
            for(int[] e : oldSeams){
                offSets.add(e[y]);
            }
            Collections.reverse(offSets);
            int seamPlace = inc(offSets,seam[y]);
            for (int x = 0; x <= seamPlace; x++){
                copy.setRGB(x,y,bi.getRGB(x,y));
            }

            //add the seam again
            if(!isAleterd)
                copy.setRGB(seamPlace+1,y,bi.getRGB(seamPlace,y));
            else{
                Color c = averageColor(seamPlace,y,bi);
                copy.setRGB(seamPlace +1,y , c.getRGB());
            }

            for (int x = seamPlace+1; x < width; x++){
                copy.setRGB(x+1,y,bi.getRGB(x,y));
            }
        }
        return copy;
    }

    private static int inc(List<Integer> old, int curr){
        if(old.isEmpty())
            return curr;
        int offset = 0;
        for(int e : old)
            if (curr >= e)
                offset +=2;
        return curr + offset;
    }

    private static int minIndex(Double[] arr) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < min) {
                min = arr[i];
                index = i;
            }
        }
        return index;
    }

    private static Color averageColor(int x, int y, BufferedImage bi){
        int count = 0;
        int red = 0;
        int green = 0;
        int blue = 0;
        for(int i = -1; i <=1; i++){
            try{
                Color c = new Color(bi.getRGB(x+i,y));
                red += c.getRed();
                green += c.getGreen();
                blue+= c.getBlue();
                count++;
            } catch (ArrayIndexOutOfBoundsException e){}
        }
        Color nColor = new Color(red / count, green/count, blue/count);
        return nColor;
    }
}

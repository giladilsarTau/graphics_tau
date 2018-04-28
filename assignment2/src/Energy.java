import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Energy {

    private static final int ENERGY_REGULAR = 0;
    private static final int ENERGY_ENTROPY = 1;
    private static final int ENERGY_FORWARD = 2;

    private static final double ENTROPY_WEIGHT_FOR_REGULAR_PART = 1;
    private static final double ENTROPY_WEIGHT_FOR_ENTROPY_PART = 4;

    private static final double FORWARD_WEIGHT_FOR_REGULAR_PART = 5;
    private static final double FORWARD_WEIGHT_FOR_ARTIFACTS_PART = 1;

    private static final int MAX_THREAD_COUNT = 8;


    public static Double[][] calcEnergy(BufferedImage bi, int type) throws Exception {
        switch (type) {
            case ENERGY_REGULAR:
                return calcRegularEnergy(bi);
            case ENERGY_ENTROPY:
                return calcEntropyEnergy(bi);
            case ENERGY_FORWARD:
                return calcForwardEnergy(bi);
            default:
                return null;
        }

    }


    private static Double[][] calcRegularEnergy(BufferedImage bi) throws IOException {

        Double[][] energyMat = new Double[bi.getHeight()][bi.getWidth()];
        int height = bi.getHeight();
        int width = bi.getWidth();
        for (int y = 0; y < height; y++)
            for (int x = 0; x < width; x++)
                energyMat[y][x] = calcRGBDiff(bi, x, y);

        return energyMat;
    }

    private static Double[][] calcEntropyEnergy(BufferedImage bi) throws Exception {
        Double[][] energyMat = new Double[bi.getHeight()][bi.getWidth()];
        int height = bi.getHeight();
        int width = bi.getWidth();
        List<EntropyThread> threadList = new ArrayList<>();
        int threadCount = 0;
        for (int y = 0; y < height; y++){
            EntropyThread t = new EntropyThread(y,bi,energyMat);
            threadList.add(t);
            t.start();
            threadCount++;
            if(threadCount == MAX_THREAD_COUNT){
                for(Thread th : threadList)
                    th.join();
                threadList.clear();
                threadCount = 0;
            }
        }
        for(Thread t : threadList){
            t.join();
        }

        return energyMat;
    }

    private static Double[][] calcForwardEnergy(BufferedImage bi) throws IOException {

        Double[][] energyMat = new Double[bi.getHeight()][bi.getWidth()];
        int height = bi.getHeight();
        int width = bi.getWidth();

        //for (y,0) and (y,width - 1) we will have give the regular energy
        for (int y = 0; y < height; y++) {
            energyMat[y][0] = FORWARD_WEIGHT_FOR_REGULAR_PART * calcRGBDiff(bi, 0, y);
            energyMat[y][width - 1] = FORWARD_WEIGHT_FOR_REGULAR_PART * calcRGBDiff(bi, width - 1, y);
        }

        //The first row is a bit different - it cann't have the a "full corner diff"
        for (int x = 1; x < width - 1; x++) {
            Color leftPixelColor = new Color(bi.getRGB(x - 1, 0));
            Color rightPixelColor = new Color(bi.getRGB(x + 1, 0));

            energyMat[0][x] =
                    FORWARD_WEIGHT_FOR_ARTIFACTS_PART *
                            (Math.abs(leftPixelColor.getGreen() - rightPixelColor.getGreen())
                                    + Math.abs(leftPixelColor.getRed() - rightPixelColor.getRed())
                                    + Math.abs(leftPixelColor.getBlue() - rightPixelColor.getBlue())
                            );

            energyMat[0][x] += FORWARD_WEIGHT_FOR_REGULAR_PART * calcRGBDiff(bi, x, 0);
        }
        for (int y = 1; y < height; y++) {
            for (int x = 1; x < width - 1; x++) {
                energyMat[y][x] = FORWARD_WEIGHT_FOR_REGULAR_PART * calcRGBDiff(bi, x, y);
                energyMat[y][x] += FORWARD_WEIGHT_FOR_ARTIFACTS_PART * calcEnergyRemovalDiff(bi, x, y);
            }
        }
        return energyMat;
    }

    private static double calcRGBDiff(BufferedImage bi, int x, int y) {
        int count = 0;
        int diff = 0;
        Color myColor = new Color(bi.getRGB(x, y));
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0)) {
                    try {
                        Color nColor = new Color(bi.getRGB(x + i, y + j));
                        diff = diff + Math.abs(myColor.getRed() - nColor.getRed()) +
                                Math.abs(myColor.getBlue() - nColor.getBlue()) + Math.abs(myColor.getGreen() - nColor.getGreen());
                        count++;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    } //catch "non neighbors"
                }
            }
        }
        return diff / count;
    }

    private static double calcLocalEntropy(BufferedImage bi, int x, int y) {
        //calc the sum of the window's grayscale value for the rho calc
        int gray = 0;
        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                try {
                    Color nColor = new Color(bi.getRGB(x + i, y - j));
                    gray += rgbToGray(nColor);
                } catch (ArrayIndexOutOfBoundsException e) {
                } //catch "non neighbors"
            }
        }
        //calc the entropy
        double H_ij = 0.0;
        for (int i = -4; i <= 4; i++) {
            for (int j = -4; j <= 4; j++) {
                try {
                    Color myColor = new Color(bi.getRGB(x + i, y - j));
                    double myGray = rgbToGray(myColor);
                    double rho = myGray / gray;
                    if (rho != 0) //if rho = 0 than log(rho) is not defined (and result should be zero)
                        H_ij += rho * Math.log(rho);
                } catch (ArrayIndexOutOfBoundsException e) {
                } //catch "non neighbors"
            }
        }
        return (-1.0) * H_ij;
    }

    private static double calcEnergyRemovalDiff(BufferedImage bi, int x, int y) {

        if (x != 0 && x != (bi.getWidth() - 1)) {
            Color upperRowPixelColor = new Color(bi.getRGB(x, y - 1));
            Color leftPixelColor = new Color(bi.getRGB(x - 1, y));
            Color rightPixelColor = new Color(bi.getRGB(x + 1, y));

            double middleDiff =
                    Math.abs(leftPixelColor.getGreen() - rightPixelColor.getGreen())
                            + Math.abs(leftPixelColor.getRed() - rightPixelColor.getRed())
                            + Math.abs(leftPixelColor.getBlue() - rightPixelColor.getBlue());

            double upperLeftDiff =
                    Math.abs(leftPixelColor.getGreen() - upperRowPixelColor.getGreen())
                            + Math.abs(leftPixelColor.getRed() - upperRowPixelColor.getRed())
                            + Math.abs(leftPixelColor.getBlue() - upperRowPixelColor.getBlue());

            double upperRightDiff =
                    Math.abs(rightPixelColor.getGreen() - upperRowPixelColor.getGreen())
                            + Math.abs(rightPixelColor.getRed() - upperRowPixelColor.getRed())
                            + Math.abs(rightPixelColor.getBlue() - upperRowPixelColor.getBlue());

            //If we look carefully at the equations at 5.1
            //	, the result here will be the 3-way min there
            return middleDiff + Math.min(upperLeftDiff, upperRightDiff);
        } else {
            //with min on the artifacts added if vertical-egde-pixel will be removed, we always get 0
            return 0;
        }
    }

    //weighted grayscale, form stack overflow
    private static int rgbToGray(Color c) {
        int red = (int) (c.getRed() * 0.299);
        int green = (int) (c.getGreen() * 0.587);
        int blue = (int) (c.getBlue() * 0.114);
        return red + green + blue;
    }

    private static class EntropyThread extends Thread{
        private int row;
        private BufferedImage bi;
        private Double[][] energy;
        private int width;
        private EntropyThread(int row, BufferedImage bi, Double[][] energy){
            this.bi = bi;
            this.row = row;
            this.energy = energy;
            this.width = bi.getWidth();
        }
        @Override
        public void run(){
            for (int x = 0; x < width; x++) {
                energy[row][x] = ENTROPY_WEIGHT_FOR_REGULAR_PART * calcRGBDiff(bi, x, row);
                energy[row][x] += ENTROPY_WEIGHT_FOR_ENTROPY_PART * calcLocalEntropy(bi, x, row);
            }
        }
    }

}

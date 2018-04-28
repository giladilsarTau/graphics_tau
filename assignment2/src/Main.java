import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {


    private static boolean hasAllargs(String[] args) {
        return args.length >= 5;
    }

    public static void main(String[] args) throws Exception {

        String input = hasAllargs(args) ? args[1] : "traffic.jpg";
        BufferedImage bi = ImageIO.read(new File(input));

        int numOfCols = hasAllargs(args) ? Integer.parseInt(args[2]) : (int) (bi.getWidth() * 0.8);
        int numOfRows = hasAllargs(args) ? Integer.parseInt(args[3]) : (int) (bi.getHeight() * 0.8);
        int energyType = hasAllargs(args) ? Integer.parseInt(args[4]) : 2;
        String output = hasAllargs(args) ? args[5] : "forward_2.jpg";


        int colsToRemove = bi.getWidth() - numOfCols;
        int rowToRemove = bi.getHeight() - numOfRows;
        int colsToAdd = colsToRemove < 0 ? -colsToRemove : 0;
        int rowsToAdd = rowToRemove < 0 ? -rowToRemove : 0;


        //alternate between the two
        boolean horizontal = rowToRemove > 0;
        //first, remove
        while (colsToRemove > 0 || rowToRemove > 0) {
            if (horizontal) {
                bi = rotate(bi, true);
            }
            Double[][] em = Energy.calcEnergy(bi, energyType);
            Double[][] seamMap = Seam.getSeamMap(em);
            int[] seam = Seam.getSeam(seamMap);
            // int[] seam = Seam.getStraightSeam(em);
            bi = Seam.cutSeam(bi, seam);
            if (horizontal) {
                bi = rotate(bi, false);
                rowToRemove--;
                if (colsToRemove > 0)
                    horizontal = false;
            } else {
                colsToRemove--;
                if (rowToRemove > 0)
                    horizontal = true;
            }
        }
        //now add cols
        //first copy everything to the one we are reducing


        int maxAdd = bi.getWidth() / 2;
        while (colsToAdd > 0) {
            BufferedImage tmp = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < bi.getHeight(); y++)
                for (int x = 0; x < bi.getWidth(); x++)
                    tmp.setRGB(x, y, bi.getRGB(x, y));

            List<int[]> seams = new ArrayList<>();
            int colsAdded = 0;
            while (colsToAdd > 0 && colsAdded < maxAdd) {
                //find seam to remove from tmp
                Double[][] em = Energy.calcEnergy(tmp, energyType);
                Double[][] seamMap = Seam.getSeamMap(em);
                int[] seam = Seam.getSeam(seamMap);
                //add it to bi
                bi = Seam.addSeam(seams, bi, seam,true);
                seams.add(seam);
                tmp = Seam.cutSeam(tmp, seam);
                colsToAdd--;
                colsAdded++;
            }
        }
        //now add the rows
        bi = rotate(bi,true);
        maxAdd = bi.getWidth() / 2;
        while (rowsToAdd > 0) {
            BufferedImage tmp = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_RGB);
            for (int y = 0; y < bi.getHeight(); y++)
                for (int x = 0; x < bi.getWidth(); x++)
                    tmp.setRGB(x, y, bi.getRGB(x, y));

            List<int[]> seams = new ArrayList<>();
            int rowsAdded = 0;
            while (rowsToAdd > 0 && rowsAdded < maxAdd) {
                //find seam to remove from tmp
                Double[][] em = Energy.calcEnergy(tmp, energyType);
                Double[][] seamMap = Seam.getSeamMap(em);
                int[] seam = Seam.getSeam(seamMap);
                //add it to bi
                bi = Seam.addSeam(seams, bi, seam,true);
                seams.add(seam);
                tmp = Seam.cutSeam(tmp, seam);
                rowsToAdd--;
                rowsAdded++;
            }
        }
        bi = rotate(bi,false);

        ImageIO.write(bi, "JPEG", new File(output));
    }

    private static BufferedImage rotate(BufferedImage bi, boolean clockwise) throws Exception {
        BufferedImage rotated = new BufferedImage(bi.getHeight(), bi.getWidth(), BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < rotated.getHeight(); y++) {
            for (int x = 0; x < rotated.getWidth(); x++) {
                if (clockwise) {
                    rotated.setRGB(x, y, bi.getRGB(y, bi.getHeight() - 1 - x));
                } else {
                    rotated.setRGB(x, y, bi.getRGB(bi.getWidth() - 1 - y, x));
                }
            }
        }

        return rotated;
    }
}

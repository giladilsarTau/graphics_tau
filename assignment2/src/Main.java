import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Main {


    private static boolean hasAllargs(String[] args) {
        return args.length >= 5;
    }

    public static void main(String[] args) throws Exception {

        String input = hasAllargs(args) ? args[1] : "blue_car_mini.jpg";
        BufferedImage bi = ImageIO.read(new File(input));

        int numOfCols = hasAllargs(args) ? Integer.parseInt(args[2]) : (int) (bi.getWidth() * 0.5);
        int numOfRows = hasAllargs(args) ? Integer.parseInt(args[3]) : (int) (bi.getHeight() * 0.5);
        int energyType = hasAllargs(args) ? Integer.parseInt(args[4]) : 1;
        String output = hasAllargs(args) ? args[5] : "new_blue_car.jpg";


        int colsToRemove = bi.getWidth() - numOfCols;
        int rowToRemove = bi.getHeight() - numOfRows;

        //alternate between the two
        boolean horizontal = numOfCols > 0;
        while (colsToRemove != 0 || rowToRemove != 0) {
            if (horizontal) {
                bi = rotate(bi, true);
            }
            Double[][] em = Energy.calcEnergy(bi, energyType);
            Double[][] seamMap = Seam.getSeamMap(em);
            int[] seam = Seam.getSeam(seamMap);
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

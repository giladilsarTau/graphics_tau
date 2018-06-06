
package RayTracing;

import SceneDataObjs.*;
import Utils.ColorUtils;
import Utils.Hit;

import java.awt.*;
import java.awt.color.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * Main class for ray tracing exercise.
 */
public class RayTracer {

    public static int imageWidth;
    public static int imageHeight;

    static Scene scene = new Scene();

    /**
     * Runs the ray tracer. Takes scene file, output image file and image size as input.
     */
    public static void main(String[] args) throws Exception {

//        try {

        RayTracer tracer = new RayTracer();

        // Default values:
        tracer.imageWidth = 500;
        tracer.imageHeight = 500;

        //  if (args.length < 2)
        //    throw new RayTracerException("Not enough arguments provided. Please specify an input scene file and an output image file for rendering.");

        String sceneFileName = args.length >= 2 ? args[0] : "scenes\\Transparency.txt";
        String outputFileName = args.length >= 2 ? args[1] : "bla.jpg";

        //   if (args.length > 3) {
        //     tracer.imageWidth = Integer.parseInt(args[2]);
        //tracer.imageHeight = Integer.parseInt(args[3]);
        //}


        scene.heightPixels = imageHeight;
        scene.widthPixels = imageWidth;

        // Parse scene file:
        tracer.parseScene(sceneFileName);

        // Render scene:
        tracer.renderScene(outputFileName);

//      } catch (IOException e) {
//          System.out.println(e.getMessage());
//        } catch (RayTracerException e) {
//            System.out.println(e.getMessage());
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }


    }

    /**
     * Parses the scene file and creates the scene. Change this function so it generates the required objects.
     */
    public void parseScene(String sceneFileName) throws IOException, RayTracerException {
        FileReader fr = new FileReader(sceneFileName);

        BufferedReader r = new BufferedReader(fr);
        String line = null;
        int lineNum = 0;
        System.out.println("Started parsing scene file " + sceneFileName);


        while ((line = r.readLine()) != null) {
            line = line.trim();
            ++lineNum;

            if (line.isEmpty() || (line.charAt(0) == '#')) {  // This line in the scene file is a comment
                continue;
            } else {
                String code = line.substring(0, 3).toLowerCase();
                // Split according to white space characters:
                String[] params = line.substring(3).trim().toLowerCase().split("\\s+");

                if (code.equals("cam")) {
                    // Add code here to parse camera parameters

                    Camera c = new Camera(params);
                    scene._cam = c;

                    System.out.println(String.format("Parsed camera parameters (line %d)", lineNum));
                } else if (code.equals("set")) {
                    // Add code here to parse general settings parameters

                    scene.ParseSceneSettings(params);

                    System.out.println(String.format("Parsed general settings (line %d)", lineNum));
                } else if (code.equals("mtl")) {
                    // Add code here to parse material parameters

                    Material m = new Material(params);
                    scene._sceneMaterials.add(m);

                    System.out.println(String.format("Parsed material (line %d)", lineNum));
                } else if (code.equals("sph")) {
                    // Add code here to parse sphere parameters

                    // Example (you can implement this in many different ways!):
                    // Sphere sphere = new Sphere();
                    // sphere.setCenter(params[0], params[1], params[2]);
                    // sphere.setRadius(params[3]);
                    // sphere.setMaterial(params[4]);
                    Sphere sphere = new Sphere(new Point3D(params[0], params[1], params[2]), Double.parseDouble(params[3]));
                    sphere.material = Integer.parseInt(params[4]);

                    scene._sceneSurfaces.add(sphere);
                    System.out.println(String.format("Parsed sphere (line %d)", lineNum));
                } else if (code.equals("pln")) {
                    // Add code here to parse plane parameters

                    Plane plane = new Plane(new Vector3D(params[0], params[1], params[2]), Double.parseDouble(params[3]));
                    plane.material = Integer.parseInt(params[4]);
                    scene._sceneSurfaces.add(plane);

                    System.out.println(String.format("Parsed plane (line %d)", lineNum));
                } else if (code.equals("trg")) {
                    // Add code here to parse plane parameters

                    Point3D v1 = new Point3D(params[0], params[1], params[2]);
                    Point3D v2 = new Point3D(params[3], params[4], params[5]);
                    Point3D v3 = new Point3D(params[6], params[7], params[8]);

                    Triangle triangle = new Triangle(v1, v2, v3);
                    triangle.material = Integer.parseInt(params[9]);
                    scene._sceneSurfaces.add(triangle);

                    System.out.println(String.format("Parsed triangle (line %d)", lineNum));
                } else if (code.equals("lgt")) {
                    // Add code here to parse light parameters

                    LightSource l = new LightSource(params);
                    scene._sceneLights.add(l);
                    System.out.println(String.format("Parsed light (line %d)", lineNum));
                } else {
                    System.out.println(String.format("ERROR: Did not recognize object: %s (line %d)", code, lineNum));
                }
            }
        }

        // It is recommended that you check here that the scene is valid,
        // for example camera settings and all necessary materials were defined.

        System.out.println("Finished parsing scene file " + sceneFileName);

    }

    /**
     * Renders the loaded scene and saves it to the specified file location.
     */
    public void renderScene(String outputFileName) {
        long startTime = System.currentTimeMillis();

        // Create a byte array to hold the pixel data:
        byte[] rgbData = new byte[this.imageWidth * this.imageHeight * 3];


        // Put your ray tracing code here!
        //
        // Write pixel color values in RGB format to rgbData:
        // Pixel [x, y] red component is in rgbData[(y * this.imageWidth + x) * 3]
        //            green component is in rgbData[(y * this.imageWidth + x) * 3 + 1]
        //             blue component is in rgbData[(y * this.imageWidth + x) * 3 + 2]
        //
        // Each of the red, green and blue components should be a byte, i.e. 0-255


        for (int h = 0; h < imageHeight; h++) {
            for (int w = 0; w < imageWidth; w++) {
                //super sampling loop
                List<Hit> closet_hits = new ArrayList<>();
                for (int ySS = 1; ySS <= scene._superSumpelingLevel; ySS++) {
                    for (int xSS = 1; xSS <= scene._superSumpelingLevel; xSS++) {
                        //cast ray. the .0 is required for enforcing doubles without rounding
                        Ray ray = new Ray(scene, w + ((2.0 * xSS - 1) / (scene._superSumpelingLevel * 2)),
                                h + ((2.0 * ySS - 1) / (scene._superSumpelingLevel * 2)));
                        List<Hit> hits = new ArrayList<>();
                        for (ISurface surface : scene._sceneSurfaces) {
                            hits.add(new Hit(surface.rayIntersection(ray), surface, ray));
                        }

                        closet_hits.add(Hit.findClosest(hits, scene, scene._cam._camPosition));

                    }
                }

                //for each hit, find color
                List<Color> colors = new ArrayList<>();
                for (Hit closest : closet_hits) {
                    Color c = getColor(closest, scene); //diffuse and specular
                    if (closest != null) {
                        c = transperecycalc(closest, scene, c); //transparency
                        c = ColorUtils.plus(c,getReflectionColor(closest,scene,scene._maxRecursions));
                    }
                    colors.add(c);
                }

                Color avg = ColorUtils.avgColor(colors);
                rgbData[(h * this.imageWidth + w) * 3] = (byte) avg.getRed();
                rgbData[(h * this.imageWidth + w) * 3 + 1] = (byte) avg.getGreen();
                rgbData[(h * this.imageWidth + w) * 3 + 2] = (byte) avg.getBlue();

            }
        }


        long endTime = System.currentTimeMillis();
        Long renderTime = endTime - startTime;

        // The time is measured for your own conveniece, rendering speed will not affect your score
        // unless it is exceptionally slow (more than a couple of minutes)
        System.out.println("Finished rendering scene in " + renderTime.toString() + " milliseconds.");

        // This is already implemented, and should work without adding any code.
        saveImage(this.imageWidth, rgbData, outputFileName);

        System.out.println("Saved file " + outputFileName);

    }

    public Color getColor(Hit closest, Scene scene) {
        Color c = Color.black;
        if (closest == null) // hit background
            c = scene._backgroundColor;
        else {

            //color getter
            try {
                //c = closest.surface.getColor(ray, scene); //diffuse
                for (LightSource lightSource : scene._sceneLights) {
                    c = ColorUtils.plus(c, lightSource.colorFromlightSource(closest, scene));
                }
            } catch (Exception e) {
                c = Color.BLACK;
                System.out.println(Arrays.toString(e.getStackTrace()));
            }
        }
        return c;
    }


    public Color transperecycalc(Hit hit, Scene scene, Color color) {
        if (hit == null) // background hit
            return color;
        if (hit.surface.getMaterial(scene)._transparency == 0) {
            return color;
        }
        //object is transparent -> therefore it color multipied by  1-trans and we add color from behind
        color = ColorUtils.mult(color, (float) (1 - hit.surface.getMaterial(scene)._transparency));
        //shoot ray behind
        Ray behindRay = new Ray(hit.hitPoint, hit.origRay);

        List<Hit> hits = new ArrayList<>();
        for (ISurface surface : scene._sceneSurfaces) {
            if (surface != hit.surface)
                hits.add(new Hit(surface.rayIntersection(behindRay), surface, behindRay));
        }

        Hit behind = Hit.findClosest(hits, scene, hit.hitPoint);


        //colors for the behind
        Color behindColor = getColor(behind, scene);

        //recursive
        behindColor = transperecycalc(behind, scene, behindColor);

        //we got the behind, mult and recursive
        behindColor = ColorUtils.mult(behindColor, (float) hit.surface.getMaterial(scene)._transparency);


        //add
        color = ColorUtils.plus(color, behindColor);

        return color;
    }


   public Color getReflectionColor(Hit hit, Scene scene, int recursion){
        //is reflective?
       if(hit == null)
           return Color.BLACK;
       Material mat = hit.surface.getMaterial(scene);
       if(mat._reflectionColor == Color.BLACK)
           return Color.BLACK;
       if(recursion == 0) // we are deep enough in the reflection recursion
           return Color.BLACK;
       //we are reflective, first get the reflection ray
       Vector3D N = hit.surface.getNormal(hit.hitPoint);
       double dot = 2.0 * (hit.origRay.dot(N));
       Vector3D Ndot = N.mult(dot);
       Vector3D reflection = hit.origRay.minus(Ndot);
       Ray reflectionRay  = new Ray(hit.hitPoint,reflection);

       //get the color pipeline

       List<Hit> hits = new ArrayList<>();
       for (ISurface surface : scene._sceneSurfaces) {
           if (surface != hit.surface)
               hits.add(new Hit(surface.rayIntersection(reflectionRay), surface, reflectionRay));
       }

       Hit reflectiveHit = Hit.findClosest(hits, scene, hit.hitPoint);
       //get color
       Color reflectColor = getColor(reflectiveHit,scene);
       //add transperency
       reflectColor = transperecycalc(reflectiveHit,scene,reflectColor);
       //recursivly add reflection color
       reflectColor = ColorUtils.plus(reflectColor,getReflectionColor(reflectiveHit,scene,recursion-1));
       //multiply by reflecion color
       reflectColor = ColorUtils.mult(reflectColor,mat._reflectionColor);
       return reflectColor;
   }


    //////////////////////// FUNCTIONS TO SAVE IMAGES IN PNG FORMAT //////////////////////////////////////////

    /*
     * Saves RGB data as an image in png format to the specified location.
     */
    public static void saveImage(int width, byte[] rgbData, String fileName) {
        try {

            BufferedImage image = bytes2RGB(width, rgbData);
            ImageIO.write(image, "png", new File(fileName));

        } catch (IOException e) {
            System.out.println("ERROR SAVING FILE: " + e.getMessage());
        }

    }

    /*
     * Producing a BufferedImage that can be saved as png from a byte array of RGB values.
     */
    public static BufferedImage bytes2RGB(int width, byte[] buffer) {
        int height = buffer.length / width / 3;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
        ColorModel cm = new ComponentColorModel(cs, false, false,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }

    public static class RayTracerException extends Exception {
        public RayTracerException(String msg) {
            super(msg);
        }
    }


}

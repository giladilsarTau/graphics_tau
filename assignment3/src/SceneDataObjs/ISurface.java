package SceneDataObjs;

import java.awt.*;

public interface ISurface {

    Point3D rayIntersection(Ray ray);

    Color getColor(Ray ray, Scene scene);

}

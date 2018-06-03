package SceneDataObjs;

import java.awt.*;

public interface ISurface {

    Point3D rayIntersection(Ray ray);

    Color getColor(Ray ray, Scene scene);

    Material getMaterial(Scene scene);

    Vector3D getNormal(Point3D point);
}

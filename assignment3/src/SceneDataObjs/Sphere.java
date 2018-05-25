package SceneDataObjs;

import java.awt.*;

public class Sphere implements ISurface {
    public int material;
    public Point3D center;
    public Double radius;

    public Sphere(Point3D center, Double radius){
        this.radius = radius;
        this.center = center;
    }

    @Override
    public Point3D rayIntersection(Ray ray) {
        double discA = Math.pow(ray.dot(ray.startP.minus(center)),2);
        double discB = ray.dot(ray)*(ray.startP.L2Square(center) - Math.pow(radius,2));
        double disc = discA - discB;
        if(disc < 0)
            return null;

        double voc = ray.mult(-1).dot(ray.startP.minus(center));
        double t1 = (voc + Math.sqrt(disc))/ ray.dot(ray);
        double t2 = (voc - Math.sqrt(disc))/ ray.dot(ray);

        if(t1 < 0 && t2 < 0)
            return null;
        else if(t1 <0)
            return ray.tPointOnRay(t2);
        else if (t2 < 0)
            return ray.tPointOnRay(t1);
        else
            return ray.tPointOnRay(Math.min(t1,t2));
    }
    @Override
    public Color getColor(Ray ray, Scene scene) {
        return scene._sceneMaterials.get(material-1)._diffuseColor;
    }
}

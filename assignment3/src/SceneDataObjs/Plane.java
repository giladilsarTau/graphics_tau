package SceneDataObjs;

import java.awt.*;

public class Plane implements ISurface {
    public Vector3D normal;
    public Double offset;
    public int material;

    public Plane(Vector3D v, Double offset){
        this.normal = v; this.offset = offset;
    }

    @Override
    public Point3D rayIntersection(Ray ray) {
        Double nRd = normal.dot(ray);
        if(Math.abs(nRd) < 0.001)
            return null;
        Double nR0 = normal.dot(ray.startP);
        Double t= -1 * (offset + nR0) / nRd;
        if(t < 0)
            return null;
        return ray.tPointOnRay(t);
    }

    @Override
    public Color getColor(Ray ray, Scene scene) {
        return scene._sceneMaterials.get(material -1)._diffuseColor;
    }

    @Override
    public Material getMaterial(Scene scene) {
        return scene._sceneMaterials.get(material -1);
    }

    @Override
    public Vector3D getNormal(Point3D point) {
        return normal;
    }
}

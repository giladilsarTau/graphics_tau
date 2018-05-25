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
        if(nRd == 0)
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
}

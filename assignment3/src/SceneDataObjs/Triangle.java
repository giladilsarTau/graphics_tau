package SceneDataObjs;

import java.awt.*;

public class Triangle implements ISurface {
    public Point3D _v1;
    public Point3D _v2;
    public Point3D _v3;
    public int material;

    public Triangle(Point3D _v1, Point3D _v2, Point3D _v3) {
        this._v1 = _v1;
        this._v2 = _v2;
        this._v3 = _v3;
    }

    @Override
    public Point3D rayIntersection(Ray ray) {

        Vector3D edge1 = Vector3D.vectorFromPoints(_v2, _v1);
        Vector3D edge2 = Vector3D.vectorFromPoints(_v3, _v1);
        Vector3D h = ray.cross(edge2);
        double a = edge1.dot(h);
        if(a == 0)
            return null;
        double f = 1 / a;
        Vector3D s = Vector3D.vectorFromPoints(ray.startP,_v1);
        double u = f * (s.dot(h));
        if(u < 0 || u > 1)
            return null;
        Vector3D q = s.cross(edge1);
        double v = f * ray.dot(q);
        if(v < 0 || u + v > 1)
            return null;
        double t = f * edge2.dot(q);
        if(t < 0)
            return null;
        else
            return ray.tPointOnRay(t);
    }
    @Override
    public Color getColor(Ray ray, Scene scene) {
        return scene._sceneMaterials.get(material-1)._diffuseColor;
    }
}

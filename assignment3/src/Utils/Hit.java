package Utils;

import SceneDataObjs.ISurface;
import SceneDataObjs.Point3D;
import SceneDataObjs.Ray;
import SceneDataObjs.Scene;

import java.util.List;

public class Hit {
    public Point3D hitPoint;
    public ISurface surface;
    public Ray origRay;

    public Hit(Point3D hitPoint, ISurface surface, Ray ray) {
        this.hitPoint = hitPoint;
        this.surface = surface;
        this.origRay = ray;
    }

    public static Hit findClosest(List<Hit> hits, Scene scene, Point3D originPoint) {
        double dist = Double.MAX_VALUE;
        Hit res = null;
        for (Hit hit : hits) {
            if (hit.hitPoint == null)
                continue;
            double d = originPoint.L2Square(hit.hitPoint);
            if (d < dist) {
                dist = d;
                res = hit;
            }
        }
        return res;
    }

    private static int numNotNull(List<Hit> hits) {
        int count = 0;
        for (Hit h : hits)
            if (h.hitPoint != null)
                count++;
        return count;
    }
}

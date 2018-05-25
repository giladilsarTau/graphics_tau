package Utils;

import SceneDataObjs.ISurface;
import SceneDataObjs.Point3D;
import SceneDataObjs.Scene;

import java.util.List;

public class Hit {
    public Point3D hitPoint;
    public ISurface surface;

    public Hit(Point3D hitPoint, ISurface surface) {
        this.hitPoint = hitPoint;
        this.surface = surface;
    }

    public static Hit findClosest(List<Hit>hits, Scene scene){
        double dist = Double.MAX_VALUE;
        Hit res = null;
        for(Hit hit: hits){
            if(hit.hitPoint == null)
                continue;
            double d = scene._cam._camPosition.L2Square(hit.hitPoint);
            if(d < dist){
                dist = d;
                res = hit;
            }
        }
        return res;
    }
}

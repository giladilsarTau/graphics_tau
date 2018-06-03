package SceneDataObjs;

import Utils.ColorUtils;
import Utils.Hit;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class LightSource {
    public Point3D _position;
    public Color _lightColor;
    public double _specularIntensity;
    public double _shadowIntensity;
    public double _lightRadius;

    public LightSource(String[] lightData) {
        _position = new Point3D(Double.parseDouble(lightData[0]), Double.parseDouble(lightData[1]), Double.parseDouble(lightData[2]));
        _lightColor = new Color(Float.parseFloat(lightData[3]), Float.parseFloat(lightData[4]), Float.parseFloat(lightData[5]), 0);
        _specularIntensity = Double.parseDouble(lightData[6]);
        _shadowIntensity = Double.parseDouble(lightData[7]);
        _lightRadius = Double.parseDouble(lightData[8]);
    }

    public Color colorFromlightSource(Hit hit, Scene scene) {
        Ray lightRay = new Ray(_position, Vector3D.vectorFromPoints(hit.hitPoint,_position));

        List<Hit> hits = new ArrayList<>();
        for (ISurface surface : scene._sceneSurfaces) {
            hits.add(new Hit(surface.rayIntersection(lightRay), surface));
        }
        Hit lightHit = Hit.findClosest(hits, scene, _position);

        if (lightHit.surface.equals(hit.surface)) {
            Color t = ColorUtils.mult(lightHit.surface.getMaterial(scene)._diffuseColor, _lightColor);
            float dot = (float) lightRay.dot(lightHit.surface.getNormal(hit.hitPoint));
            if(dot > 0.001)
                t = ColorUtils.mult(t, dot);
            return t;
        } else {
            return Color.black;
        }
    }
}

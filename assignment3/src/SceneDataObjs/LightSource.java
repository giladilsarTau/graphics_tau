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
        Ray lightRay = new Ray(_position, Vector3D.vectorFromPoints(hit.hitPoint, _position));

        lightRay = lightRay.normalize();

        List<Hit> hits = new ArrayList<>();
        for (ISurface surface : scene._sceneSurfaces) {
            hits.add(new Hit(surface.rayIntersection(lightRay), surface, lightRay));
        }
        Hit lightHit = Hit.findClosest(hits, scene, _position);

        if(lightHit == null)
            return Color.BLACK;
        float shadowFactor = 1;
        boolean isHit = true;
        if (!lightHit.surface.equals(hit.surface)) {
            shadowFactor = (float) (1 - this._shadowIntensity);
            isHit = false;
            //shodowFacotr = 0;
        }

        Color t = ColorUtils.mult(lightHit.surface.getMaterial(scene)._diffuseColor,_lightColor);
        //  t = ColorUtils.mult(t,(float)this._specularIntensity);
        float dot = (float) lightRay.dot(lightHit.surface.getNormal(hit.hitPoint));

        t = ColorUtils.mult(t, Math.max(dot, 0));

        if(isHit)
            t = ColorUtils.plus(t, this.getSpectralColor(lightHit, hit, lightRay, scene));

        return ColorUtils.mult(t,shadowFactor);
//        } else {
//            return Color.black;
//        }
    }

    private Color getSpectralColor(Hit lightHit, Hit origHit, Ray lightRay, Scene scene) {
        //get Highlight vector from the formula : R = (2L.N)N-L
        Vector3D N = lightHit.surface.getNormal(origHit.hitPoint);
        double myDot = N.dot(lightRay);
        double myLen = 2.0 * myDot;

        Vector3D reflect = N.mult(myLen).minus(lightRay).normalize();

        //spectral color is Ks * Ip * (R.V)^N
        double mySpec = Math.max(reflect.dot(lightRay), 0);

        Material mat = lightHit.surface.getMaterial(scene);
        mySpec = Math.pow(mySpec, mat._phongSpecularityCoefficient);

        Color c= ColorUtils.mult(mat._specularColor, (float) (mySpec * this._specularIntensity));
        return c;

    }
}

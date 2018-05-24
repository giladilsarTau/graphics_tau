package SceneDataObjs;

import java.awt.Color;

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
}

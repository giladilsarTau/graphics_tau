package SceneDataObjs;

import java.awt.Color;

public class LightSource {
	public Point3D _position;
	public Color _lightColor;
	public double _specularIntensity;
	public double _shadowIntensity;
	public double _lightRadius;
	
	public LightSource(String[] lightData) {
		_position = new Point3D(Integer.parseInt(lightData[1]), Integer.parseInt(lightData[2]), Integer.parseInt(lightData[3]));
		_lightColor = new Color(Integer.parseInt(lightData[4]), Integer.parseInt(lightData[5]), Integer.parseInt(lightData[6]), 0);
		_specularIntensity = Double.parseDouble(lightData[7]);
		_shadowIntensity = Double.parseDouble(lightData[8]);
		_lightRadius = Double.parseDouble(lightData[9]);
	}
}

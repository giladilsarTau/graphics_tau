package SceneDataObjs;

import java.awt.Color;

public class Material {

	public Color _diffuseColor;
	public Color _specularColor;
	public Color _reflectionColor;
	public double _phongSpecularityCoefficient;
	public double _transparency;
	
	public Material(String[] materialData) {
		_diffuseColor = new Color(Integer.parseInt(materialData[1]), Integer.parseInt(materialData[2]), Integer.parseInt(materialData[3]), 0);
		_specularColor = new Color(Integer.parseInt(materialData[4]), Integer.parseInt(materialData[5]), Integer.parseInt(materialData[6]), 0);
		_reflectionColor = new Color(Integer.parseInt(materialData[7]), Integer.parseInt(materialData[8]), Integer.parseInt(materialData[9]), 0);
		_phongSpecularityCoefficient = Double.parseDouble(materialData[10]);
		_transparency = Double.parseDouble(materialData[11]);
	}
}

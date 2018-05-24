package SceneDataObjs;

import java.awt.Color;

public class Material {

	public Color _diffuseColor;
	public Color _specularColor;
	public Color _reflectionColor;
	public double _phongSpecularityCoefficient;
	public double _transparency;
	
	public Material(String[] materialData) {
		_diffuseColor = new Color(Float.parseFloat(materialData[0]), Float.parseFloat(materialData[1]), Float.parseFloat(materialData[2]), 0);
		_specularColor = new Color(Float.parseFloat(materialData[3]), Float.parseFloat(materialData[4]), Float.parseFloat(materialData[5]), 0);
		_reflectionColor = new Color(Float.parseFloat(materialData[6]), Float.parseFloat(materialData[7]), Float.parseFloat(materialData[8]), 0);
		_phongSpecularityCoefficient = Double.parseDouble(materialData[9]);
		_transparency = Double.parseDouble(materialData[10]);
	}
}

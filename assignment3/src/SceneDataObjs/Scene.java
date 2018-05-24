package SceneDataObjs;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Scene {
	public Color _backgroundColor;
	public int _rootNumOfShadowRays;
	public int _maxRecursions;
	public int _superSumpelingLevel;
	
	public Camera _cam;
	public List<Material> _sceneMaterials;
	public List<ISurface> _sceneSurfaces;
	public List<LightSource> _sceneLights;
	
	public Scene() {
		_sceneMaterials = new ArrayList<Material>();
		_sceneSurfaces = new ArrayList<ISurface>();
		_sceneLights = new ArrayList<LightSource>();
	}
	
	public void ParseSceneSettings(String[] sceneSettings) {
		_backgroundColor = new Color(Float.parseFloat(sceneSettings[0]), Float.parseFloat(sceneSettings[1]), Float.parseFloat(sceneSettings[2]), 0);
		_rootNumOfShadowRays = Integer.parseInt(sceneSettings[3]);
		_maxRecursions = Integer.parseInt(sceneSettings[4]);
		_superSumpelingLevel = Integer.parseInt(sceneSettings[5]);
	}
}

package graphics_tau_assigment3;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidParameterException;

import SceneDataObjs.Camera;
import SceneDataObjs.LightSource;
import SceneDataObjs.Material;
import SceneDataObjs.Scene;

public class SceneParser {
	public static Scene ParseSenceFile(String filePath) {
		Scene scene = new Scene();
		BufferedReader reader = null;
		
		try {
		    reader = new BufferedReader(new FileReader(filePath));
		    String text = null;

		    while ((text = reader.readLine()) != null) {
		    	ParseSceneLine(scene, text);
		    }
		} catch (Exception e) {
		    e.printStackTrace();
		    return null;
		} finally {
		    try {
		        if (reader != null) {
		            reader.close();
		        }
		    } catch (IOException e) {
		    	e.printStackTrace();
		    	return null;
		    }
		}
		
		return scene;
	}
	
	public static void ParseSceneLine(Scene scene, String line) {
		String[] lineData = line.split(" ");
		
		//Switch case for the first String
		switch(lineData[0]) {
		case "cam":
			scene._cam = new Camera(lineData);
			break;
		case "set":
			scene.ParseSceneSettings(lineData);
			break;
		case "mtl":
			scene._sceneMaterials.add(new Material(lineData));
			break;
		case "sph":
			break;
		case "pln":
			break;
		case "trg":
			break;
		case "lgt":
			scene._sceneLights.add(new LightSource(lineData));
			break;
		default:
			throw new InvalidParameterException("invalid file format");
		}
	}
}

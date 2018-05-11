package SceneDataObjs;

public class Camera {
	public Point3D _camPosition;
	public Point3D _lookAtposition;
	public Vector3D _upVector;
	public double _screenDistance;
	public double _screenWidth;
	
	public Camera(String[] cameraData) {
		_camPosition = new Point3D(Integer.parseInt(cameraData[1]), Integer.parseInt(cameraData[2]), Integer.parseInt(cameraData[3]));
		_lookAtposition = new Point3D(Integer.parseInt(cameraData[4]), Integer.parseInt(cameraData[5]), Integer.parseInt(cameraData[6]));
		_upVector = new Vector3D(Integer.parseInt(cameraData[7]), Integer.parseInt(cameraData[8]), Integer.parseInt(cameraData[9]));
		_screenDistance = Double.parseDouble(cameraData[10]);
		_screenWidth = Double.parseDouble(cameraData[11]);
	}
	
}

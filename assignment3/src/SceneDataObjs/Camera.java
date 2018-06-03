package SceneDataObjs;

public class Camera {
	public Point3D _camPosition;
	public Point3D _lookAtposition;
	public Vector3D _upVector;
	public Vector3D vpUp;
	public double _screenDistance;
	public double _screenWidth;
	
	public Camera(String[] cameraData) {
		_camPosition = new Point3D(Double.parseDouble(cameraData[0]), Double.parseDouble(cameraData[1]), Double.parseDouble(cameraData[2]));
		_lookAtposition = new Point3D(Double.parseDouble(cameraData[3]), Double.parseDouble(cameraData[4]), Double.parseDouble(cameraData[5]));
		_upVector = new Vector3D(Double.parseDouble(cameraData[6]), Double.parseDouble(cameraData[7]), Double.parseDouble(cameraData[8]));
		_screenDistance = Double.parseDouble(cameraData[9]);
		_screenWidth = Double.parseDouble(cameraData[10]);
	}
	
}

package SceneDataObjs;

public class Vector3D {
    public double _x;
    public double _y;
    public double _z;

    public Vector3D(double x, double y, double z) {
        _x = x;
        _y = y;
        _z = z;
    }

    public Vector3D(String x, String y, String z) {
        _x = Double.parseDouble(x);
        _y = Double.parseDouble(y);
        _z = Double.parseDouble(z);
    }
}

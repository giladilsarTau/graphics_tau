package SceneDataObjs;

public class Point3D {
    public double _x;
    public double _y;
    public double _z;

    public Point3D(Double x, Double y, Double z) {
        _x = x;
        _y = y;
        _z = z;
    }

    public Point3D(String x, String y, String z) {
        _x = Double.parseDouble(x);
        _y = Double.parseDouble(y);
        _z = Double.parseDouble(z);
    }
}

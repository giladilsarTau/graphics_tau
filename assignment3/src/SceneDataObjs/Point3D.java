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

    public Point3D minus(Point3D o){
        return new Point3D(this._x - o._x, this._y - o._y, this._z - o._z);
    }

    public Point3D add(Point3D o){
        return new Point3D(this._x + o._x, this._y + o._y, this._z + o._z);
    }
    public Double L2Square(Point3D o){
        double x = Math.pow(this._x - o._x, 2);
        double y = Math.pow(this._y - o._y, 2);
        double z = Math.pow(this._z - o._z, 2);
        return x + y + z;
    }
}

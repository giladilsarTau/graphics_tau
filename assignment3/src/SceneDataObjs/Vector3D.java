package SceneDataObjs;

public class Vector3D {
    public double _x;
    public double _y;
    public double _z;

    public Vector3D() {
    }


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

    public Point3D toPoint() {
        return new Point3D(this._x, this._y, this._z);
    }

    public Vector3D plus(Vector3D o) {
        return new Vector3D(this._x + o._x, this._y + o._y, this._z + o._z);
    }

    public Vector3D plus(Point3D o) {
        return new Vector3D(this._x + o._x, this._y + o._y, this._z + o._z);
    }

    public Vector3D plus(double o) {
        return new Vector3D(this._x + o, this._y + o, this._z + o);
    }

    public Vector3D minus(Vector3D o) {
        return new Vector3D(this._x - o._x, this._y - o._y, this._z - o._z);

    }

    public Vector3D minus(double o) {
        return new Vector3D(this._x - o, this._y - o, this._z - o);
    }

    public double dot(Vector3D o) {
        return this._x * o._x + this._y * o._y + this._z * o._z;
    }

    public double dot(Point3D o) {
        return this._x * o._x + this._y * o._y + this._z * o._z;
    }

    public Vector3D cross(Vector3D o) {
        Double x = this._y * o._z - this._z * o._y;
        Double y = this._z * o._x - this._x * o._z;
        Double z = this._x * o._y - this._y * o._x;
        return new Vector3D(x, y, z);
    }

    public Vector3D mult(double o) {

        return new Vector3D(this._x * o, this._y * o, this._z * o);
    }

    public Vector3D normalize() {
        return this.mult(1 / Math.sqrt(this.dot(this)));
    }

    public static Vector3D vectorFromPoints(Point3D a, Point3D b) {
        return new Vector3D(a._x - b._x, a._y - b._y, a._z - b._z);
    }

}

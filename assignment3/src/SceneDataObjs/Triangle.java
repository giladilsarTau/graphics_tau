package SceneDataObjs;

public class Triangle implements ISurface {
    public Vector3D _v1;
    public Vector3D _v2;
    public Vector3D _v3;
    public int material;

    public Triangle(Vector3D _v1, Vector3D _v2, Vector3D _v3) {
        this._v1 = _v1;
        this._v2 = _v2;
        this._v3 = _v3;
    }
}

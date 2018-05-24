package SceneDataObjs;

public class Plane implements ISurface {
    public Vector3D normal;
    public int offset;
    public int material;

    public Plane(Vector3D v, int offset){
        this.normal = v; this.offset = offset;
    }
}

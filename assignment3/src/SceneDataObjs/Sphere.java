package SceneDataObjs;

public class Sphere implements ISurface {
    public int material;
    public Point3D center;
    public int radius;

    public Sphere(Point3D center, int radius){
        this.radius = radius;
        this.center = center;
    }

}

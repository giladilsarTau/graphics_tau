package SceneDataObjs;

public class Ray extends Vector3D {


    public Point3D startP;
//
//    public Ray (Scene scene, int xPixel, int yPixel){
//        double aspectRatio = (double)scene.heightPixels / (double)scene.widthPixels;
//        int screenHeightReal = (int)(scene._cam._screenWidth / aspectRatio);
//        Vector3D heightRayVector =   scene._cam._upVector.mult(((yPixel+0.5) / scene.heightPixels) * screenHeightReal);
//
//        Vector3D w = Vector3D.vectorFromPoints(scene._cam._lookAtposition,scene._cam._camPosition);
//        Vector3D v = w.cross(scene._cam._upVector);
//        Vector3D widthRayVector = v.mult(((xPixel+0.5) / scene.widthPixels) * scene._cam._screenWidth);
//
//        this._x = -scene._cam._camPosition._x + heightRayVector._x + widthRayVector._x;
//        this._y = -scene._cam._camPosition._y + heightRayVector._y + widthRayVector._y;
//        this._z = -scene._cam._camPosition._z + heightRayVector._z + widthRayVector._z;
//
//        startP = scene._cam._camPosition;
//    }

    public Ray(Scene scene, int xPixel, int yPixel) {
        double aspectRatio = (double) scene.heightPixels / (double) scene.widthPixels;

        Vector3D upVectorNormal = scene._cam._upVector.normalize();

        double x = ((xPixel + 0.5) - scene.widthPixels / 2) / (scene.widthPixels / 2);
        double y = ((yPixel + 0.5) - scene.heightPixels / 2) / (scene.heightPixels / 2);
        Vector3D w = Vector3D.vectorFromPoints(scene._cam._lookAtposition, scene._cam._camPosition);
        w = w.normalize();
        Vector3D u = w.cross(upVectorNormal);
        Vector3D uNormal = u.normalize();

        Vector3D up = uNormal.cross(w).normalize();

        Vector3D xPart = uNormal.mult(x);
        Vector3D yPart = up.mult(y * aspectRatio);
        Vector3D zPart = w.mult(scene._cam._screenDistance);
        Vector3D r = xPart.plus(yPart).plus(zPart);


        r = r.normalize();

        this._x = r._x;
        this._y = r._y;
        this._z = r._z;


        startP = scene._cam._camPosition;
    }

    public Point3D tPointOnRay(double t) {
        return this.mult(t).plus(this.startP).toPoint();
    }
}

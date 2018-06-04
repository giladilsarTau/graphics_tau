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
//
//    public Ray(Scene scene, int xPixel, int yPixel) {
//        double aspectRatio = (double) scene.heightPixels / (double) scene.widthPixels;
//
//        Vector3D upVectorNormal = scene._cam._upVector.normalize();
//
//        double x = ((xPixel + 0.5) - scene.widthPixels / 2) / (scene.widthPixels / 2);
//        double y = ((yPixel + 0.5) - scene.heightPixels / 2) / (scene.heightPixels / 2);
//        Vector3D w = Vector3D.vectorFromPoints(scene._cam._lookAtposition, scene._cam._camPosition);
//        w = w.normalize();
//        Vector3D u = w.cross(upVectorNormal);
//        Vector3D uNormal = u.normalize();
//
//        Vector3D up = uNormal.cross(w).normalize();
//
////
//        Point3D fovCenter = w.mult(scene._cam._screenDistance).plus(scene._cam._camPosition).toPoint();
//        double upAmount = (fovCenter._y + (scene._cam._screenWidth / 2) * y)*-1;
//        double xAmount = (fovCenter._x + (scene._cam._screenWidth  / 2) * x) * -1;
//
//        Point3D some = new Point3D(xAmount,upAmount,fovCenter._z);
//
//
////        Vector3D xPart = uNormal.mult(x);
////        Vector3D yPart = up.mult(y * aspectRatio);
////        Vector3D zPart = w.mult(scene._cam._screenDistance);
////        Vector3D r = xPart.plus(yPart).plus(zPart);
//
//        Vector3D r = Vector3D.vectorFromPoints(some,scene._cam._camPosition);
//
//        r = r.normalize();
//
//        this._x = r._x;
//        this._y = r._y;
//        this._z = r._z;
//
//
//        startP = scene._cam._camPosition;
//    }


    public Ray(Scene scene, double xPixel, double yPixel) {

        double pixelWidth = scene._cam._screenWidth / scene.widthPixels;
        double pixelHeight = (scene.widthPixels / scene.heightPixels) * pixelWidth;
        double upDistance =   (yPixel - (scene.heightPixels / 2) ) * pixelHeight;
        double rightDistance = (xPixel - (scene.widthPixels / 2) ) * pixelWidth;


        Vector3D w = Vector3D.vectorFromPoints(scene._cam._camPosition,scene._cam._lookAtposition).normalize(); //forwardVec
        Vector3D u = w.cross(scene._cam._upVector).normalize().mult(-1); //rightVec

        scene._cam.vpUp = w.cross(u).normalize();

        Vector3D upMovement = scene._cam.vpUp.mult(upDistance);

        Vector3D rightMovement=u.mult(rightDistance);

        Point3D fovCenter = w.mult(scene._cam._screenDistance).plus(scene._cam._camPosition).toPoint();

        Point3D fov = fovCenter.add(upMovement.toPoint());
        fov = fov.add(rightMovement.toPoint());

        Vector3D r = Vector3D.vectorFromPoints(scene._cam._camPosition,fov).normalize();
        this._x = r._x;
        this._y = r._y;
        this._z = r._z;
        startP = scene._cam._camPosition;
    }


//    public Ray(Scene scene, int xPixel, int yPixel) {
//        xPixel += 0.5;
//        yPixel += 0.5;
//        Vector3D gaze = Vector3D.vectorFromPoints(scene._cam._lookAtposition, scene._cam._camPosition);
//
//        double dist = gaze.getNormalValue();
//        gaze = gaze.normalize();
//
//        Vector3D screenx = gaze.cross(scene._cam._upVector).normalize();
//        Vector3D screeny = screenx.cross(gaze).normalize();
//
//        double tanHalfalphaX = ((scene._cam._screenWidth) / 2) / scene._cam._screenDistance;
//
//
//        double aspectRatio = (double) scene.heightPixels / (double) scene.widthPixels;
//        double screenHight = scene._cam._screenWidth * aspectRatio;
//
//        double tanHalfalphaY = ((screenHight) / 2) / scene._cam._screenDistance;
//
//        dist *= 2;
//
//        double magnitude = (dist * tanHalfalphaX) / scene.widthPixels;
//        screenx = screenx.mult(magnitude);
//        magnitude = (dist * tanHalfalphaY) / scene.heightPixels;
//        screeny = screeny.mult(magnitude);
//
//        Vector3D firstRay = Vector3D.vectorFromPoints(scene._cam._lookAtposition, scene._cam._camPosition);
//        firstRay._x += ((scene.heightPixels / 2) * screeny._x - (scene.widthPixels / 2) * screenx._x);
//        firstRay._y += ((scene.heightPixels / 2) * screeny._y - (scene.widthPixels / 2) * screenx._y);
//        firstRay._z += ((scene.heightPixels / 2) * screeny._z - (scene.widthPixels / 2) * screenx._z);
//
//
//        double rX = firstRay._x + xPixel * screenx._x - yPixel * screeny._x;
//        double rY = firstRay._y + xPixel * screenx._y - yPixel * screeny._y;
//        double rZ = firstRay._z + xPixel * screenx._z - yPixel * screeny._z;
//        Vector3D r = new Vector3D(rX, rY, rZ);
//        r = r.normalize();
//        this._x = r._x;
//        this._y = r._y;
//        this._z = r._z;
//
//
//        this.startP = scene._cam._camPosition;
//    }

    public Ray(Point3D startPoint, Vector3D baseVector) {
        super(baseVector._x, baseVector._y, baseVector._z);
        startP = startPoint;
    }


    public Point3D tPointOnRay(double t) {
        return this.mult(t).plus(this.startP).toPoint();
    }

    @Override
    public Ray normalize() {
        Vector3D v = new Vector3D(this._x,this._y,this._z);
        return new Ray(this.startP,v.normalize());
    }
}

import processing.core.PApplet;
import SimpleOpenNI.*;
import oscP5.*;
import netP5.*;
import processing.core.PVector;

/**
 * Gets position data for body
 */
public class BodyTracker extends PApplet {
    // camera object
    SimpleOpenNI cam;

    // for udp
    OscP5 osc;
    NetAddress destination;
    // joint vectors
    PVector headJoint = new PVector();
    PVector neckJoint = new PVector();
    PVector torsoJoint = new PVector();

    public void setup() {
        cam = new SimpleOpenNI(this);

        if (!cam.isInit()) {
            System.out.println("Kinect not found.");
            exit();
            return;
        }

        cam.enableDepth();
        cam.enableUser();

        background(200, 0, 0);
        strokeWeight(3);

        size(cam.depthWidth(), cam.depthHeight());
    }

    public void draw() {
        cam.update();

        image(cam.userImage(), 0, 0);

        // get users
        int[] userList = cam.getUsers();
        for (int i = 0; i < userList.length; i++) {
            if (cam.isTrackingSkeleton(userList[i])) {
                cam.getJointPositionSkeleton(i,SimpleOpenNI.SKEL_HEAD,headJoint);
                cam.getJointPositionSkeleton(i,SimpleOpenNI.SKEL_NECK,neckJoint);
                cam.getJointPositionSkeleton(i,SimpleOpenNI.SKEL_TORSO,torsoJoint);
                if (headJoint.z < torsoJoint.z) {
                    System.out.println("Leaning forward");
                } else {
                    System.out.println("Leaning backward");
                }
                trackSkeleton(userList[i]);
            }
        }

    }

    public void trackSkeleton(int user) {
        stroke(0, 255, 0);
        cam.drawLimb(1, SimpleOpenNI.SKEL_HEAD, SimpleOpenNI.SKEL_NECK);
        stroke(255, 0, 0);
        cam.drawLimb(1, SimpleOpenNI.SKEL_NECK, SimpleOpenNI.SKEL_TORSO);

    }


    public void onNewUser(SimpleOpenNI curContext, int userId) {

        println("onNewUser - userId: " + userId);
        println("\tstart tracking skeleton");

        curContext.startTrackingSkeleton(userId);
    }

    public void onLostUser(SimpleOpenNI curContext, int userId) {
        println("onLostUser - userId: " + userId);
    }

    public void onVisibleUser(SimpleOpenNI curContext, int userId) {
        //println("onVisibleUser - userId: " + userId);
    }


}

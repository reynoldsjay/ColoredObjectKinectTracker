import processing.core.PApplet;
import processing.core.PImage;
import processing.video.*;
import SimpleOpenNI.*;

/**
 * Processing sketch to track a colored object in 3D with Kinect
 */
public class ColoredObjectKinectTracker extends PApplet {

    // camera object
    SimpleOpenNI cam;
    // WEBCAM VERSION
    // Capture cam;

    PImage kinectFrame;

    // color to track
    int trackColor;

    public void setup() {

        size(640, 480);

        // init camera, 15 fps
        cam = new SimpleOpenNI(this);
        if (!cam.isInit()) {
            System.out.println("Camera not found.");
            exit();
            return;
        }
        cam.enableRGB();
        cam.enableDepth();

        // WEBCAM VERSION
        // cam = new Capture(this, width, height, 15);
        // cam.start();


        // init tracking red
        trackColor = color(255, 0 , 0);



    }

    public void draw() {
//        WEBCAM VERSION
//        if(cam.available()) {
//            cam.read();
//        }
//        cam.loadPixels();

        kinectFrame = cam.rgbImage();
        image(kinectFrame, 0, 0);

        // difference in color to check for, big to automatically get first pixel
        float minDiff = Float.MAX_VALUE;

        // coordinates of pixel that is closest to the tracked color
        int closestX = 0;
        int closestY = 0;

        // iterate through current frame
        for (int x = 0; x < kinectFrame.width; x++) {
            for (int y = 0; y < kinectFrame.height; y++) {
                int loc = x + y*kinectFrame.width;
                // get current pixels color
                int currentColor = kinectFrame.pixels[loc];
                float r1 = red(currentColor);
                float g1 = green(currentColor);
                float b1 = blue(currentColor);
                float r2 = red(trackColor);
                float g2 = green(trackColor);
                float b2 = blue(trackColor);

                // check how different pixel color is from color tracked
                float d = dist(r1, g1, b1, r2, g2, b2);

                // update closest color pixel
                if (d < minDiff) {
                    minDiff = d;
                    closestX = x;
                    closestY = y;
                }
            }
        }

        // draw circle if color is close enough
        if (minDiff < 10) {
            fill(trackColor);
            strokeWeight(4.0f);
            stroke(0);
            ellipse(closestX, closestY, 16, 16);
            System.out.println(closestX + "," + closestY);
        }

    }

    // save color where the mouse is clicked in trackColor variable
    public void mousePressed() {
        int loc = mouseX + mouseY*kinectFrame.width;
        trackColor = kinectFrame.pixels[loc];
    }
}
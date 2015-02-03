import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import wblut.processing.*; 
import wblut.geom.*; 
import wblut.core.*; 
import wblut.math.*; 
import java.util.List; 
import oscP5.*; 
import netP5.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class GSavi extends PApplet {

/**
 * An audio-visual tool to generate music and visuals from gesture input.
 * Part of Nuno Correla's audio visual user interface research at Goldsmiths.
 * 
 * Project by Will Brown, Missnommer and Miguel Ortiz
 * based on the Ref_WB_Delaunay3D example from the HE_Mesh library.
 * 
 */

 // import HE_Mesh utils






// import OSC utils



// declare OSC objects
OscP5 oscP5;
NetAddress myRemoteLocation;

// declare sketch variable input objects
IntList xMouse;
IntList yMouse;

PVector [] meshData;

int fillColor;
int strokeColor;

int lifetime;
int activetime;
int savedtime;
int explodetime = 60000;
int explodeProp = 0;
int skipPoints = 1;

boolean active = false;
// manages weighting of different drawing modes
int [] randomChoice = {0, 0, 0, 0, 0, 0, 0, 1, 2, 1};

int sketchRate;

// declare OSC message received variables
int magVal;
int durVal;
int gestureVal = 1;
float scaleVal;
float phaseVal;
int speedVal;
int angleVal;

// declare OSC message sent variables
float backgroundLayer;
FloatList bkgdRGBLiveArray;
FloatList bkgdHSBLiveArray;
FloatList bkgdHSBStaticArray;
int bkgdOverride = 255;
FloatList fillVal;
FloatList strokeVal;
FloatList trigRot;
FloatList triRot;
FloatList angRot;
int lineQualVal;
int explodedVal;
float sinRot;
float cosRot;
float tanRot;
float angRotY;
float angRotX;
float angRotZ;
float triRotY;
float triRotX;
float triRotZ;

// declare HE_Mesh objects
List <WB_Point> meshPoints;
WB_Delaunay triangulation;

WB_Render3D render;

// setup sketch environment
public void setup() {
  size(displayWidth, displayHeight, OPENGL);
  strokeWeight(1);
  strokeCap(ROUND);
  strokeJoin(ROUND);
  smooth(8);

  // initialize OSC and assign port for incoming messages
  oscP5 = new OscP5(this, 5001);

  // set remote location 
  myRemoteLocation = new NetAddress("127.0.0.1", 5001);
  
  // plug message communication between OSC messages and sketch
  oscP5.plug(this, "gesture", "/gesture");
  oscP5.plug(this, "scaler", "/scaler");
  oscP5.plug(this, "phase", "/phase");
  oscP5.plug(this, "speed", "/speed");
  oscP5.plug(this, "angle", "/angle");
  oscP5.plug(this, "magnitude", "/magnitude");
  oscP5.plug(this, "duration", "/durVal");

  // initialize lists for gesture input
  xMouse = new IntList();
  yMouse = new IntList();

  // initalize lists for sent messages
  bkgdRGBLiveArray = new FloatList();
  bkgdHSBLiveArray = new FloatList();
  bkgdHSBStaticArray = new FloatList();
  fillVal = new FloatList();
  strokeVal = new FloatList();
  trigRot = new FloatList();
  triRot = new FloatList();
  angRot = new FloatList();

  // initialize list to store gesture input
  meshData = new PVector[xMouse.size()];

  //start timer
  savedtime = millis();

  // initilize HE_Mesh renderer
  render = new WB_Render3D(this);
}

// set preview to full screen
public boolean sketchFullScreen()
{
  return true;
}

public void oscEvent(OscMessage theOscMessage) {

  // verifies received messages in the console
  println("OSC Message received: ");
  println(theOscMessage.addrPattern() + " ");

  // check OSC message patterns and assign them to variables
  if(theOscMessage.checkAddrPattern("/gesture") == true) gestureVal = theOscMessage.get(0).intValue(); 
  else if(theOscMessage.checkAddrPattern("/scaler") == true) scaleVal = theOscMessage.get(0).intValue()/100f;
  else if(theOscMessage.checkAddrPattern("/phase") == true) phaseVal = radians(theOscMessage.get(0).intValue());
  else if(theOscMessage.checkAddrPattern("/speed") == true) speedVal = theOscMessage.get(0).intValue();
  else if(theOscMessage.checkAddrPattern("/angle") == true) angleVal = theOscMessage.get(0).intValue();   
  else if(theOscMessage.checkAddrPattern("/magnitude") == true) magVal = theOscMessage.get(0).intValue();
  else if(theOscMessage.checkAddrPattern("/duration") == true) durVal = theOscMessage.get(0).intValue() * 1000;
}

public void create() {
  // start collecting mesh data when the mouse is dragged
  if(mousePressed) {
    xMouse.append(mouseX);
    yMouse.append(mouseY);

    // initialize list for gesture data
    meshPoints = new ArrayList <WB_Point>();

    // to vary shapes of the mesh
    // skip collected data to form the mesh
    // magnify the effect to make visible in the mesh
    int multiplier = skipPoints * magVal;

    int zPos = (int)random(height);
    float[] point = new float[3];

    for (int i = 0; i < xMouse.size(); i ++) {
      // if multiplier is too large, there will be no drawing before
      // i gets to the value, so this ignores the condition until then
      if(i < multiplier) {
      point[0] = xMouse.get(i); 
      point[1] = yMouse.get(i); 
      point[2] = random(-zPos, zPos); 
      }
      // draws mesh skipping data at regular intervals
      else if(i >= multiplier) {
        if(i % multiplier == 0) {
          point[0] = xMouse.get(i); 
          point[1] = yMouse.get(i); 
          point[2] = random(-zPos, zPos); 
        }
      }

      // appends data points to triangulation object
      meshPoints.add(new WB_Point(point[0], point[1], point[2]));
      // to access the x, y and z coordinates, this mirrors the triangulation data
      meshData = (PVector []) append(meshData, new PVector(point[0], point[1], point[2]));
    }

    // send data to HE_Mesh triangulation method
    triangulation = WB_Delaunay.getTriangulation3D(meshPoints, 0.001f);
  }
    // clear mesh data for new gesture
    else if(!mousePressed) {
    xMouse.clear();
    yMouse.clear();
    meshData = new PVector[xMouse.size()];
  }
}

public void draw() {
  // hides cursor from view
  noCursor();
  // vary framerate
  sketchRate = (int)random(5, speedVal);
  frameRate(sketchRate);
  // OscMessage newMessage = new OscMessage("/framerate");
  // newMessage.add(sketchrate);

  // break up color with monochrome frames and enables background variation when no gesture is registered
  backgroundLayer = map(mouseX, 0, width, 0, 360);
  background(backgroundLayer);
  // OscMessage newMessage = new OscMessage("/backroundLayer");
  // newMessage.add(backgroundLayer);

  // change background color during gesture input every frame
  for(int i = 0; i < xMouse.size(); i ++) {
    // colorMode(RGB);
    // background(meshData[i].x % 255, meshData[i].y % 255, meshData[i].z % 255);
    // OscMessage newMessage = new OscMessage("/bkgdRBGLiveArray");
    // bkgdRBGLiveArray.set(0, meshData[i].x % 255);
    // bkgdRBGLiveArray.set(1, meshData[i].y % 255);
    // bkgdRBGLiveArray.set(2, meshData[i].z % 255);
    // newMessage.add(bkgdRBGLiveArray);

    colorMode(HSB);
    background(meshData[i].x % 360, meshData[i].y % 100, meshData[i].z % 100);
    // OscMessage newMessage = new OscMessage("/bkgdHSBLiveArray");
    // bkgdHSBLiveArray.set(0, meshData[i].x % 360);
    // bkgdHSBLiveArray.set(1, meshData[i].y % 100);
    // bkgdHSBLiveArray.set(2, meshData[i].z % 100);
    // newMessage.add(bkgdHSBLiveArray);
  }
  //reset Present colorMode just incase RGB is enabled above
  colorMode(HSB, 360, 100, 100, 360);

  // set up lights
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);

  hint(ENABLE_DEPTH_TEST);

  // call to function that creates mesh
  create();

  // enable different drawing scenes depending on gesture message
  switch(gestureVal) {
    case(1):
   // OscMessage newMessage = new OscMessage("/Copies");
   // newMessage.add(gestureVal);
      skipPoints = 1;
      pushMatrix();
        display();
      popMatrix();
      break;
    case(2):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      skipPoints = 2;
      pushMatrix();
        // draw 3D
        hint(ENABLE_DEPTH_TEST); 
        translate(-width/4, 0);
        display();
      popMatrix();
      pushMatrix();
        // draw 2D
        hint(DISABLE_DEPTH_TEST); 
        translate(width/4, 0);
        display();
      popMatrix();
      break;
    case(3):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      skipPoints = 3;
      pushMatrix();
        hint(DISABLE_DEPTH_TEST); 
        translate(-width/4, 0);
        display();
      popMatrix();
      pushMatrix();
        hint(ENABLE_DEPTH_TEST); 
        translate(0, 0);
        display();
      popMatrix();
      pushMatrix();
        hint(DISABLE_DEPTH_TEST); 
        translate(width/4, 0);
        display();
      popMatrix();
      break;
    case(4):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      skipPoints = 4;
      pushMatrix(); 
        hint(ENABLE_DEPTH_TEST); 
        translate(-width/4, height/4);
        display();
      popMatrix();
      pushMatrix(); 
        hint(DISABLE_DEPTH_TEST); 
        translate(-width/4, -height/4);
        display();
      popMatrix();
      pushMatrix(); 
        hint(DISABLE_DEPTH_TEST); 
        translate(width/4, height/4);
        display();
      popMatrix();
      pushMatrix(); 
        hint(ENABLE_DEPTH_TEST); 
        translate(width/4, -height/4);
        display();
      popMatrix();
      break;
    case(5):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      skipPoints = 5;
      pushMatrix(); 
        hint(DISABLE_DEPTH_TEST); 
        translate(0, 0);
        display();
      popMatrix();
      pushMatrix(); 
        hint(ENABLE_DEPTH_TEST); 
        translate(-width/4, height/4);
        display();
      popMatrix();
      pushMatrix(); 
        hint(ENABLE_DEPTH_TEST); 
        translate(-width/4, -height/4);
        display();
      popMatrix();
      pushMatrix(); 
        hint(ENABLE_DEPTH_TEST); 
        translate(width/4, height/4);
        display();
      popMatrix();
      pushMatrix(); 
        hint(ENABLE_DEPTH_TEST); 
        translate(width/4, -height/4);
        display();
      popMatrix();
      break;
  }
}

public void display() {

  // start timer for choosing different drawing scenes within the mesh
  lifetime = millis() - savedtime;
  // OscMessage newMessage = new OscMessage("/Time");
  // newMessage.add(lifetime);

  // different variations to rotate the mesh in relation to the height location of the mouse
  sinRot = constrain(sin(angleVal) * map(mouseY, 0, height, 0, height) * scaleVal/width * phaseVal, 0, TWO_PI);
  cosRot = constrain(cos(angleVal) * map(mouseY, 0, height, 0, height) * scaleVal/width * phaseVal, 0, TWO_PI);
  tanRot = constrain(tan(angleVal) * map(mouseY, 0, height, 0, height) * scaleVal/width * phaseVal, 0, TWO_PI);

  rotateY(sinRot);
  rotateX(cosRot);
  rotateZ(tanRot);
  // OscMessage newMessage = new OscMessage("/triRot");
  // trigRot.set(0, sinRot);
  // trigRot.set(1, cosRot);
  // trigRot.set(2, tanRot);
  // newMessage.add(trigRot);

  // different variations to rotate the mesh in relation to the frameCount
  angRotY = constrain(frameCount * (angleVal/1000f)/width * TWO_PI, 0, TWO_PI);
  angRotX = constrain(frameCount * (angleVal/1000f)/width * TWO_PI, 0, TWO_PI);
  angRotZ = constrain(frameCount * (angleVal/1000f)/width * TWO_PI, 0, TWO_PI);

  rotateY(angRotY);
  rotateX(angRotX);
  rotateZ(angRotZ);
  // OscMessage newMessage = new OscMessage("/angRot");
  // angRot.set(0, angRotY);
  // angRot.set(1, angRotX);
  // angRot.set(2, angRotZ);
  // newMessage.add(angRot);

  // start to draw mesh if it's has been populates with points
  if(triangulation != null) {
    //for loop cycles through each triangulation(i) and its members(j)
    for (int i = 0; i < triangulation.Tri.length; i ++) {
      for (int j = 0; j < triangulation.Tri[i].length; j ++) {

        //set strokeWeight in relation to indices in Tri array
        lineQualVal = (int)random(j);
        strokeWeight(lineQualVal);
        // OscMessage newMessage = new OscMessage("/lineQuality");
        // newMessage.add(lineQuality);

        //if enabled, hinders drawing in interesting way, breaks of monotony of seeing so many meshes
        // if(gestureVal == 1) background(bkgdOverride);
        // OscMessage newMessage = new OscMessage("/bkgdOverride);
        // newMessage.add(bkgdOverride);

        //noFill();
        // OscMessage newMessage = new OscMessage("/fillcolor");
        // fillVal.set(0, 0);
        // fillVal.set(1, 0);
        // fillVal.set(2, 0);
        // fillVal.set(3, 0);
        // newMessage.add(fillVal);
        fillColor = color(360 % (triangulation.Tri[i][j] + 1), 100 % (triangulation.Vertices.length + 1), 100 % (triangulation.Edges.length + 1), 360 % (triangulation.Walk.length + 1));
        fill(fillColor);
        // OscMessage newMessage = new OscMessage("/fillcolor");
        // fillVal.set(0, 360 % (triangulation.Tri[i][j] + 1));
        // fillVal.set(1, 100 % (triangulation.Vertices[i][j] + 1));
        // fillVal.set(2, 100 % (triangulation.Edges[i][j] + 1));
        // fillVal.set(3, 360 % (triangulation.Walk[i][j] + 1));
        // newMessage.add(fillVal);

        //noStroke();
        // OscMessage newMessage = new OscMessage("/strokecolor");
        // strokeVal.set(0, 0);
        // strokeVal.set(1, 0);
        // strokeVal.set(2, 0);
        // strokeVal.set(3, 0);
        // newMessage.add(strokeVal);
        strokeColor = color(360 % (triangulation.Tri[i][j] + 1) + 180, 100 % (triangulation.Vertices.length + 1), 100 % (triangulation.Edges.length + 1), 360 % (triangulation.Walk.length + 1));
        stroke(strokeColor);
        // OscMessage newMessage = new OscMessage("/strokecolor");
        // strokeVal.set(0, 360 % (triangulation.Tri[i][j] + 1) + 180);
        // strokeVal.set(1, 100 % (triangulation.Vertices[i][j] + 1));
        // strokeVal.set(2, 100 % (triangulation.Edges[i][j] + 1));
        // strokeVal.set(3, 360 % (triangulation.Walk[i][j] + 1));
        // newMessage.add(strokeVal);

        // timer to select different exploded mesh drawing modes
        if(lifetime > explodetime && !active) {
          active = true;
          savedtime = millis();
          explodeProp = (randomChoice[(int)random(randomChoice.length - 1)]);
          explodetime = (int)random(5000, 10000);
        } else {
          active = false;
        }

        switch(explodeProp) {
          case(0):
            // empty case draws the standard mesh for the first minute. to cahnge the duration, change "explodetime" within the sketch declarations
            // this case is weighted to be drawn 70% of the time. to change the probability that this mode will draw,
            // change the concentration of "0"s in the randomChoice array within the sketch declarations
          break;
          case(1):
          // drawing mode based on length of triangles in mesh
          triRotY = constrain(frameCount * triangulation.Tri.length * scaleVal/width * TWO_PI, 0, TWO_PI);
          triRotX = constrain(frameCount * triangulation.Tri.length * scaleVal/height * TWO_PI, 0, TWO_PI);
          triRotZ = constrain(frameCount * triangulation.Tri.length * scaleVal/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI);

          rotateY(triRotY);
          rotateX(triRotX);
          rotateZ(triRotZ);
          // OscMessage newMessage = new OscMessage("/explodedModel");
          // triRot.set(0, triRotY);
          // triRot.set(1, triRotX);
          // triRot.set(2, triRotZ);
          // newMessage.add(triRot);
          break;
          case(2):
          // drawing mode based on length of vertices associated with each triangle
          triRotY = constrain(frameCount * triangulation.Tri[i][j] * scaleVal/width * TWO_PI, 0, TWO_PI);
          triRotX = constrain(frameCount * triangulation.Tri[i][j] * scaleVal/height * TWO_PI, 0, TWO_PI);
          triRotZ = constrain(frameCount * triangulation.Tri[i][j] * scaleVal/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI);

          rotateY(triRotY);
          rotateX(triRotX);
          rotateZ(triRotZ);
          // OscMessage newMessage = new OscMessage("/explodedModel");
          // triRot.set(0, triRotY);
          // triRot.set(1, triRotX);
          // triRot.set(2, triRotZ);
          // newMessage.add(triRot);
          break;
          case(3):
          // drawing mode based on duration of gestures
          translate(width/2, 0, 0);
          triRotY = constrain(durVal * random(scaleVal)/width * TWO_PI, 0, TWO_PI);
          triRotX = constrain(durVal * random(scaleVal)/height * TWO_PI, 0, TWO_PI);
          triRotZ = constrain(durVal * random(scaleVal)/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI);

          rotateY(triRotY);
          rotateX(triRotX);
          rotateZ(triRotZ);
          // OscMessage newMessage = new OscMessage("/explodedModel");
          // triRot.set(0, triRotY);
          // triRot.set(1, triRotX);
          // triRot.set(2, triRotZ);
          // newMessage.add(triRot);
          break;
        }
      } 
      // render mesh on screen
      render.drawTetrahedron(triangulation.Tri[i], meshPoints);
    }
  }
}


  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "GSavi" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}

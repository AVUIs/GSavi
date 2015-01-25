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










OscP5 oscP5;
NetAddress myRemoteLocation;

IntList xMouse;
IntList yMouse;

PVector [] meshData;

int lifetime;
int activetime;
int savedtime;
int savedactivetime;
int explodetime = 5000;
int explodeProp;
int skipPoints = 1;

int sketchRate;

int mouseState;
int xPos, yPos, zPos;
int magVal;
int durVal;
int gestureVal = 1;
float scaleVal;
float phaseVal;
int speedVal;
int angleVal;

List <WB_Point> meshPoints;
WB_Delaunay triangulation;

WB_Render3D render;

public void setup() {
  size(1400, 900, OPENGL);
  strokeWeight(1);
  strokeCap(ROUND);
  smooth(8);

  oscP5 = new OscP5(this, 5001);

  myRemoteLocation = new NetAddress("127.0.0.1", 5001);
  
  oscP5.plug(this, "x", "/x");
  oscP5.plug(this, "y", "/y");
  oscP5.plug(this, "gesture", "/gesture");
  oscP5.plug(this, "scaler", "/scaler");
  oscP5.plug(this, "phase", "/phase");
  oscP5.plug(this, "speed", "/speed");
  oscP5.plug(this, "angle", "/angle");
  oscP5.plug(this, "magnitude", "/magnitude");
  oscP5.plug(this, "duration", "/durVal");

  xMouse = new IntList();
  yMouse = new IntList();

  meshData = new PVector[xMouse.size()];

  savedtime = millis();
  savedactivetime = millis();

  render = new WB_Render3D(this);
}

public void oscEvent(OscMessage theOscMessage) {

  // println("OSC Message received: ");
  // println(theOscMessage.addrPattern() + " ");

  if(theOscMessage.checkAddrPattern("/mouseState") == true) mouseState = theOscMessage.get(0).intValue();
  else if(theOscMessage.checkAddrPattern("/gesture") == true) gestureVal = theOscMessage.get(0).intValue(); 
  else if(theOscMessage.checkAddrPattern("/x") == true) xPos = theOscMessage.get(0).intValue(); 
  else if(theOscMessage.checkAddrPattern("/y") == true) yPos = theOscMessage.get(0).intValue();
  else if(theOscMessage.checkAddrPattern("/scaler") == true) scaleVal = theOscMessage.get(0).intValue()/100f;
  else if(theOscMessage.checkAddrPattern("/phase") == true) phaseVal = radians(theOscMessage.get(0).intValue());
  else if(theOscMessage.checkAddrPattern("/speed") == true) speedVal = theOscMessage.get(0).intValue();
  else if(theOscMessage.checkAddrPattern("/angle") == true) angleVal = theOscMessage.get(0).intValue();   
  else if(theOscMessage.checkAddrPattern("/magnitude") == true) magVal = theOscMessage.get(0).intValue();
  else if(theOscMessage.checkAddrPattern("/duration") == true) durVal = theOscMessage.get(0).intValue() * 1000;
}

public void create() {
  if(mouseState == 1) {
    xMouse.append(xPos);
    yMouse.append(yPos);

    meshPoints = new ArrayList <WB_Point>();

    int multiplier = skipPoints * magVal;

    zPos = (int)random(height);
    float[] point = new float[3];
    for (int i = 0; i < xMouse.size(); i ++) {
      if(i < multiplier) {
      point[0] = xMouse.get(i); 
      point[1] = yMouse.get(i); 
      point[2] = random(-zPos, zPos); 
      }
      else if(i >= multiplier) {
        if(i % multiplier == 0) {
          point[0] = xMouse.get(i); 
          point[1] = yMouse.get(i); 
          point[2] = random(-zPos, zPos); 
        }
      }

      meshPoints.add(new WB_Point(point[0], point[1], point[2]));
      meshData = (PVector []) append(meshData, new PVector(point[0], point[1], point[2]));
    }

    triangulation = WB_Delaunay.getTriangulation3D(meshPoints, 0.001f);
  }
    else if(mouseState == 0) {
    xMouse.clear();
    yMouse.clear();
    meshData = new PVector[xMouse.size()];
  }
}

public void draw() {
  sketchRate = (int)random(5, speedVal);
  frameRate(sketchRate);
  background(map(mouseX, 0, width, 0, 360), random(360));
  for(int i = 0; i < xMouse.size(); i ++) {
    // colorMode(RGB);
    // background(meshData[i].x % 255, meshData[i].y % 255, meshData[i].z % 255, phaseVal % 255);

    colorMode(HSB);
    background(meshData[i].x % 360, meshData[i].y % 100, meshData[i].z % 100, phaseVal % 360);
  }
  colorMode(HSB, 360, 100, 100, 360);
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);

  hint(ENABLE_DEPTH_TEST);
  translate(0, 0, 0);

  create();

  switch(gestureVal) {
    case(1):
   // OscMessage newMessage = new OscMessage("/Copies");
   // newMessage.add(numReflections);
      skipPoints = 1;
      pushMatrix();
        display();
      popMatrix();
      break;
    case(2):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(numReflections);
      skipPoints = 2;
      pushMatrix();
        hint(ENABLE_DEPTH_TEST); 
        translate(-width/4, 0);
        display();
      popMatrix();
      pushMatrix();
        hint(DISABLE_DEPTH_TEST); 
        translate(width/4, 0);
        display();
      popMatrix();
      break;
    case(3):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(numReflections);
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
      // newMessage.add(numReflections);
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
      // newMessage.add(numReflections);
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

  lifetime = millis() - savedtime;
  println("lifetime: " + lifetime);

  if(lifetime > explodetime) {
    explodeProp = (int)(random(0, 3));
    println("exp: " + explodeProp);
    explodetime = (int)random(7000, 30000); 

    activetime = millis() - savedactivetime;
    println("activetime: " + activetime);
  }

  rotateY(constrain(sin(angleVal) * map(mouseY, 0, height, 0, height) * scaleVal/height * phaseVal, 0, TWO_PI));
  rotateX(constrain(cos(angleVal) * map(mouseX, 0, width, 0, width) * scaleVal/width * phaseVal, 0, TWO_PI));
  rotateZ(constrain(tan(angleVal) * map(mouseX, 0, width, 0, width)/(map(mouseY, 0, height, 0, height) + 1) * scaleVal/width * phaseVal, 0, TWO_PI));

  rotateY(constrain(frameCount * (angleVal/1000f)/height * TWO_PI, 0, TWO_PI));
  rotateX(constrain(frameCount * (angleVal/1000f)/width * TWO_PI, 0, TWO_PI));
  rotateZ(constrain(frameCount * (angleVal/1000f)/width * TWO_PI, 0, TWO_PI));

  if(triangulation != null) {
    for (int i = 0; i < triangulation.Tri.length; i ++) {
      for (int j = 0; j < triangulation.Tri[i].length; j ++) {
        fill(360 % (triangulation.Tri[i][j] + 1), 100 % (triangulation.Vertices.length + 1), 100 % (triangulation.Edges.length + 1), 360 % (triangulation.Walk.length + 1));
        stroke(360 % (triangulation.Tri[i][j] + 1) + 180, 100 % (triangulation.Vertices.length + 1), 100 % (triangulation.Edges.length + 1), 360 % (triangulation.Walk.length + 1));
   
        if(activetime > explodetime) {
          savedactivetime = millis();
          switch(explodeProp) {
            case(0):
            rotateX(constrain(frameCount * triangulation.Tri.length * scaleVal/width * TWO_PI, 0, TWO_PI));
            rotateY(constrain(frameCount * triangulation.Tri.length * scaleVal/height * TWO_PI, 0, TWO_PI));
            rotateZ(constrain(frameCount * triangulation.Tri.length * scaleVal/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI));
            //  OscMessage newMessage = new OscMessage("/explodedModel");
            //  newMessage.add(triRot);
            break;
            case(1):
            rotateX(constrain(frameCount * triangulation.Tri[i][j] * scaleVal/width * TWO_PI, 0, TWO_PI));
            rotateY(constrain(frameCount * triangulation.Tri[i][j] * scaleVal/height * TWO_PI, 0, TWO_PI));
            rotateZ(constrain(frameCount * triangulation.Tri[i][j] * scaleVal/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI));
            //  explodedVal = i * j;
            //  OscMessage newMessage = new OscMessage("/explodedModel");
            //  newMessage.add(explodedVal);
            break;
            case(2):
            translate(width/2, 0, 0);
            rotateX(constrain(durVal * random(scaleVal)/width * TWO_PI, 0, TWO_PI));
            rotateY(constrain(durVal * random(scaleVal)/height * TWO_PI, 0, TWO_PI));
            rotateZ(constrain(durVal * random(scaleVal)/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI));
            //  OscMessage newMessage = new OscMessage("/explodedModel");
            //  newMessage.add(angRot);
            break;
          } 

          if(lifetime > explodetime) savedtime = millis();
        }

        render.drawTetrahedron(triangulation.Tri[i], meshPoints);
      }
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

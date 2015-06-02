/**
 * An audio-visual tool to generate music and visuals from gesture input.
 * Part of Nuno Correla's audio visual user interface research at Goldsmiths, UK.
 * 
 * Project by Will Brown, Missnommer and Miguel Ortiz
 * based on the Ref_WB_Delaunay3D example from the HE_Mesh library.
 * 
 */

 // import HE_Mesh utils
import wblut.processing.*;
import wblut.geom.*;
import wblut.core.*;
import wblut.math.*;
import java.util.List;

// import OSC utils
import oscP5.*;
import netP5.*;

// declare OSC objects
OscP5 oscP5;
NetAddress myRemoteLocation;

//declare PShape objects
PShape triCirc;
PShape triTri;
PShape triSqu;
PShape triLinePSlope;
PShape triLineNSlope;

float cricRot;
float squTime = 0.0;
boolean limit = false;
int division = 0;
int squDivision = 0;

// declare sketch variables and objects
IntList xMouse;
IntList yMouse;

PVector [] meshData;

color fillColor;
color strokeColor;

int duration = millis();
int lifetime;
int activetime;
int savedtime;
int savedDurationTime;
int explodetime;
int explodeProp = 0;
int skipPoints = 1;

float magnitude;
float multiplier; 
float angle;

boolean active = false;
boolean noFillColor = false;
boolean noStrokeColor = false;
boolean bOverride = false;

int [] randomChoice = {0, 0, 0, 0, 1, 2, 3, 1, 2, 3};

int sketchRate;

// declare OSC message received variables
int gestureVal = 1;
float scaleVal;
float phaseVal;
int speedVal = 5;

// declare OSC message sent variables
FloatList mouseCoordPair;
float backgroundLayer;
FloatList bkgdRGBLiveArray;
FloatList bkgdHSBLiveArray;
FloatList bkgdHSBStaticArray;
int bkgdOverride = 255;
FloatList trigRot;
FloatList triRot;
FloatList angRot;
int lineQualVal;
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
void setup() {
  size(displayWidth, displayHeight, OPENGL);
  strokeWeight(1);
  strokeCap(ROUND);
  strokeJoin(ROUND);
  smooth(8);

  triCirc = createShape(TRIANGLE, 0, 0, -width, -height, width, -height);
  triTri = createShape(TRIANGLE, 0, 0, width/30, height/2, 0, height);
  triSqu = createShape(TRIANGLE, 0, 0, 0, width/16, width/16, width/16);
  triLinePSlope = createShape(TRIANGLE, 0, 0, 0, width/4, width/4, width/4);
  triLineNSlope = createShape(TRIANGLE, 0, 0, 0, width/4, width/4, width/4);

  // initialize OSC and assign port for incoming messages
  oscP5 = new OscP5(this, 5001);

  // set remote location 
  myRemoteLocation = new NetAddress("127.0.0.1", 2323);
  
  // plug message communication between OSC messages and sketch
  oscP5.plug(this, "gesture", "/gesture");
  oscP5.plug(this, "scaler", "/scaler");
  oscP5.plug(this, "phase", "/phase");
  oscP5.plug(this, "speed", "/speed");

  // initialize lists for gesture input
  xMouse = new IntList();
  yMouse = new IntList();

  // initalize lists for sent messages
  bkgdRGBLiveArray = new FloatList();
  bkgdHSBLiveArray = new FloatList();
  bkgdHSBStaticArray = new FloatList();
  trigRot = new FloatList();
  triRot = new FloatList();
  angRot = new FloatList();

  // initialize list to store gesture input
  meshData = new PVector[xMouse.size()];

  //start timer
  savedtime = millis();
  savedDurationTime = millis();

  // initilize HE_Mesh renderer
  render = new WB_Render3D(this);
}

//enable full screen presentation mode
boolean sketchFullScreen()
{
  return true;
}

void oscEvent(OscMessage theOscMessage) {

  // verifies received messages in the console
  // println("OSC Message received: ");
  // println(theOscMessage.addrPattern() + " " + theOscMessage);

  // check OSC message patterns and assign them to variables
  if(theOscMessage.checkAddrPattern("/gesture") == true) gestureVal = theOscMessage.get(0).intValue(); 
  else if(theOscMessage.checkAddrPattern("/scaler") == true) scaleVal = abs(theOscMessage.get(0).intValue()/100f);
  else if(theOscMessage.checkAddrPattern("/phase") == true) phaseVal = radians(theOscMessage.get(0).intValue());
  else if(theOscMessage.checkAddrPattern("/speed") == true) speedVal = abs(theOscMessage.get(0).intValue());

  //println(scaleVal + ", " + phaseVal + ", " + speedVal);
}

void create() {

  // start collecting mesh data when the mouse is dragged
  if(mousePressed) {
    xMouse.append(mouseX);
    yMouse.append(mouseY);

    // initialize list for gesture data
    meshPoints = new ArrayList <WB_Point>();

    int zPos = (int)random(height);
    float[] point = new float[3];

    for (int i = 0; i < xMouse.size(); i ++) {

      magnitude = (int)(sqrt(pow(xMouse.get(i), 2) + pow(yMouse.get(i), 2))/100.0);
      duration = (millis() - savedDurationTime)/1000;
      angle = atan2(yMouse.get(i), xMouse.get(i));

      // println(magnitude + ", " + duration + ", " + angle);

      // to vary shapes of the mesh
      // skip collected data to form the mesh
      // magnify the effect to make visible in the mesh
      multiplier = skipPoints * magnitude;

      //send x, y values to gesture follower
      OscMessage newMessage = new OscMessage("/tablet/1");
      newMessage.add(map(xMouse.get(i), 0, width, 0.0, 1.0));
      newMessage.add(map(yMouse.get(i), 0, height, 0.0, 1.0));
      oscP5.send(newMessage, myRemoteLocation);

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
    triangulation = WB_Delaunay.getTriangulation3D(meshPoints, 0.001);
  }
}

// clear mesh data for new gesture
void mouseReleased() {
  xMouse.clear();
  yMouse.clear();
  meshData = new PVector[xMouse.size()];
  savedDurationTime = millis();
}

void draw() {
  // disable the cursor in drawing mode
  noCursor();
  // vary framerate
  //sketchRate = (int)random(5, speedVal);
  sketchRate = (int)map(speedVal, 0, 100, 5, 60);
  frameRate(sketchRate);
  // OscMessage newMessage = new OscMessage("/framerate");
  // newMessage.add(sketchrate);
  // oscP5.send(newMessage, myRemoteLocation);

  // break up color with monochrome frames and enables background variation when no gesture is registered
  backgroundLayer = map(mouseX, 0, width, 0, 360);
  background(backgroundLayer);
  // OscMessage newMessage = new OscMessage("/backroundLayer");
  // newMessage.add(backgroundLayer);
  // oscP5.send(newMessage, myRemoteLocation);

  // change background color during gesture input every frame
  for(int i = 0; i < xMouse.size(); i ++) {
    // colorMode(RGB);
    // background(meshData[i].x % 255, meshData[i].y % 255, meshData[i].z % 255);
    // OscMessage newMessage = new OscMessage("/bkgdRBGLiveArray");
    // newMessage.add(meshData[i].x % 255);
    // newMessage.add(meshData[i].y % 255);
    // newMessage.add(meshData[i].z % 255);
    // oscP5.send(newMessage, myRemoteLocation);
    // if(bOverride) background(bkgdOverride);
    // else {
      colorMode(HSB);
      background(meshData[i].x % 360, meshData[i].y % 100, meshData[i].z % 100);
    // }
    // OscMessage newMessage = new OscMessage("/bkgdHSBLiveArray");
    // newMessage.add(meshData[i].x % 360);
    // newMessage.add(meshData[i].y % 360);
    // newMessage.add(meshData[i].z % 360);
    // oscP5.send(newMessage, myRemoteLocation);
  }
  //reset Present colorMode just incase RGB is enabled above
  colorMode(HSB, 360, 100, 100, 360);

  // set up lights
  directionalLight(255, 255, 255, 1, 1, -1);
  directionalLight(127, 127, 127, -1, -1, 1);

  //hint(ENABLE_DEPTH_TEST);

  // call to function that creates mesh
  if(mousePressed) create();

  // enable different drawing scenes depending on gesture message
  switch(gestureVal) {
    case(1):
   // OscMessage newMessage = new OscMessage("/Copies");
   // newMessage.add(gestureVal);
   // oscP5.send(newMessage, myRemoteLocation);
      skipPoints = 1;
      pushMatrix();
        hint(ENABLE_DEPTH_TEST);
        directionalLight(255, 255, 255, 1, 1, -1);
        directionalLight(127, 127, 127, -1, -1, 1);
        display();
      popMatrix();

      for(int i = 0; i < meshData.length; i ++) {
        triCirc = createShape(TRIANGLE, 0, 0, width/(meshData[i].x + 1) * magnitude, width/(meshData[i].y + 1) * magnitude, width/(meshData[i].y + 1) * magnitude, width/(meshData[i].z + 1) * magnitude);
      }

      translate(width/2, height/2);
      pushMatrix();
      for(int i = 0; i < meshData.length; i ++) {
        noLights();
        triCirc.setStrokeWeight(0.5);
        triCirc.setFill(color(map(i, 1, meshData.length, 0, 360), meshData.length % 100, 100, meshPoints.size() % 360));
        rotate(TWO_PI/(meshData.length + 1));
        triCirc.rotate(cricRot * i);
        shape(triCirc);
      }
      cricRot += activetime * .0005;
      popMatrix();
    break;
    case(2):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      // oscP5.send(newMessage, myRemoteLocation);
      skipPoints = 2;
      pushMatrix();
      //   // draw 3D
         hint(ENABLE_DEPTH_TEST); 
      //   translate(-width/4, 0);
        display();
      popMatrix();

      if(triangulation != null) {
        for(int i = 0; i < triangulation.Tri.length; i ++) {
            triTri = createShape(TRIANGLE, 0, (height/2 - i * random(magnitude)), width/(i + 1), height/2, 0, (height/2 + i * random(magnitude)));

            division = width/(i + 1);
        } 
      }

      pushMatrix();
      for(int i = 0; i < division; i ++) {
        noLights();
        triTri.setFill(color(map(i, 0, division, 0, 360), 100, 100, random(10, 360)));
        shape(triTri, width/division * i, 0);
        triTri.rotateY(PI);
        triTri.translate(width/division, 0);
      }
      popMatrix();
      break;
    case(3):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      // oscP5.send(newMessage, myRemoteLocation);
      skipPoints = 3;
      pushMatrix();
        hint(ENABLE_DEPTH_TEST); 
        display();
      popMatrix();

      if(triangulation != null) {
        for(int i = 0; i < triangulation.Vertices.length; i ++) {
          for(int j = 0; j < triangulation.Vertices[i].length; j ++) {
            triSqu = createShape(TRIANGLE, 0, 0, 0, width/((j + 1) * sketchRate * .05), width/((j + 1) * sketchRate * .05), width/((j + 1) * sketchRate * .05));

            squDivision = (int)((j + 1) * sketchRate * .05);

            triSqu.setStrokeWeight(0);
            triSqu.setFill(color(map(i, 0, triangulation.Vertices.length, 0, 360), 100, 100, map(j, 0, triangulation.Vertices[i].length, 0, 360)));
          }
          pushMatrix();
            noLights();
            if(squDivision > 0) {
              if(squDivision % 2 == 0) {
                translate(0, height/2);
                for(int k = 0; k < 3; k ++) {
                  shape(triSqu, width/squDivision * i, 0);
                  triSqu.rotate(PI/2);
                  if(k % 2 != 0) translate(0, width/squDivision);
                  else if(k % 2 == 0) translate(width/squDivision, 0);
                }
              }
            }
            popMatrix();
        } 
      }
      break;
    case(4):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      // oscP5.send(newMessage, myRemoteLocation);
      skipPoints = 4;
       pushMatrix(); 
         hint(ENABLE_DEPTH_TEST); 
         display();
       popMatrix();

        pushMatrix();
        translate(0, random(0, height));
        rotate(-PI/2);
        for(int i = 0; i < 32; i ++) {
          noLights();
          triLinePSlope.setStrokeWeight(0);
          triLinePSlope.setFill(color(map(i, 1, 32, 0, 180), 100, 100, random(10, 360)));
            shape(triLinePSlope, width/32 * i, width/32 * i); 
        }
        popMatrix();

        pushMatrix();
        translate(random(0, width), 0);
        rotate(-PI/2);
        for(int i = 0; i < 32; i ++) {
          noLights();
          triLinePSlope.setStrokeWeight(0);
          triLinePSlope.setFill(color(map(i, 1, 32, 180, 0), 100, 100, random(10, 360)));
          shape(triLinePSlope, width/32 * -i, width/32 * -i); 
        }
        popMatrix();
    break;
    case(5):
      // OscMessage newMessage = new OscMessage("/Copies");
      // newMessage.add(gestureVal);
      // oscP5.send(newMessage, myRemoteLocation);
      skipPoints = 5;
      pushMatrix(); 
         hint(ENABLE_DEPTH_TEST); 
         display();
      popMatrix();

      pushMatrix();
      translate(random(0, width), random(0, height));
      rotate(PI);
      for(int i = 0; i < 32; i ++) {
        //noLights();
        triLineNSlope.setStrokeWeight(0);
        triLineNSlope.setFill(color(map(i, 1, 32, 180, 360), 100, 100, random(10, 360)));
        shape(triLineNSlope, width/32 * i, width/32 * i); 
      }
      popMatrix();

      pushMatrix();
      translate(random(0, width), random(0, height));
      rotate(PI);
      for(int i = 0; i < 32; i ++) {
        //noLights();
        triLineNSlope.setStrokeWeight(0);
        triLineNSlope.setFill(color(map(i, 1, 32, 360, 180), 100, 100, random(10, 360)));
        shape(triLineNSlope, width/32 * -i, width/32 * -i); 
      }
    popMatrix();
    break;
  }
}

void display() {

  // start timer for choosing different drawing scenes within the mesh
  lifetime = millis() - savedtime;
  // OscMessage newMessage = new OscMessage("/Time");
  // newMessage.add(lifetime);
  // oscP5.send(newMessage, myRemoteLocation);

  // different variations to rotate the mesh in relation to the height location of the mouse
  sinRot = constrain(sin(angle) * scaleVal/width * phaseVal/10f, 0, TWO_PI);
  cosRot = constrain(cos(angle) * scaleVal/width * phaseVal/10f, 0, TWO_PI);
  tanRot = constrain(tan(angle) * scaleVal/width * phaseVal/10f, 0, TWO_PI);

  rotateY(sinRot);
  rotateX(cosRot);
  rotateZ(tanRot);
  // OscMessage newMessage = new OscMessage("/triRot");
  // newMessage.add(sinRot);
  // newMessage.add(cosRot);
  // newMessage.add(tanRot);
  // oscP5.send(newMessage, myRemoteLocation);

  // different variations to rotate the mesh in relation to the frameCount
  angRotY = constrain(frameCount * angle/width * TWO_PI, 0, TWO_PI);
  angRotX = constrain(frameCount * angle/width * TWO_PI, 0, TWO_PI);
  angRotZ = constrain(frameCount * angle/width * TWO_PI, 0, TWO_PI);

  rotateY(angRotY);
  rotateX(angRotX);
  rotateZ(angRotZ);
  // OscMessage newMessage = new OscMessage("/angRot");
  // newMessage.add(angRotY);
  // newMessage.add(angRotX);
  // newMessage.add(angRotZ);
  // oscP5.send(newMessage, myRemoteLocation);

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
        // oscP5.send(newMessage, myRemoteLocation);

        // enables an additional drawing mode, breaks of monotony of seeing so many meshes
        if(bOverride) background(bkgdOverride);
        // OscMessage newMessage = new OscMessage("/bkgdOverride);
        // newMessage.add(bkgdOverride);
        // oscP5.send(newMessage, myRemoteLocation);

        if(noFillColor) {
          fillColor = color(360 % (triangulation.Tri[i][j] + 1), 100 % (triangulation.Vertices.length + 1), 100 % (triangulation.Edges.length + 1), 360 % (triangulation.Walk.length + 1));
          // OscMessage newMessage = new OscMessage("/fillcolor");
          // newMessage.add(360 % (triangulation.Tri[i][j] + 1));
          // newMessage.add(100 % (triangulation.Vertices[i][j] + 1));
          // newMessage.add(100 % (triangulation.Edges[i][j] + 1));
          // newMessage.add(360 % (triangulation.Walk[i][j] + 1));
          // oscP5.send(newMessage, myRemoteLocation);
        } else {
            fillColor = color(360 % (triangulation.Tri[i][j] + 1), 100 % (triangulation.Vertices.length + 1), 0, 360 % (triangulation.Walk.length + 1));
            // OscMessage newMessage = new OscMessage("/fillcolor");
            // newMessage.add(0);
            // newMessage.add(0);
            // newMessage.add(0);
            // newMessage.add(0);
            // oscP5.send(newMessage, myRemoteLocation);
          }

        fill(fillColor);


        if(noStrokeColor) {
          strokeColor = color(360 % (triangulation.Tri[i][j] + 1) + 180, 100 % (triangulation.Vertices.length + 1), 100 % (triangulation.Edges.length + 1), 360 % (triangulation.Walk.length + 1));
          // OscMessage newMessage = new OscMessage("/strokecolor");
          // newMessage.add(360 % (triangulation.Tri[i][j] + 1) + 180);
          // newMessage.add(100 % (triangulation.Vertices[i][j] + 1));
          // newMessage.add(100 % (triangulation.Edges[i][j] + 1));
          // newMessage.add(360 % (triangulation.Walk[i][j] + 1));
          // oscP5.send(newMessage, myRemoteLocation);
        } else {
            strokeColor = color(360 % (triangulation.Tri[i][j] + 1) + 180, 100 % (triangulation.Vertices.length + 1), 0, 360 % (triangulation.Walk.length + 1));
            // OscMessage newMessage = new OscMessage("/strokecolor");
            // newMessage.add(0);
            // newMessage.add(0);
            // newMessage.add(0);
            // newMessage.add(0);
            // oscP5.send(newMessage, myRemoteLocation);
          }
        stroke(strokeColor);

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
          // newMessage.add(triRotY);
          // newMessage.add(triRotX);
          // newMessage.add(triRotZ);
          // oscP5.send(newMessage, myRemoteLocation);
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
          // newMessage.add(triRotY);
          // newMessage.add(triRotX);
          // newMessage.add(triRotZ);
          // oscP5.send(newMessage, myRemoteLocation);
          break;
          case(3):
          // drawing mode based on duration of gestures
          //translate(width/2, 0, 0);
          triRotY = constrain(duration * random(scaleVal)/width * TWO_PI, 0, TWO_PI);
          triRotX = constrain(duration * random(scaleVal)/height * TWO_PI, 0, TWO_PI);
          triRotZ = constrain(duration * random(scaleVal)/sqrt(pow(width, 2) + pow(height, 2)) * TWO_PI, 0, TWO_PI);

          rotateY(triRotY);
          rotateX(triRotX);
          rotateZ(triRotZ);
          // OscMessage newMessage = new OscMessage("/explodedModel");
          // newMessage.add(triRotY);
          // newMessage.add(triRotX);
          // newMessage.add(triRotZ);
          // oscP5.send(newMessage, myRemoteLocation);
          break;
        }
      } 
      // render mesh on screen
      render.drawTetrahedron(triangulation.Tri[i], meshPoints);
    }
  }
}

void keyPressed() {
  if(key == 'a') noFillColor = true;
  else if(key == 'b') noFillColor = false;
  if(key == 'c') noStrokeColor = true;
  else if (key == 'd') noStrokeColor = false;
  if(key == 'e') bOverride = true;
  else if(key =='f') bOverride = false;
}



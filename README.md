# GS.avi
beta3 code

GS.avi is a gestural instrument that generates continuous spatial visualizations and music from the input of a performer. The features extracted form a performer’s gesture defines the color, position, form and orientation of a 3-dimensional Delaunay mesh. The music, composed using granular synthesis, is generated from features extracted from the mesh. The project was created using Processing and MAX/MSP. OSC is used to communicate between the two.


# MAX/MSP Usage

NOTE: The current MAX/MSP patch uses sound samples to simulate the sound generated by the visuals. In this version, the visual features do not generate the audio. You’ll need 5 sound samples.

1: To use this patch:

A. Download the MAX/MSP dependencies as listed in the following section, extract them and put them in the PACKAGES folder where MAX/MSP is installed on your computer.

B. Open the file GSavi.maxpat. 

C. Press the SPACE BAR to toggle the sound on and off (or click on the sound icon on the bottom right of the patch to start the sound).


2: To load sounds (optional): Store 5 sound files named 1.wav, 2.wav, … 5.wav in GSavi > snd


3: If sounds fail to load automatically:

A. Disable the PRESENTATION MODE of the patch and look for the 5 SHAKER objects (spaced horizontally near the middle of the patch).

B. For each SHAKER object (1 through 5), double click to open the encapsulated patch. Click LOAD FILE and import each respective sound file (named 1.wav, 2.wav, … 5.wav) into each SHAKER object (named “one” through “cinco”).

C. After the sound files are loaded, re-enable the PRESENTATION MODE of the patch.


# Processing Usage

A. Start the Processing sketch.

B. Use the trackpad or mouse to begin drawing in the Processing window.

C. To enable different Fill and Stroke combination (not yet controlled by sound):
Press ‘a’ to enable Fill()
Press ‘b’ to enable noFill()
Press ‘c’ to enable Stroke()
Press ‘d’ to enable noStroke()

D. To override the background drawn treatment to enable a different drawing mode (not yet controlled by sound):
Press ‘e’ to enable the different mode
Press ‘f’ to disable the different mode

E. Visual cues to signify the gesture predicted by the system has been added.
Gesture 1: Predicts a circle drawn counter-clockwise.
Gesture 2: Predicts a triangle drawn clockwise.
Gesture 3: Predicts a square drawn counter-clockwise. 
Gesture 4: Predicts a line drawn from the upper right to the lower left.
Gesture 5: Predicts a line drawn from the upper left to the lower right.

# MAX/MSP Dependencies

Install the following libraries into Max/Packages:
(For previous versions of MAX/MSP, install the relevant version of the library)

MuBu Library of external objects for Max 7: http://forumnet.ircam.fr/product/mubu/

FTM Library of external objects for Max 7: http://ftm.ircam.fr/index.php/Download

CNMAT OSC externals for Max 7 - download the latest version from:
http://cnmat.berkeley.edu/downloads/externals/archives
Create a folder “CNMAT" inside Max/Packages, and a folder “externals” inside it
Move the file “OSC-route.mxo” (inside CNMAT_Externals) to Max/Packages/CNMAT/externals

Gesture follower external for Max 7: https://github.com/bcaramiaux/ofxGVF
Create a folder “GVF" inside Max/Packages, and a folder “externals” inside it
Move the file “gvf.mxo” (inside ofxGVF/example-maxmsp/maxexternal) to Max/Packages/GVF/externals


# Processing Dependencies

Install the following libraries into Documents/Processing/libraries:

He_Mesh Library: http://hemesh.wblut.com/hemesh-latest.zip

oscP5 Library: http://www.sojamo.de/libraries/oscP5/download/oscP5-0.9.8.zip

A guide to installing libraries can be found here: http://www.learningprocessing.com/tutorials/libraries/
/**
 * Brightness Thresholding 
 * Originial sketch by Golan Levin. 
 *
 * Does binary thresholding of images captured from a web cam. The threshold
 * level is adjustable with the 'a' and 'z' keys.
 */


import processing.video.*;

color black = color(0);
color white = color(255);
int numPixels;
int threshold = 127;

Capture video;

void setup() {
  size(800, 600); // Change size to 320 x 240 if too slow at 640 x 480
  strokeWeight(5);
  
  // This the default video input, see the GettingStartedCapture 
  // example if it creates an error
  video = new Capture(this, width, height);
  
  // Start capturing the images from the camera
  video.start(); 
  
  numPixels = video.width * video.height;
  noCursor();
  smooth();
}

void draw() {
  if (video.available()) {
    video.read();
    video.loadPixels();
    float pixelBrightness; // Declare variable to store a pixel's color
    // Turn each pixel in the video frame black or white depending on its brightness
    loadPixels();
    for (int i = 0; i < numPixels; i++) {
      pixelBrightness = brightness(video.pixels[i]);
      pixels[i] = (pixelBrightness > threshold ? white : black);
    }
    updatePixels();
    // Test a location to see where it is contained. Fetch the pixel at the test
    // location (the cursor), and compute its brightness
    int testValue = get(mouseX, mouseY);
    float testBrightness = brightness(testValue);
    if (testBrightness > threshold) { // If the test location is brighter than
      fill(black); // the threshold set the fill to black
    } 
    else { // Otherwise,
      fill(white); // set the fill to white
    }
    ellipse(mouseX, mouseY, 20, 20);
  }
}

void keyTyped() {
  if (key == 'a') {
    ++threshold;
    println("Threshold: " + threshold);
  } else if (key == 'z') {
    --threshold;
    println("Threshold: " + threshold);
  }
}

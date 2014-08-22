/**
 * Brightness Tracking 
 * Original sketch by Golan Levin. 
 *
 * Tracks the brightest pixel in a live video signal. The tracking point uses
 * a moving average to smooth out the motion of the brightest pixel.
 */


import processing.video.*;

final int BRIGHTNESS_RADIUS = 25; 
Capture video;
boolean useAverage = true;
float avgBrightestX = 0.0f;
float avgBrightestY = 0.0f;
float alpha = 0.10f;

void setup() {
  size(800, 600);
  // Uses the default video input, see the reference if this causes an error
  video = new Capture(this, width, height);
  video.start();  
  noStroke();
  smooth();
}

void draw() {
  if (video.available()) {
    video.read();
    image(video, 0, 0, width, height); // Draw the webcam video onto the screen
    float brightestX = 0.0f; // X-coordinate of the brightest video pixel
    float brightestY = 0.0f; // Y-coordinate of the brightest video pixel
    float brightestValue = 0; // Brightness of the brightest video pixel
    // Search for the brightest pixel: For each row of pixels in the video image and
    // for each pixel in the yth row, compute each pixel's index in the video
    video.loadPixels();
    int index = 0;
    for (int y = 0; y < video.height; y++) {
      for (int x = 0; x < video.width; x++) {
        // Get the color stored in the pixel
        int pixelValue = video.pixels[index];
        // Determine the brightness of the pixel
        float pixelBrightness = brightness(pixelValue);
        // If that value is brighter than any previous, then store the
        // brightness of that pixel, as well as its (x,y) location
        if (pixelBrightness > brightestValue) {
          brightestValue = pixelBrightness;
          brightestY = y;
          brightestX = x;
        }
        index++;
      }
    }
    // Moving average of the brightest point.
    avgBrightestX = alpha * brightestX + (1.0f - alpha) * avgBrightestX;
    avgBrightestY = alpha * brightestY + (1.0f   - alpha) * avgBrightestY;
    //println("(" + brightestX + ", " + brightestY + "), (" 
    //    + avgBrightestX + ", " + avgBrightestY + "), useAverage = " + useAverage); 

    // Draw a large, yellow circle at the brightest pixel
    if (useAverage == true) {
      fill(255, 153, 0, 128);
      ellipse(int(avgBrightestX), int(avgBrightestY), BRIGHTNESS_RADIUS, BRIGHTNESS_RADIUS);
    } else {
      fill(255, 204, 0, 128);
      ellipse(int(brightestX), int(brightestY), BRIGHTNESS_RADIUS, BRIGHTNESS_RADIUS);
    }
  }
}

void keyTyped() {
  useAverage = !useAverage;
}

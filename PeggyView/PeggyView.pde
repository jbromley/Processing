// PeggyView.pde
//
// Capture an image with the camera, crop a 400x400 square, apply a 
// threshold, then down-pyramid it to 25x25. Take this image
// and send it to the Peggy 2 LED matrix for visualization.
import gab.opencv.*;
import java.awt.*;
import processing.video.*;
import processing.serial.*;

import org.opencv.core.CvType;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;

final int INPUT_WIDTH = 640;
final int INPUT_HEIGHT = 480;
final int CROP_SIZE = 400;
final int OUTPUT_SIZE = 25;

Capture video;
OpenCV opencv;
Rect cropRect;
Size outputSize;
boolean useAdaptive;
int threshold;
Serial serialPort;

void setup() {
  size(OUTPUT_SIZE, OUTPUT_SIZE);
  scale(2.0);
  video = new Capture(this, INPUT_WIDTH, INPUT_HEIGHT);
  opencv = new OpenCV(this, INPUT_WIDTH, INPUT_HEIGHT);
  opencv.useGray();
  
  cropRect = new Rect((INPUT_WIDTH - CROP_SIZE) / 2, (INPUT_HEIGHT - CROP_SIZE) / 2, CROP_SIZE, CROP_SIZE);
  outputSize = new Size(OUTPUT_SIZE, OUTPUT_SIZE);
  useAdaptive = true;
  threshold = 128;  
  
  // Set up the serial port.
  printArray(Serial.list());
  // TODO: Write function to find a given serial port.
  serialPort = new Serial(this, Serial.list()[7], 115200);
  
  video.start();
}

void draw() {
  opencv.loadImage(video);
  
  // Crop the video image.
  Mat videoMat = opencv.getGray();
  Mat cropMat = new Mat(videoMat, cropRect);
  
  // Apply the threshold.
  if (useAdaptive) {
    Imgproc.adaptiveThreshold(cropMat, cropMat, 255, 
                              Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, 
                              Imgproc.THRESH_BINARY, 9, 5);  
  } else {
    Imgproc.threshold(cropMat, cropMat, threshold, 255, Imgproc.THRESH_BINARY);
  }

  // Downsize the image.
  // Mat outputMat = new Mat(OUTPUT_SIZE, OUTPUT_SIZE, CvType.CV_8UC1);
  // Imgproc.resize(cropMat, outputMat, outputSize, 0, 0, Imgproc.INTER_LINEAR);
  
  // Down-pyramid the image to the desired size.
  Size downSize = new Size(CROP_SIZE / 2, CROP_SIZE / 2);
  Mat mat1 = cropMat;
  Mat outputMat = null;
  
  while (downSize.width >= OUTPUT_SIZE) {
    outputMat = new Mat(downSize, CvType.CV_8UC1);
    Imgproc.pyrDown(mat1, outputMat);
    mat1 = outputMat;
    downSize.width /= 2;
    downSize.height /= 2;
  }
  
  // Convert the image and show it on screen.
  PImage outputImg = createImage(OUTPUT_SIZE, OUTPUT_SIZE, RGB);
  opencv.toPImage(outputMat, outputImg);
  image(outputImg, 0, 0);
  
  // Send the image to the Peggy board over a serial connection.
  transmitImageSerial(serialPort, outputImg); 
}

void captureEvent(Capture c) {
  c.read();
}

void keyTyped() {
  if (key == 'a') {
    useAdaptive = !useAdaptive;
    println("Adaptive threshold is " + (useAdaptive ? "on" : "off"));
  } else if (key == '1') {
    --threshold;
    println("Threshold is " + threshold);
  }
  else if (key == '2') {
    ++threshold;
    println("Threshold is " + threshold);
  }
}

void transmitImageSerial(Serial port, PImage img)
{
  int out = 0;
  int pixel = 0;
  for (int y = 0; y < OUTPUT_SIZE; ++y) {
    // Compose the whole row into a single byte.
    for (int x = 0; x < OUTPUT_SIZE; ++x) {
      pixel = img.get(x, y);
      if (pixel > 0) {
        out |= (1 << (24 - x));
      }
    }
    
    // Send the row.
    port.write((byte) ((out & 0xFF000000) >> 24));
    port.write((byte) ((out & 0x00FF0000) >> 16));
    port.write((byte) ((out & 0x0000FF00) >> 8));
    port.write((byte) (out & 0x000000FF));
    
    // Reset the row.
    out = 0;
  }
}

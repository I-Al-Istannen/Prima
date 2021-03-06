package com.tiggerbiggo.prima.processing.fragment.render;

import com.tiggerbiggo.prima.calculation.ColorTools;
import com.tiggerbiggo.prima.core.Vector2;
import com.tiggerbiggo.prima.graphics.SafeImage;
import com.tiggerbiggo.prima.processing.fragment.Fragment;
import java.awt.Color;

/**
 * Allows smooth fading between multiple images
 */
public class ImageFadeFragment implements Fragment<Color[]> {

  private SafeImage[] imgs;
  private Fragment<Vector2[]> position;
  private Fragment<Double> fade;

  /**
   * Constructs a new FadeImageFragment
   *
   * @param position For each frame, the position to sample colors from the images
   * @param fade For each frame, the
   */
  public ImageFadeFragment(Fragment<Vector2[]> position, Fragment<Double> fade, SafeImage[] imgs) {
    this.imgs = imgs;
    this.position = position;
    this.fade = fade;
  }

  /**
   * The main calculation method. All processing for a given pixel should be done in this method.
   *
   * @param x The X position of the pixel being rendered
   * @param y The Y position of the pixel being rendered
   * @param w The width of the image
   * @param h The height of the image
   * @param num The number of frames in the animation
   * @return The output of the fragment
   */
  @Override
  public Color[] get(int x, int y, int w, int h, int num) {
    Vector2[] positions;
    Double fadeAmount;

    //read in arrays from animators
    positions = position.get(x, y, w, h, num);
    fadeAmount = fade.get(x, y, w, h, num);

    //length of image array
    double imageArrayMaxIndex = imgs.length - 1;

    //init color array
    Color[] toReturn = new Color[num];

    for (int i = 0; i < num; i++) {
      double percent = (double) i / num;
      int imageIndex = (int) (percent * imageArrayMaxIndex);

      Color a, b;

      a = imgs[imageIndex].getColor(positions[i]);

      //if the first sampled color is from the last image in the array,
      //we sample the second color from the first image.
      if (imageIndex == imageArrayMaxIndex) {
        b = imgs[0].getColor(positions[0]);
      } else {
        b = imgs[imageIndex + 1].getColor(positions[i + 1]);
      }

      toReturn[i] = ColorTools.colorLerp(a, b, fadeAmount + percent);
    }
    return toReturn;
  }
}

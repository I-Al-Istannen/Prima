package com.tiggerbiggo.prima.gui;

import com.tiggerbiggo.prima.core.Builder;
import com.tiggerbiggo.prima.processing.fragment.Fragment;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;

public class PrimaPane extends JPanel {

  private Fragment<Color[]> in;
  private Fragment<Color[]>[][] map;

  private BufferedImage[] imgs = null;

  private int counter = 0;

  private Timer t;
  Builder b = null;

  public PrimaPane(Fragment<Color[]> in) {
    super();
    this.in = in;
  }

  public PrimaPane startTimer(long ms) {
    t = new Timer();
    TimerTask tt = new TimerTask() {
      /**
       * @param
       */
      @Override
      public void run() {
        repaint();
      }
    };
    t.scheduleAtFixedRate(tt, 0, ms);

    return this;
  }

  public void stopTimer() {
    t.cancel();
    t = null;
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    if (b != null) {
      imgs = b.getImgs();
    }
    if (imgs != null) {
      g.drawImage(imgs[counter % imgs.length], 0, 0, null);
    }
    counter++;

  }

  public synchronized PrimaPane reBuild() {
    try {
      b = new Builder(in, getWidth(), getHeight(), 60);
      b.startBuild();

      //imgs = b.getImgs();
      b.joinAll();

      System.out.println("Done.");
    } catch (Exception e) {
      System.out.println("bad things");
      e.printStackTrace();
    }
    return this;
  }

  public BufferedImage[] getImgs() {
    return imgs;
  }
}

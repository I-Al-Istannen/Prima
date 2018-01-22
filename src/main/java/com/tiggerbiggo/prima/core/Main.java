package com.tiggerbiggo.prima.core;

import com.tiggerbiggo.prima.graphics.DoubleGradient;
import com.tiggerbiggo.prima.graphics.Gradient;
import com.tiggerbiggo.prima.graphics.SimpleGradient;
import com.tiggerbiggo.prima.presets.Transform;
import com.tiggerbiggo.prima.processing.ColorProperty;
import com.tiggerbiggo.prima.processing.fragment.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main
{
    public static void main(String[] args)
    {
        String outName = "pretty/";
        String extension = ".png";

        int start, end, diff;
        start=1;
        end=72;
        diff=end-start;

        BufferedImage[] images = new BufferedImage[diff];
        BufferedImage imgA, imgB;
        imgA = imgB = null;

        for(int i=0; i<diff; i++){
            try {
                images[i] = ImageIO.read(new File(outName + (i+start) + extension)); //image00XX.jpg
                //imgA = ImageIO.read(new File("space.jpg")); //image00XX.jpg
                //imgB = ImageIO.read(new File("space2.jpg"));
            } catch (IOException e) {
                System.out.println("oh no an error");
                System.exit(1);
            }
        }

        Vector2 imageSize = new Vector2(images[0].getWidth(), images[0].getHeight());
        //imageSize = Vector2.divide(imageSize, new Vector2(100));

        ArrayList<Vector2> points = new ArrayList<>();

//        for(int i=0; i<50; i++) {
//            Random r = new Random();
//            double x, y;
//            x = r.nextDouble()*2; x-=1;
//            y = r.nextDouble()*2; y-=1;
//            points.add(new Vector2(x, y));
//        }

        points.add(new Vector2());

        Fragment<Vector2> f, d;

        //f = new MapGenFragment(imageSize.minus(), imageSize);
        f = new MapGenFragment(Vector2.MINUSTWO, new Vector2(2));
        //f = new CombineFragment(f, new ConstFragment(2.8), CombineType.MULTIPLY);
        f = new TransformFragment(f, Transform.MAGNETISM);
        f = new NearestPointFragment(f, points);
        f = new TransformFragment(f, Transform.SINSIN);
        //f = new CombineFragment(f, new NoiseGenFragment(5, 0.2), CombineType.ADD);
        //f = new CombineFragment(f, new ConstFragment(new Vector2(1)), CombineType.MULTIPLY);
        //f = new MandelFragment(f, 500);
        //f = new CombineFragment(f, new ConstFragment(0.1), CombineType.MULTIPLY);
        //f = new ImageConvertFragment(imgA, f, ColorProperty.V);
        //f = new CombineFragment(f, new ConstFragment(5), CombineType.MULTIPLY);

        Fragment<Color[]> r;

        Gradient g;
        g= new DoubleGradient(Color.BLACK,  Color.blue, Color.red, true);
        //g = new SimpleGradient(Color.red, Color.MAGENTA, true);

        r = new RenderFragment(f, 60, g);
        //r = new ImageCycleFragment(images, f, 0, 0);
        //r = new ImageMapFragment(f, images[5], 60, new Vector2(100));
        //r = new FadeImageFragment(120, true, f, images);
        try
        {
            Builder b;
            //b = new Builder(render.build(new Vector2(img.getWidth()/5, img.getHeight()/5)));
            //b = new Builder(r.build(imageSize.iX(), imageSize.iY()));
            b = new Builder(r.build(300, 300));
            b.startBuild();
            b.joinAll();

            FileManager.writeGif(b.getImgs(), BufferedImage.TYPE_INT_ARGB, 0, true, "default");
            FileManager.writeGif(b.getImgs(), "morethings17");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}

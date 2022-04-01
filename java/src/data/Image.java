package data;

import ij.IJ;
import ij.ImagePlus;
import ij.process.ImageConverter;
import ij.process.ImageProcessor;

/**
 * Interface the IJ lib.
 * Used to simplify usage directives.
 */
public class Image {

    private ImagePlus imPlus;
    private ImageProcessor ip;

    public Image(String imgPath) {
        imPlus = IJ.openImage(imgPath);
        new ImageConverter(imPlus).convertToGray8();
        ip = imPlus.getProcessor();
    }

    public int getHeight() {
        return ip.getHeight();
    }

    public int getWidth() {
        return ip.getWidth();
    }

    public byte[] getPixels() {
        return (byte[]) ip.getPixels();
    }

    public void setPixels(byte[] pixels) {
        ip.setPixels(pixels);
    }

    /**
     * Launch gui from IJ to show the image.
     */
    public void show() {
        imPlus.show();
    }
}
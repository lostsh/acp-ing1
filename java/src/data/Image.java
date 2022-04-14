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

    /**
     * Save Image into file system.
     */
    public void save() {
        IJ.save(imPlus, imPlus.getOriginalFileInfo().getFilePath());
    }

    /**
     * Extract ImageVector from Image
     * @return ImageVector of the Image
     */
    public ImageVector getVector(){
        ImageVector iv = new ImageVector();
        // TODO: extract vector from image
        byte[] pixels = getPixels();

        int width = getWidth();
        int height = getHeight();

        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                //convert byte pixel value to Integer
                int pix = pixels[ i*width + j ] & 0xff;
                iv.add(pix);
            }
        }
        return iv;
    }
}
import data.Image;
import data.ImageVector;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello fraises");

        /* Just an example of how it works :
         * Use Image class to import the file
         * Then we use Image vector to manipulate Image.
         * (Once you extracted the vector from the Image,
         * you do not manipulate the Image object)
         */
        Image example = new Image("../BDD/Learn/2/20220317_132350.jpg");

        // gui from IJ lib, do not use that, its juste for example
        // to show you what it look like
        example.show();

        /* This is how to manipulate an Image
         * from grey shades Image to b&w Image
         */
        byte[] pixels = example.getPixels();

        int width = example.getWidth();
        int height = example.getHeight();

        //Iterate over the picture
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){

                //convert byte pixel value to Integer
                int pix = pixels[ i*width + j ] & 0xff;

                //if px value greater than 128 its black
                // 128 is an arbitrary value
                if (pix <= 128)
                    pixels[i * width + j] = (byte) 0; // white
                else
                    pixels[i * width + j] = (byte) 255; // black
            }
        }

        // apply modifications to Image
        example.setPixels(pixels);
    }
}
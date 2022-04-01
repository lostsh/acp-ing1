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
    }
}
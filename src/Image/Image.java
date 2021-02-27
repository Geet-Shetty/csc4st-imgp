package Image;

import Histogram.Histogram;
import Main.Global_Funcs;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;

public class Image extends ImageObjectAbstract {

    private int height;
    private int width;

    public int getHeight(){
        return height;
    }

    public int getWidth(){
        return width;
    }

    @Override
    public int[][][] getImage() {
        return image;
    }

    @Override
    public int[] getPixel(int y, int x) throws IndexOutOfBoundsException {
        int[] pixel = {image[0][y][x],image[1][y][x],image[2][y][x]};
        return pixel;
    }

    @Override
    public BufferedImage toBI() {
        BufferedImage bi0 = new BufferedImage(image[0][0].length, image[0].length, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image[0].length; ++y) {
            for (int x = 0; x < image[0][0].length; ++x) {
                int rgb =
                        (image[0][y][x] << 16) |
                                (image[1][y][x] <<  8) |
                                (image[2][y][x] <<  0);
                bi0.setRGB(x,  y,  rgb);

            }
        }
        return bi0;
    }

    @Override
    public void loadImage(String filename) throws IOException {
        BufferedImage bi = ImageIO.read(new File(filename));
        System.out.println(bi.getHeight() + "x" + bi.getWidth() + " : " + bi.getType());

        // -- BufferedImage -> int[][][]
        image = new int[3][bi.getHeight()][bi.getWidth()];
        for (int y = 0; y < bi.getHeight(); ++y) {
            for (int x = 0; x < bi.getWidth(); ++x) {
                int argb = bi.getRGB(x, y);
                image[0][y][x]  = (argb >> 16) & 0xFF; // -- RED
                image[1][y][x]  = (argb >>  8) & 0xFF; // -- GREEN
                image[2][y][x]  = (argb >>  0) & 0xFF; // -- BLUE
            }
        }

        this.height = bi.getHeight();
        this.width = bi.getWidth();
    }

    @Override
    public void saveImage(String filename, String format) throws IOException {
        ImageIO.write(toBI(), format, new File(filename)); //"PNG"
    }

    @Override
    public int[][] getComponent(COMPONENTS color) {
        switch(color){
            case RED:
                return image[0];
            case GREEN:
                return image[1];
            case BLUE:
                return image[2];
            default:
                return null;
        }
    }

    public BufferedImage toBIC(COMPONENTS Color) throws IOException {
        //BufferedImage bi0 = new BufferedImage(image[0][0].length, image[0].length, BufferedImage.TYPE_INT_RGB);
        BufferedImage bi0 = new BufferedImage(image[0][0].length, image[0].length, BufferedImage.TYPE_INT_RGB);
        int[][] img = getComponent(Color);
        for (int y = 0; y < image[0].length; ++y) {
            for (int x = 0; x < image[0][0].length; ++x) {
                //bi0.setRGB(x,  y,  image[num][y][x]<< shift);
                int rgb =
                                (img[y][x] << 16) |
                                (img[y][x] <<  8) |
                                (img[y][x] <<  0);
                bi0.setRGB(x,  y,  rgb);
            }
        }
        return bi0;
    }

    //OPERATIONS

    public void add(int r, int g, int b){
        for (int y = 0; y < image[0].length; ++y) {
            for (int x = 0; x < image[0][0].length; ++x) {
                image[0][y][x] = Global_Funcs.clamp(image[0][y][x]+r);
                image[1][y][x] = Global_Funcs.clamp(image[1][y][x]+g);
                image[2][y][x] = Global_Funcs.clamp(image[2][y][x]+b);
            }
        }
    }

    public void sub(int r, int g, int b){
        add(-r,-g,-b);
    }

    public void mult(double r, double g, double b){
        for (int y = 0; y < image[0].length; ++y) {
            for (int x = 0; x < image[0][0].length; ++x) {
                image[0][y][x] = Global_Funcs.clamp((int)(image[0][y][x]*r));
                image[1][y][x] = Global_Funcs.clamp((int)(image[1][y][x]*g));
                image[2][y][x] = Global_Funcs.clamp((int)(image[2][y][x]*b));
            }
        }
    }

    public void div(double r, double g, double b){
        mult((1.0/r),(1.0/g),(1.0/b));
    }

    public void imageop(Image n,int negate){
        int[][][] data = n.getImage();
        if(n.getHeight() == this.getHeight() && n.getWidth() == this.getWidth()) {
            for (int y = 0; y < image[0].length; ++y) {
                for (int x = 0; x < image[0][0].length; ++x) {
                    image[0][y][x] = Global_Funcs.clamp(image[0][y][x] + (data[0][y][x]*negate));
                    image[1][y][x] = Global_Funcs.clamp(image[1][y][x] + (data[1][y][x]*negate));
                    image[2][y][x] = Global_Funcs.clamp(image[2][y][x] + (data[2][y][x]*negate));
                }
            }
        }
    }

    public void addImage(Image n){
        imageop(n,1);
    }

    public void subImage(Image n){
        imageop(n,-1);
    }

    // Constast

    public void stretch(int s_min, int s_max){ // can be used for auto also
            for (int y = 0; y < image[0].length; ++y) {
                for (int x = 0; x < image[0][0].length; ++x) {
                    image[0][y][x] = Global_Funcs.clamp(Global_Funcs.scale( image[0][y][x], s_min, s_max,0,255));
                    image[1][y][x] = Global_Funcs.clamp(Global_Funcs.scale( image[1][y][x], s_min, s_max,0,255));
                    image[2][y][x] = Global_Funcs.clamp(Global_Funcs.scale( image[2][y][x], s_min, s_max,0,255));
                }
            }
    }

    public void stretch_auto(Histogram hist){
        stretch(hist.getMin(),hist.getMax());
    }

    // GAMMA

    public void applyTable(int[][] table){
        for(int i = 0; i < 3; i++){
            for (int y = 0; y < image[0].length; ++y) {
                for (int x = 0; x < image[0][0].length; ++x) {
                    image[i][y][x] = table[i][image[i][y][x]];
                }
            }
        }
    }

    public void setGamma_table(double r, double g, double b){
        int K = 256;
        int aMax = K-1;
        int[][] gamma_table = new int[3][K];

        for(int a = 0; a < K; a++){
            double aa = (double)a/aMax;

            double bb = Math.pow(aa,r);
            gamma_table[0][a] = (int) Math.round(bb*aMax);

            bb = Math.pow(aa,g);
            gamma_table[1][a] = (int) Math.round(bb*aMax);

            bb = Math.pow(aa,b);
            gamma_table[2][a] = (int) Math.round(bb*aMax);
        }
        applyTable(gamma_table);
    }

    // ALPHA BLEND

    public void blend(double alpha, Image I){
        if(alpha >= 0 && alpha <= 1.0) {
            int i_data[][][] = I.getImage();
            //out=((int) in1[x] + alpha * ((int) in2[x] - (int) in1[x]));
            for (int i = 0; i < 3; i++) {
                for (int y = 0; y < image[0].length; ++y) {
                    for (int x = 0; x < image[0][0].length; ++x) {
                        //image[i][y][x] = (int)(image[i][y][x] + alpha * (i_data[i][y][x] - image[i][y][x]));
                        image[i][y][x] = (int)((alpha * image[i][y][x]) + ((1-alpha) * i_data[i][y][x]));
                    }
                }
            }
        }
    }

    public void binarize(int threshold){
        for(int i = 0; i < 3; i++) {
            for (int j = 0; j < height; j++) {
                for (int k = 0; k < width; k++) {
                    if(image[i][j][k] < threshold){
                        image[i][j][k] = 0;
                    } else {
                        image[i][j][k] = 255;
                    }
                }
            }
        }
    }

    public double Luminance(int j, int k) {
        return 0.299 * image[0][j][k] + 0.587 * image[1][j][k] + 0.114 * image[2][j][k];
    }

    public void setLuminance(){
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                double y = Luminance(j,k);
                image[0][j][k] = (int)y;
                image[1][j][k] = (int)y;
                image[2][j][k] = (int)y;
            }
        }
    }

    public void deSaturation(double s){ // s must be between 0 and 1
        for (int j = 0; j < height; j++) {
            for (int k = 0; k < width; k++) {
                double y = Luminance(j,k);
                image[0][j][k] = (int)(y + s * ( image[0][j][k] - y));
                image[1][j][k] = (int)(y + s * ( image[1][j][k] - y));
                image[2][j][k] = (int)(y + s * ( image[2][j][k] - y));
            }
        }
    }

    public int Otsu(){
        int total = height * width;
        int top = 256;
        int wB = 0;
        double max = 0.0;

        return 0;
    }

    public static void main(String args[]){}

}
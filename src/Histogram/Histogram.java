package Histogram;

import Image.Image;
import Main.Global_Funcs;

import java.io.IOException;

public class Histogram {

    private int[][] hist = new int[3][];;
    private int[][] scaled_hist = new int[3][];;

    private double[] min = {-1,-1,-1};
    private double[] max = {-1,-1,-1};
    private double[] h_min = {-1,-1,-1};
    private double[] h_max = {-1,-1,-1};
    private double[] mode = {-1,-1,-1};
    private double[] mean = {-1,-1,-1};
    private double[] sd = {-1,-1,-1};

    private double[][] data = {min, max, mode, mean, sd};

    private int w,h;

    public void reload(Image I){
        h = I.getHeight();
        w = I.getWidth();
        setHistos(I.getImage());
        getStats();
        setScaleHistos();
    }

    private void getStats(){
            setMin();
            setMax();
            setMode();
            setMean();
            setHMin();
            setSD();
    }

    public static void setHisto(int[] channel, int[][] image){
        for (int i = 0; i < image.length; i++) {
            for (int j = 0; j < image[0].length; j++) {
                channel[image[i][j]]++;
            }
        }
    }

    private void setHistos(int[][][] image){
        for(int k = 0; k < 3; k++ ) {
            int[] histo = new int[256];
            setHisto(histo,image[k]);
            hist[k] = histo;
        }
    }

    private static double round(double d){
        return Math.floor(d * 1000) / 1000;
    }

    private void setScaleHistos(){
        int height = 100;
        for(int k = 0; k < 3; k++ ) {
            int[] histo = new int[256];
            for(int i = 0; i < hist[0].length; i++){
                histo[i] = Global_Funcs.scale(hist[k][i], (int)h_min[k], (int)h_max[k], 0, height);
            }
            scaled_hist[k] = histo;
        }
    }

    private void setMean(){
        for(int j = 0; j < 3; j++) {
            int sum = 0;
            for (int i = 0; i < hist[j].length; i++) {
                sum += (i * hist[j][i]);
            }
            mean[j] = round(sum / (w * h * 1.0));

        }
    }

    private void setSD(){
        for(int j = 0; j < 3; j++) {
            double sum = 0;
            for (int i = 0; i < hist[j].length; i++) {
                sum += Math.pow(Math.abs(i - mean[j]), 2) * hist[j][i];
            }
            sd[j] = round(Math.sqrt(sum / (w * h)));
        }
    }

    private void setMin(){
        for(int j = 0; j < 3; j++) {
            for (int i = 0; i < hist[j].length; i++) {
                if (hist[j][i] != 0) {
                    min[j] = i;
                    break;
                }
            }
        }
    }

    private void setMax(){
        for(int j = 0; j < 3; j++) {
            for (int i = hist[j].length - 1; i > -1; i--) {
                if (hist[j][i] != 0) {
                    max[j] = i;
                    break;
                }
            }
        }
    }

    private void setMode(){ // method of finding mode and h_max are the same
        for(int j = 0; j < 3; j++) {
            int max_i = 0;
            int max_v = hist[j][0];
            for (int i = 1; i < hist[j].length; i++) {
                int val = hist[j][i];
                if (val > max_v) {
                    max_i = i;
                    max_v = val;
                }
            }
            mode[j] = max_i;
            h_max[j] = max_v;
        }
    }

    private void setHMin(){
        for(int j = 0; j < 3; j++) {
            int min = hist[j][0];
            for (int i = 1; i < hist[j].length; i++) {
                if (min > hist[j][i])
                    min = hist[j][i];
            }
            h_min[j] = min;
        }
    }

    public int[][] getScaledHist(){
        return scaled_hist;
    }

    public double[][] getData(){ return data; }

    public int getMin(){
        double p = .05 * w * h;
        int sum_p = 0;
        int sum = 0;
        for(int i = 0; i < hist[1].length; i++){
            sum+=hist[1][i];
            if(sum > p ) {
                return i;
            }
        }
        return -1;
    }

    public int getMax(){
        double p = .01 * w * h;
        int sum_p = 0;
        int sum = 0;
        for(int i = hist[1].length-1; i > -1; i--){
            sum+=hist[1][i];
            if(sum > p ) {
                return i;
            }
        }
        return -1;
    }

    public void Equalization(Image I){
        int[][] H = hist.clone();
        for(int i = 0; i < 3; i++){
            for(int j = 1; j < H[0].length; j++){
                H[i][j] += H[i][j-1];
            }
        }
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < h; j++){
                for(int k = 0; k < w; k++){
                    int[][][] img = I.getImage();
                    img[i][j][k] = ((H[i][img[i][j][k]] * 255) / (w * h)); // might need a clamp
                }
            }
        }
    }

    public int[][] MatchHistos(Histogram hR) {
        double[][] PA = Cdf(hist);
        double[][] PR = Cdf(hR.hist);
        int[][] fhs = new int[3][hist[0].length];
        for(int i = 0; i < 3; i++) {
            for (int a = 0; a < hist[0].length; a++) {
                int j = hist[0].length - 1;
                do {
                    fhs[i][a] = j;
                    j--;
                } while (j > -1 && PA[i][a] <= PR[i][j]);
            }
        }
        return fhs;
    }

    public static double[][] Cdf(int[][] hist){
        double[][] P = new double[3][hist[0].length];
        for(int i = 0; i < 3; i++){
            int sum = 0;
            for(int j = 0; j < hist[0].length; j++){
                sum+=hist[i][j];
            }

            int c = hist[i][0];
            P[i][0] = (double)c/sum;
            for(int j = 1; j < hist[0].length; j++){
                c+=hist[i][j];
                P[i][j] = (double)c/sum;
            }
        }
        return P;
    }

    public static void main(String[] args) throws IOException {
    }
}

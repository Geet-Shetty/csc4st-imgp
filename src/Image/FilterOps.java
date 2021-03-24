package Image;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLOutput;
import java.util.Arrays;

public class FilterOps {

    public enum padding {IGNORE, EXTEND, MIRROR};

    public static class fileData {
        public double[][] kernel;
        public int[] origin;

        public fileData(double[][] k, int[] o){
            kernel = k;
            origin = o;
        }
    }

    public static void main(String[] args) throws IOException {
//        double[][] kernal = {{1,1,1},{1,1,1},{1,1,1}};
//        double[][] kernal = {{1},{1},{1}};
//        int[][] kernal = {{1,1,1}};
        int[][] image = {{1,2,3,4},{5,6,7,8},{9,9,9,9},{2,1,1,2}};
//        int[][] image = {{1,2,3,4},{5,6,7,8},{2,1,1,2}};
//        pad_image(image, kernal, 1,1,find_kernal_distances(kernal,1,1));
//        extend_pad(image,kernal,1,0);
        //mirror_pad(image,kernal,2,2);
//        print_2d(convolve(image,kernal,new int[]{0,0},padding.MIRROR));
        print_2d(outlier(image,new int[]{3,1},1,padding.IGNORE));

//        double[][] kernal2 = null;
//        int[] origin = null;
//        read_kernal("D:\\stuff\\image\\k.txt");
//        print_2d(kernal2);

    }

    public static void print_2d(int[][] data){
        for(int[] i : data){
            System.out.println(Arrays.toString(i));
        }
    }

    public static void print_2d(double[][] data){
        for(double[] i : data){
            System.out.println(Arrays.toString(i));
        }
    }

    // Helper Functions

    public static int[] find_kernal_distances(double[][] kernal, int row, int col){
        if((row >= kernal.length || row < 0) && (col >= kernal[0].length || col < 0)) return null;
        int left = col;
        int right = kernal[0].length-1-col;
        int up = row;
        int down = kernal.length-1-row;
        return new int[]{left,right,up,down};
    }

    public static int[] find_kernal_distances(int rowL, int colL, int row, int col){
        if((row >= rowL || row < 0) && (col >= colL || col < 0)) return null;
        int left = col;
        int right = colL-1-col;
        int up = row;
        int down = rowL-1-row;
        return new int[]{left,right,up,down};
    }

    public static int[][] create_temp_image(int[][] image, int[] kernal_positions){
        int rows = image.length+kernal_positions[2]+kernal_positions[3];
        int cols = image[0].length+kernal_positions[0]+kernal_positions[1];
        return new int[rows][cols];
    }

    public static fileData read_kernal(String path) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        String line = reader.readLine(); // reads dimensions of kernal
        double[][] kernal = null;
        int[] origin = null;

        if(line != null){
            String[] dim = line.split(" ");
            kernal = new double[Integer.parseInt(dim[0])][Integer.parseInt(dim[1])];
        }
        for(int i = 0; i < kernal.length; i++){ // will go row by row and add data to kernal
            line = reader.readLine();
            String[] row = line.split(" ");
            for(int j = 0; j < row.length; j++){
                kernal[i][j] = Double.parseDouble(row[j]);
            }
        }
        line = reader.readLine(); // reads origin of kernal
        if(line != null){
            String[] o = line.split(" ");
            origin = new int[]{Integer.parseInt(o[0]),Integer.parseInt(o[1])};
        }
        print_2d(kernal);
        System.out.println();
        System.out.println(Arrays.toString(origin));
        return new fileData(kernal,origin);
    }

    public static void linearscale(int[][] in){
        int min = in[0][0], max = min;
        for(int[] row: in){
            for(int i: row){
                min = Math.min(min,i);
                max = Math.max(max,i);
            }
        }
        for(int i = 0; i < in.length; i++){
            for(int j = 0; j < in[0].length; j++){
                in[i][j] = (in[i][j]-min)*255/(max-min);
            }
        }
    }

    // PADDINGS

    public static int[][] pad_image(int[][] image, double[][] kernal, int row, int col, int[] kdata){

        System.out.println(Arrays.toString(kdata));
        int[][] temp_i = create_temp_image(image,kdata);
        System.out.println("rows " + temp_i.length + " cols " + temp_i[0].length);
        System.out.println("start " + kdata[2] + " , " + kdata[0]);
        System.out.println("end " + (kdata[2] + image.length) + " , " + (kdata[0] + image[0].length));

        for(int i = kdata[2]; i < kdata[2]+ image.length; i++){
            for(int j = kdata[0]; j < kdata[0] + image[0].length; j++){
                temp_i[i][j] = image[i-kdata[2]][j-kdata[0]];
            }
        }
//        print_2d(temp_i);
        return temp_i;
    }

    public static int[][] extend_pad(int[][] image, double[][] kernal, int row, int col, int[] kdata){
        int[][] temp_i = pad_image(image, kernal, row, col, kdata);
        System.out.println();

        // extend left side (sideways only)
        for(int r = kdata[2]; r < kdata[2]+ image.length; r++){
            int num = temp_i[r][kdata[0]]; // starting coords
            for(int c = 0; c < kdata[0]; c++){
                temp_i[r][c] = num;
            }
        }

        // extend right side (sideways only)
        for(int r = kdata[2]; r < kdata[2]+ image.length; r++){
            int num = temp_i[r][kdata[0] + image[0].length-1]; // starting coords
            for(int c = kdata[0] + image[0].length; c < temp_i[0].length; c++){
                temp_i[r][c] = num;
            }
        }

        // extend top side (including corners)
        for(int c = 0; c < temp_i[0].length; c++){
            int num = temp_i[kdata[2]][c];
            for(int r = 0; r < kdata[2]; r++){
                temp_i[r][c] = num;
            }
        }

        // extend bot side (including corners)
        for(int c = 0; c < temp_i[0].length; c++){
            int num = temp_i[kdata[2] + image.length-1][c];
            for(int r = kdata[2] + image.length; r < temp_i.length; r++){
                temp_i[r][c] = num;
            }
        }

//        print_2d(temp_i);
        return temp_i;
    }

    public static int[][] mirror_pad(int[][] image, double[][] kernal, int row, int col, int[] kdata){
        int[][] temp_i = pad_image(image, kernal, row, col, kdata);
        System.out.println();

        // extend left side (sideways only)
        for(int r = kdata[2]; r < kdata[2]+image.length; r++){
            int count = kdata[0];
            for(int c = kdata[0]-1; c >-1; c--){
                int num = temp_i[r][count++]; // starting coords
                temp_i[r][c] = num;
            }
        }

        // extend right side (sideways only)
        for(int r = kdata[2]; r < kdata[2]+ image.length; r++){
            int count = kdata[0] + image[0].length-1;
            for(int c = kdata[0] + image[0].length; c < temp_i[0].length; c++){
                int num = temp_i[r][count--]; // starting coords
                temp_i[r][c] = num;
            }
        }

        // mirror top (including corners)
        for(int c = 0; c < temp_i[0].length; c++){
            int count = kdata[2];
            for(int r = kdata[2]-1; r >-1; r--){
                int num = temp_i[count++][c];
                temp_i[r][c] = num;
            }
        }

        // mirror bot (including corners)
        for(int c = 0; c < temp_i[0].length; c++){
            int count = kdata[2] + image.length-1;
            for(int r = kdata[2] + image.length; r < temp_i.length; r++){
                int num = temp_i[count--][c];
                temp_i[r][c] = num;
            }
        }

//        print_2d(temp_i);
        return temp_i;
    }

    // CONVOLVE

    public static int[][] convolve (int in[][], double kernel[][], int[] origin, padding p) throws IllegalArgumentException
    {
        if ((kernel.length % 2 == 0) || (kernel[0].length % 2 == 0)) {
            throw new IllegalArgumentException("kernel dimensions must be odd");
        }

        // -- assume kernel dimensions are odd // TODO: still the case
        //    the origin is the center // TODO: fixed for any origin
        //    coefficients are non-negative // TODO: fixed for negatives i think
        //    at least 1 non-zero coefficient // TODO: i think this req is fine
        int originrow = origin[0];
        int origincol = origin[1];

        int[] kdata = find_kernal_distances(kernel,origin[0],origin[1]); // left, right, up, down
        int[][] out = null;
        switch(p){
            case IGNORE:
                out = pad_image(in,kernel,origin[0],origin[1],kdata);
                break;
            case EXTEND:
                out = extend_pad(in,kernel,origin[0],origin[1],kdata);
                break;
            case MIRROR:
                out = mirror_pad(in,kernel,origin[0],origin[1],kdata);
        }
        int startr = kdata[2], startc = kdata[0];
        int endr = (kdata[2] + in.length), endc = (kdata[0] + in[0].length);

        for (int i = startr; i < endr; ++i) {
            for (int j = startc; j < endc; ++j) {
                double sum = 0.0;
                // -- loop through all kernel coefficients
                for (int k = -kdata[2]; k <= kdata[3]; ++k) {
                    for (int l = -kdata[0]; l <= kdata[1]; ++l) {
                        // -- perform the convolution operation
                        sum += out[i + k][j + l] * kernel[k + originrow][l + origincol]; // TODO: LINEARLY SCALE
                    }
                }
                in[i-startr][j-startc] = (int)sum;
            }
        }
        linearscale(in);
        System.out.println();
        return in;
    }

    public static int[][] median(int in[][], int[] lengths, padding p){ // len and origin is row,col
        if ((lengths[0] % 2 == 0) || (lengths[1] % 2 == 0)) {
            throw new IllegalArgumentException("kernel dimensions must be odd");
        }

        // -- assume kernel dimensions are odd // TODO: still the case
        //    the origin is the center // TODO: fixed for any origin
        //    coefficients are non-negative // TODO: fixed for negatives i think
        //    at least 1 non-zero coefficient // TODO: i think this req is fine
        int originrow = lengths[0]/2;
        int origincol = lengths[1]/2;

        int[] kdata = find_kernal_distances(lengths[0],lengths[1],originrow,origincol); // left, right, up, down
        int[][] out = null;
        double[][] kernel = new double[lengths[0]][lengths[1]];
        switch(p){
            case IGNORE:
                out = pad_image(in,kernel,originrow,origincol,kdata);
                break;
            case EXTEND:
                out = extend_pad(in,kernel,originrow,origincol,kdata);
                break;
            case MIRROR:
                out = mirror_pad(in,kernel,originrow,origincol,kdata);
        }
        int startr = kdata[2], startc = kdata[0];
        int endr = (kdata[2] + in.length), endc = (kdata[0] + in[0].length);

        for (int i = startr; i < endr; ++i) {
            for (int j = startc; j < endc; ++j) {
                int[] data = new int[lengths[0]*lengths[1]];
                int index = 0;
                // -- loop through all kernel coefficients
                for (int k = -kdata[2]; k <= kdata[3]; ++k) {
                    for (int l = -kdata[0]; l <= kdata[1]; ++l) {
                        // -- perform the convolution operation
                        data[index++] = out[i + k][j + l];
                    }
                }
                Arrays.sort(data);
                in[i-startr][j-startc] = data[data.length/2];
            }
        }
        System.out.println();
        return in;
    }

    public static int[][] outlier(int in[][], int[] lengths, int threshold, padding p){ // TODO: EDIT LOOPS
        if ((lengths[0] % 2 == 0) || (lengths[1] % 2 == 0)) {
            throw new IllegalArgumentException("kernel dimensions must be odd");
        }

        // -- assume kernel dimensions are odd // TODO: still the case
        //    the origin is the center // TODO: fixed for any origin
        //    coefficients are non-negative // TODO: fixed for negatives i think
        //    at least 1 non-zero coefficient // TODO: i think this req is fine
        int originrow = lengths[0]/2;
        int origincol = lengths[1]/2;

        int[] kdata = find_kernal_distances(lengths[0],lengths[1],originrow,origincol); // left, right, up, down
        int[][] out = null;
        double[][] kernel = new double[lengths[0]][lengths[1]];
        switch(p){
            case IGNORE:
                out = pad_image(in,kernel,originrow,origincol,kdata);
                break;
            case EXTEND:
                out = extend_pad(in,kernel,originrow,origincol,kdata);
                break;
            case MIRROR:
                out = mirror_pad(in,kernel,originrow,origincol,kdata);
        }
        int startr = kdata[2], startc = kdata[0];
        int endr = (kdata[2] + in.length), endc = (kdata[0] + in[0].length);

        for (int i = startr; i < endr; ++i) {
            for (int j = startc; j < endc; ++j) {
                // -- loop through all kernel coefficients
                double avg = 0;
                for (int k = -kdata[2]; k <= kdata[3]; ++k) {
                    for (int l = -kdata[0]; l <= kdata[1]; ++l) {
                        // -- perform the convolution operation
                        avg += out[i + k][j + l];
                    }
                }
                avg /= (lengths[0]*lengths[1]);
                if(Math.abs(out[i][j]-avg) > threshold){
                    in[i-startr][j-startc] = (int)avg;
                } else {
                    in[i-startr][j-startc] = out[i][j];
                }
            }
        }
        System.out.println();
        return in;
    }

    // APPLY Filters to all 3 channels in the Image

    public static void apply_convolve(Image in, double[][] kernel, int[] origin, padding p){
        for(int[][] i: in.getImage()){
            convolve(i,kernel,origin,p);
        }
    }

    public static void apply_median(Image in, int[] lengths, padding p){
        for(int[][] i: in.getImage()){
            median(i,lengths,p);
        }
    }

    public static void apply_outlier(Image in, int[] lengths, int threshold, padding p){
        for(int[][] i: in.getImage()){
            outlier(i,lengths,threshold,p);
        }
    }
    
}

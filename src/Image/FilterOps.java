package Image;

import java.sql.SQLOutput;
import java.util.Arrays;

public class FilterOps {
    public static void main(String[] args) {
        double[][] kernal = {{1,1,1},{1,1,1},{1,1,1}};
//        int[][] kernal = {{1},{1},{1}};
//        int[][] image = {{1,2,3,4},{5,6,7,8},{9,9,9,9},{2,1,1,2}};
        int[][] image = {{1,2,3,4},{5,6,7,8},{2,1,1,2}};
//        pad_image(image, kernal, 1,1,find_kernal_distances(kernal,1,1));
//        extend_pad(image,kernal,1,0);
        mirror_pad(image,kernal,1,0);
    }

    public static void print_2d(int[][] data){
        for(int[] i : data){
            System.out.println(Arrays.toString(i));
        }
    }

    public static int[] find_kernal_distances(double[][] kernal, int row, int col){
        if((row >= kernal.length || row < 0) && (col >= kernal[0].length || col < 0)) return null;
        int left = col;
        int right = kernal[0].length-1-col;
        int up = row;
        int down = kernal.length-1-row;
        return new int[]{left,right,up,down};
    }

    public static int[][] create_temp_image(int[][] image, int[] kernal_positions){
        int rows = image.length+kernal_positions[2]+kernal_positions[3];
        int cols = image[0].length+kernal_positions[0]+kernal_positions[1];
        return new int[rows][cols];
    }

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
        print_2d(temp_i);
        return temp_i;
    }

    public static int[][] extend_pad(int[][] image, double[][] kernal, int row, int col){
        int[] kdata = find_kernal_distances(kernal,row,col);
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

        print_2d(temp_i);
        return temp_i;
    }

    public static int[][] mirror_pad(int[][] image, double[][] kernal, int row, int col){
        int[] kdata = find_kernal_distances(kernal,row,col);
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

        print_2d(temp_i);
        return temp_i;
    }

}

package Image;

import java.io.IOException;

public class CafeWallProof {


    public static void getGray(Image I) throws IOException {
        int[][][] data = I.getImage();
        int[][][] result = new int[data.length][data[0].length][data[0][0].length];
        for(int y = 0; y < data[0].length; y++) {
            for(int x = 0; x < data[0][0].length; x++){
                if(data[0][y][x] == 128 && data[1][y][x] == 128 && data[2][y][x] == 128){
                    result[0][y][x] = 128;
                    result[1][y][x] = 128;
                    result[2][y][x] = 128;
                }
            }
        }
        I.setImage(result);
        I.saveImage("D:\\stuff\\image\\greytest.png","PNG");
    }

    public static void main(String[] args) throws IOException {
        Image pic = new Image();
        pic.loadImage("D:\\stuff\\image\\CafeWall.png");
        getGray(pic);
    }
}

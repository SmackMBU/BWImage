package ru.smackmbu;

import ij.IJ;
import ij.ImagePlus;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ImagePlus imp = IJ.openImage("image.png");
        int width = imp.getWidth();
        int height = imp.getHeight();
        if(width > 512 || height > 512) {
            System.out.println("the image must not be larger than 512x512");
            return;
        }
        byte[][] matrix = new byte[width][height];
        for(int i = 0; i<width; i++) {
            for (int j = 0; j < height; j++) {
                int[] array = imp.getPixel(i, j);
                matrix[i][j] = (byte)((array[0] + array[1] + array[2]) / 3);
            }
        }
        try(FileOutputStream fos = new FileOutputStream("img.bwg", false)){
            byte[] buffer = new byte[width*height+4];
            if(width <=256){
                buffer[0] = (byte)(width-1);
                buffer[1] = 0;
            }else{
                buffer[0] = (byte)255;
                buffer[1] = (byte)(width-256-1);
            }
            if(height <= 256){
                buffer[2] = (byte)(height-1);
                buffer[3] = 0;
            }else {
                buffer[2] = (byte) 255;
                buffer[3] = (byte) (height - 256 - 1);
            }
            for(int i = 0; i<width; i++) {
                for (int j = 0; j < height; j++) {
                    buffer[(i*height) + j + 4] = matrix[i][j];
                }
            }
            fos.write(buffer);
            System.out.println("Success");
            System.out.println("img.bwg created");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
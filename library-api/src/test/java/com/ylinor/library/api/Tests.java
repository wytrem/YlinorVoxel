package com.ylinor.library.api;

public class Tests {
    public static void main(String[] args) {
        int[] array = {4, 16, 13, 19, 19, 5, 4, 2, 13, 10, 10000};
        
        int optimalWidth = 1, optimalHeight = 1, optimalPos = 0, optimalSurface = 0;
        int tempSurface, tempHeight;
        
        for (int width = 1; width < array.length; width++) {
            for (int pos = 0; pos < array.length - width + 1; pos++) {
                tempHeight = min(array, pos, width);
                tempSurface = tempHeight * width;
                
                if (tempSurface > optimalSurface) {
                    optimalWidth = width;
                    optimalPos = pos;
                    optimalSurface = tempSurface;
                    optimalHeight = tempHeight;
                }
            }
        }
        
        System.out.println("Le plus grand rectangle est de " + optimalWidth + "x" + optimalHeight + " à la position " + optimalPos);
    }
    
    /**
     * @return le minimum du sous tableau commençant à {@code startIndex} de taille {@code length}
     */
    public static int min(int[] array, int startIndex, int length) {
        int min = array[startIndex];
        
        for (int i = startIndex + 1; i < startIndex + length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        
        return min;
    }
}

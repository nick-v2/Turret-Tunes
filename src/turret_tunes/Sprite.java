/*
 * Copyright (C) 2018 Nick Vocaire
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package turret_tunes;

import java.awt.image.BufferedImage;

/**
 * Class for creating sprites, tiles that have multiple frames
 * @author Nick Vocaire
 */
public class Sprite {
    int x,y,width,height,currentFrame = 0;
    BufferedImage frames[];
    BufferedImage frame;
    boolean loop = false; //A queue telling the main method to loop through this sprite
    
    /**
     * Constructor for making a basic sprite
     * @param x X coordinate in the program
     * @param y Y coordinate in the program
     * @param frames BufferedImages of the sprite
     * @param width the width of the tile
     * @param height the height of the tile
     */
    public Sprite(int x, int y, int width, int height, BufferedImage frames[]) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.frames = frames;
        frame = frames[currentFrame];
    }
    
    /**
     * Method for iterating through the frames of the sprite
     */
    public void nextFrame(){
        currentFrame++;
        if(currentFrame>=frames.length) {
            currentFrame = 0;
            loop = false;
        }
        frame = frames[currentFrame];
    }
    
    /**
     * Method for skipping to a certain frame
     * @param x the frame to skip to
     */
    public void skipToFrame(int x){
        currentFrame = x;
        frame = frames[currentFrame];
    }
}
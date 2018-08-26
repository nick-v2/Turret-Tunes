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
 * Class for creating tiles, images with a x value, a y value, width, height, and visibility
 * @author Nick Vocaire
 */
public class Tile {
    int x,y,width,height;
    int deX,deY,deWidth,deHeight; //Default stats
    boolean visible;
    BufferedImage i;
    
    /**
     * Constructor for making a basic tile
     * @param x X coordinate in the program
     * @param y Y coordinate in the program
     * @param i BufferedImage of the tile
     */
    public Tile(int x, int y, BufferedImage i) {
        this.x = x;
        this.y = y;
        this.i = i;
    }
    
    /**
     * Constructor for making a basic tile with a visibility
     * @param x X coordinate in the program
     * @param y Y coordinate in the program
     * @param i BufferedImage of the tile
     * @param visible boolean for if the tile is visible
     */
    public Tile(int x, int y, BufferedImage i, boolean visible) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.visible = visible;
    }
    
    /**
     * Constructor for making a basic tile with width, height, and vidibility
     * @param x X coordinate in the program
     * @param y Y coordinate in the program
     * @param i BufferedImage of the tile
     * @param width the width of the tile
     * @param height the height of the tile
     * @param visible boolean for if the tile is visible
     */
    public Tile(int x, int y, int width, int height, BufferedImage i, boolean visible) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        deX = x;
        deY = y;
        deWidth = width;
        deHeight = height;
        this.i = i;
        this.visible = visible;
    }
}
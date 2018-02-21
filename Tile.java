import java.awt.image.BufferedImage;
public class Tile {
    int x,y,width,height;
    int deX,deY,deWidth,deHeight; //Default stats
    boolean visible;
    BufferedImage i;
    public Tile(int x, int y, BufferedImage i) {
        this.x = x;
        this.y = y;
        this.i = i;
    }
    public Tile(int x, int y, BufferedImage i, boolean visible) {
        this.x = x;
        this.y = y;
        this.i = i;
        this.visible = visible;
    }
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
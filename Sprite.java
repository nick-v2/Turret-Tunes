import java.awt.image.BufferedImage;
public class Sprite {
    int x,y,width,height,currentFrame = 0;
    BufferedImage frames[];
    BufferedImage frame;
    boolean loop = false;
    public Sprite(int x, int y, int width, int height, BufferedImage frames[]) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.frames = frames;
        frame = frames[currentFrame];
    }
    public void nextFrame(){
        currentFrame++;
        if(currentFrame>=frames.length) {
            currentFrame = 0;
            loop = false;
        }
        frame = frames[currentFrame];
    }
    public void skipToFrame(int x){
        currentFrame = x;
        frame = frames[currentFrame];
    }
}
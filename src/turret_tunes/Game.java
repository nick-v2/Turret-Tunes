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

import javax.swing.JFrame; //B4-C6, oooooh
import javax.swing.JPanel;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.FloatControl;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Font;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

/**
 * A game based in the portal universe created by VALVE, create music as the turrets
 * @author Nick Vocaire
 */
public class Game extends Thread{
    public static final long T_FPS = 30; //Target FPS, Large values make system less percise because of Thread.sleep
    public static final int NUMBER_BLANKS = 4; //number of relativly blank tiles to scroll before putting a special one
    public static final int WINDOW_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 800;

    public static boolean menu = true, start = false, tBackground = false, doorsOpen = false, help = true, update = false;
    public static int blanks = 4; //number of blank tiles that start on screen
    public static int tileOnTop = 0; //Scrolling tile that is the highest up
    public static int fpsToUpdate = 4; //Amount of fps per update to change sprites
    public static int fps;
    public static JPanel panel; //Panel to repaint
    public static JFrame game; //Frame
    public static Clip menuMusic;
    public static Clip noteClips[] = new Clip[9]; //Notes
    public static FloatControl menuVolume;
    public static FloatControl[] noteVolumes = new FloatControl[9];
    public static Font font = new Font("Impact", Font.BOLD, 22); //Font of keys
    public static BufferedImage elevator,background,spriteSheet,turretBackground,panels,title;
    public static BufferedImage[] backgroundTiles = new BufferedImage[4];
    public static BufferedImage[] normal = new BufferedImage[4]; //normal turret frames
    public static BufferedImage[] big = new BufferedImage[4]; //Big turret frames
    public static Tile sb[] = new Tile[4]; //Scrolling images
    public static Tile buttons[] = new Tile[5]; //Buttons
    public static Tile elevatorDoors[] = new Tile[2]; //the doors
    public static Tile extraPanels[] = new Tile[2]; //options and controls
    public static Tile[] checkBoxes = new Tile[2];
    public static Tile[] volume = new Tile[2];
    public static Sprite normalTurrets[] = new Sprite[8];
    public static Sprite bigTurret;
    /**
     * Loads menu
     * 
     * @param a the command line arguments
     */
    public static void main(String a[]){
        game = new JFrame("Turret Tunes    FPS: ");
        panel = new Panel();

        loadImages();
        loadScrollingImages();
        loadNotes();
        playMenuMusic();
        setVolume();

        game.setSize(WINDOW_WIDTH, WINDOW_HEIGHT + 30);
        game.add(panel);
        game.addKeyListener(new Keyboard());
        game.addMouseListener(new Mouse());
        game.addMouseMotionListener(new MouseMotion());
        game.setVisible(true);
        game.setResizable(false);
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Thread loop = new Game();
        loop.start();
    }
    
    /**
     * Set volume of game
     */
    public static void setVolume() {
        for(int i = 0; i < noteVolumes.length; i++) {
            noteVolumes[i].setValue((volume[1].x-475)/3);
        }
        menuVolume.setValue((volume[1].x-475)/3);
    }
    
    /**
     * Load the note files for the turrets
     */
    public static void loadNotes(){
        File notes[] = new File[9];
        AudioInputStream audioStreams[] = new AudioInputStream[9];
        notes[0] = new File("Sounds/B4.wav");
        notes[1] = new File("Sounds/C5.wav");
        notes[2] = new File("Sounds/D5.wav");
        notes[3] = new File("Sounds/E5.wav");
        notes[4] = new File("Sounds/F5.wav");
        notes[5] = new File("Sounds/G5.wav");
        notes[6] = new File("Sounds/A5.wav");
        notes[7] = new File("Sounds/B5.wav");
        notes[8] = new File("Sounds/C6.wav");
        try{
            for(int i = 0; i<notes.length;i++){
                audioStreams[i] = AudioSystem.getAudioInputStream(notes[i]);
                noteClips[i] = AudioSystem.getClip();
                noteClips[i].open(audioStreams[i]);
                noteVolumes[i] = (FloatControl) noteClips[i].getControl(FloatControl.Type.MASTER_GAIN);
            }
        } catch(UnsupportedAudioFileException e){}
          catch(IOException e){}
          catch(LineUnavailableException e){}
    }
    
    /**
     * Starts the menu music
     */     
    public static void playMenuMusic(){
        File mMusic = new File("Sounds/menu.wav");
        try{
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(mMusic);
            menuMusic = AudioSystem.getClip();
            menuMusic.open(audioIn);
            menuMusic.start();
            menuMusic.loop(10); //Loop 10 times
        } catch(UnsupportedAudioFileException e){}
          catch(IOException e){}
          catch(LineUnavailableException e){}
        menuVolume = (FloatControl) menuMusic.getControl(FloatControl.Type.MASTER_GAIN);
    }
    
    /**
     * Loads the images for the scrolling menu
     */
    public static void loadScrollingImages() {
        for(int i = 0; i <sb.length; i++){ //Set the original background tiles for the scrolling
            sb[i] = new Tile(0,i*200-200,backgroundTiles[3]);
        }
    }
    
    /**
     * Loads the images for the assets in the game
     */
    public static void loadImages() {
        File e = new File("Images/Elevator.png");
        File b = new File("Images/Background.png");
        File s = new File("Images/Sprites.png");
        File t = new File("Images/turretBackground.png");
        File p = new File("Images/Panels.png");
        try {
            elevator = ImageIO.read(e);
            background = ImageIO.read(b);
            spriteSheet = ImageIO.read(s);
            turretBackground = ImageIO.read(t);
            panels = ImageIO.read(p);
        } catch (IOException i) {
        }
        
        title = spriteSheet.getSubimage(100,600,700,100);
        checkBoxes[0] = new Tile(500,215,50,50,spriteSheet.getSubimage(0,700,100,100), false);
        checkBoxes[1] = new Tile(500,215,50,50,spriteSheet.getSubimage(100,700,100,100), false);
        volume[1] = new Tile(420,330,50,50,spriteSheet.getSubimage(200,700,100,100), false);
        volume[0] = new Tile(300,330,200,75,spriteSheet.getSubimage(300,700,300,100), false);
        extraPanels[0] = new Tile(200,150,panels.getSubimage(0,0,400,300), false); //Options 
        extraPanels[1] = new Tile(200,150,panels.getSubimage(0,300,400,300), false); //Controls
        elevatorDoors[0] = new Tile(0,0,elevator.getSubimage(0,0,400,600));
        elevatorDoors[1] = new Tile(400,0,elevator.getSubimage(400,0,400,600));

        for(int i = 0; i <backgroundTiles.length; i++){ //Split the background into the tiles
            backgroundTiles[i] = background.getSubimage(0,i*200,800,200);
        }

        for(int i = 0; i <normal.length; i++){ //Split the sprite into normalTurrent Frames
            normal[i] = spriteSheet.getSubimage(i*200,100,200,250);
        }

        for(int i = 0; i <big.length; i++){ //Split the sprite into bigTurrent Frames
            big[i] = spriteSheet.getSubimage(i*200,350,200,250);
        }

        buttons[0] = new Tile(300,150,200,80,spriteSheet.getSubimage(0,0,240,100), true); //Start
        buttons[1] = new Tile(270,260,260,80,spriteSheet.getSubimage(240,0,300,100), true); //Options
        buttons[2] = new Tile(290,360,220,80,spriteSheet.getSubimage(540,0,260,100), true); //Controls
        buttons[3] = new Tile(530,390,40,40,spriteSheet.getSubimage(0,600,100,100), false); //Back
        buttons[4] = new Tile(740,10,40,40,spriteSheet.getSubimage(0,600,100,100), false); //Back

        for(int i = 0; i<normalTurrets.length-4; i++){ //Set up secound row of turrets
            normalTurrets[i] = new Sprite(i*200+60,175-800,100,125,normal);
        }
        for(int i = 0; i<normalTurrets.length-4; i++){ //Set up first row of turrets
            normalTurrets[i+4] = new Sprite(i*200+60,550-800,100,125,normal);
        }
        normalTurrets[5].y = 530-800;
        normalTurrets[6].y = 530-800; //For moving some turrets up a little
        normalTurrets[1].y = 155-800;
        normalTurrets[2].y = 155-800;
        bigTurret = new Sprite(350,310-800,100,125,big); //Setup the big turret
    }
    
    /**
     * The method executed when a game thread is created and run
     */
    public void run() {
        fps = 0;
        long lastLoopTime = System.nanoTime();
        long fpsTimer = 0;
        final long OPTIMAL_TIME = 1000000000 / T_FPS; //Optimal time between each loop
        boolean running = true;

        while(running) {
            long now = System.nanoTime();
            long loopTime = now - lastLoopTime; //How much time it took the loop to cycle, includes sleeping
            lastLoopTime = now; //Last time loop took place
            double deltaLoop = loopTime / ((double)OPTIMAL_TIME); //How close the loop was to the optimal time
            //(Value of 1 is perfect, slow>1, fast<1)
            fpsTimer += loopTime;
            fps++;

            if (fpsTimer >= 1000000000) { //If its been 1 sec, shows the frames in that secound
                game.setTitle("Turret Tunes    FPS: " + fps);
                fpsTimer = 0;
                fps = 0;
            }

            update(deltaLoop); //Update, pass in the delta loop to change calculations based on lag if necessary

            now = System.nanoTime();
            long updateTime = now - lastLoopTime; //Similar to loop time but does not include the sleep,
            //only the update method call and the small code before

            long timeToSleep = (OPTIMAL_TIME - updateTime)/1000000;
            //Takes how long the update took, subtracts it from the optimal time and divides by 1000000 to get
            //how long to sleep in millisecounds (depending on lag, changes loop speed. More lag, smaller sleep).
            //Because of percision lost from dividing a long, fps is usually over what is set

            if(timeToSleep > 0) {//If update is taking super long (timeToSleep is negetive), dont sleep
                try {
                    Thread.sleep(timeToSleep);
                } catch (InterruptedException i) {
                    System.err.println("Loop could not sleep");
                }
            }
        }
    }
    
    /**
     * The method executed T_FPS per second
     * @param d the delta of how far off the update call was to the optimal time between calls
     */
    public void update(double d) {
        if(fps%fpsToUpdate == 0) {//If the fpsToUpdates converter evenly divides into fps, increment updates
            update = true;
        }
        if(menu) 
            menuScroll();
        if(start && menuVolume.getValue() >= -79) //Decrease menu music
            menuVolume.setValue(menuVolume.getValue()-0.15f);
        if(!doorsOpen && !menu) {
            openDoors();
            randomMovements();
            if(update) { //If update is true, loop through the sprites once
                loopSprites();
                update = false;
            }
        }
        if(doorsOpen) {
            menuMusic.stop();
            randomMovements();
            buttons[4].visible = true;
            if(update) {
                loopSprites(); //If update is true, loop through the sprites once
                update = false;
            }
        }
        panel.repaint();
    }
    
    /**
     * Loops through the turrets sprite frames
     */
    public void loopSprites() {
        for(int i = 0; i<normalTurrets.length; i++) { //If the sprites loop is true, loop through it
           if(normalTurrets[i].loop) {
              normalTurrets[i].nextFrame();
           }
        }
        if(bigTurret.loop) {
           bigTurret.nextFrame();
        }
    }
    
    /**
     * Randomly open and close turrets when idling
     */
    public void randomMovements() { //Random movements while they wait for you to play music
        int turret = (int)(Math.random() * 9);
        int move = (int)(Math.random() * 100);
        if(move>=90){
            int frame = (int)(Math.random() * 10);
            if(frame <=8) //Make it more likely to close the turrets side
                frame = 0;
            else
                frame = 1;
            if(turret == 8) {
                bigTurret.skipToFrame(frame);
            } else {
                normalTurrets[turret].skipToFrame(frame);
            }
        }
    }
    
    /**
     * Opens the doors of the elevator
     */
    public void openDoors(){
        if(elevatorDoors[0].x+400<=0) //If the door is off the screen
            doorsOpen = true;
        elevatorDoors[0].x-=8;
        elevatorDoors[1].x+=8;
    }
    
    /**
     * Scrolls the menu tiles
     */
    public void menuScroll() {
        for(int i = 0; i < sb.length; i++){ //If a scrolling tile passes the bottom of the screen, loop and pick another
            if(sb[i].y>=WINDOW_HEIGHT) {
                if(!start) { //Stop adding new tiles after start is pressed
                    if(blanks >= NUMBER_BLANKS) {
                        int rTile = (int)(Math.random() * 3);
                        sb[i] = new Tile(0,-200,backgroundTiles[rTile]);
                        tileOnTop = i;
                        blanks = 0;
                    } else {
                        sb[i] = new Tile(0,-200,backgroundTiles[3]); //Blank Tile
                        tileOnTop = i;
                        blanks++;
                    }
                } else if(sb[tileOnTop].y>=WINDOW_HEIGHT+196) { //When the tile on top passes, stop scrolling
                    menu = false;
                }
                if(start && !tBackground){ //If the turret background has not been swapped in yet, swap it in
                    sb[i] = new Tile(0,-800,turretBackground);
                    tBackground = true;
                }
            }
        }
        for(int i = 0; i < sb.length; i++) { //Scroll the tiles down
            sb[i].y+= 4; //certain speeds cause tiles to not overlap
        }
        if(tBackground) {//If the background is switched, scroll turrets too
            for(int i = 0; i < normalTurrets.length; i++) { 
                normalTurrets[i].y+= 4;
            }
            bigTurret.y+= 4;
        }
    }
    
    /**
     * Class to extend JPanel such that graphics can be painted on the screen
     */
    private static class Panel extends JPanel {
        
        /**
         * Method for painting to the screen when the repaint method is called
         * @param g graphics object passed in from java
         */
        public void paintComponent(Graphics g) {
            for(int i = 0; i < sb.length; i++) { //Print all the background tiles
                g.drawImage(sb[i].i,sb[i].x,sb[i].y,null);
            }

            if(start) { //Only draw after start is pressed
                for(int i = 0; i < normalTurrets.length; i++) { //Print all the turrets
                    g.drawImage(normalTurrets[i].frame,normalTurrets[i].x,normalTurrets[i].y-normalTurrets[i].height,normalTurrets[i].width,normalTurrets[i].height,null);
                }
                g.drawImage(bigTurret.frame,bigTurret.x,bigTurret.y-bigTurret.height,bigTurret.width,bigTurret.height,null);
            }
            
            if(!menu && help) { //Print keys when the doors open
                g.setColor(Color.WHITE);
                g.setFont(font);
                for(int i = 0; i<4; i++){ //Set up first row of keys
                    g.drawString("" + (i+1),i*200+100,405);
                }
                for(int i = 0; i<4; i++){ //Set up secound row of keys
                    g.drawString("" + (i+5),i*200+100,30);
                }
                g.drawString("9",390,175); //bigturret
            }
            
            for(int i =0; i<elevatorDoors.length; i++) { //Elevator doors
                g.drawImage(elevatorDoors[i].i,elevatorDoors[i].x,elevatorDoors[i].y,null); //Draw elevator over background
            }
            
            if(!start) { //Only draw before start is pressed
                for(int i = 0; i <extraPanels.length; i++) { //Print all Panels if thier visible
                    if(extraPanels[i].visible)
                        g.drawImage(extraPanels[i].i,extraPanels[i].x,extraPanels[i].y,null);
                }
                for(int i = 0; i <2; i++) { //Print all checkBoxes and volume stuff if thier visible
                    if(checkBoxes[i].visible)
                        g.drawImage(checkBoxes[i].i,checkBoxes[i].x,checkBoxes[i].y,checkBoxes[i].width,checkBoxes[i].height,null);
                    if(volume[i].visible)
                        g.drawImage(volume[i].i,volume[i].x,volume[i].y,volume[i].width,volume[i].height,null);
                } 
                for(int i = 0; i <buttons.length-1; i++) { //Print all buttons except homescreen back button
                    if(buttons[i].visible)
                        g.drawImage(buttons[i].i,buttons[i].x,buttons[i].y,buttons[i].width,buttons[i].height,null);
                }
                g.drawImage(title,50,25,null); //Print title
            }
            if(buttons[4].visible){
                g.drawImage(buttons[4].i,buttons[4].x,buttons[4].y,buttons[4].width,buttons[4].height,null);
            }
        }
    }
    
    /**
     * Class to extend MouseListener such that code can be executed upon certain mouse actions
     */
    private static class Mouse implements MouseListener {
        
        /**
         * Method called when the mouse is pressed
         * @param e MouseEvent object passed in from java
         */
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if(!start) { //Only check before start is pressed
                if((x>checkBoxes[0].x && x<checkBoxes[0].x+checkBoxes[0].width) && (y>checkBoxes[0].y + 30 && y<checkBoxes[0].y+checkBoxes[0].height + 30)){
                    if(help){ //For setting the keys to on or off depending on if the checkbox is checked
                        help = false;
                        checkBoxes[0].visible = true;
                        checkBoxes[1].visible = false;
                    }
                    else{
                        help = true;
                        checkBoxes[0].visible = false;
                        checkBoxes[1].visible = true;
                    }
                }
                for(int i = 0; i <buttons.length; i++) {
                    if((x>buttons[i].x && x< buttons[i].x+buttons[i].width) && (y>buttons[i].y + 30 && y< buttons[i].y+buttons[i].height + 30)) {
                        if(i == 0 && buttons[3].visible == false) { //Start button
                            start = true;
                        }
                        if(i == 1 && buttons[3].visible == false) { //Options button
                            extraPanels[0].visible = true;
                            buttons[0].visible = false;
                            buttons[1].visible = false;
                            buttons[2].visible = false;
                            buttons[3].visible = true;
                            volume[0].visible = true;
                            volume[1].visible = true;
                            if(help)
                                checkBoxes[1].visible = true;
                            else
                                checkBoxes[0].visible = true;
                        }
                        if(i == 2 && buttons[3].visible == false) { //Controls button
                            extraPanels[1].visible = true;
                            buttons[0].visible = false;
                            buttons[1].visible = false;
                            buttons[2].visible = false;
                            buttons[3].visible = true;
                        }
                        if(i == 3 && buttons[3].visible == true) { //Back button
                            extraPanels[0].visible = false;
                            extraPanels[1].visible = false;
                            buttons[0].visible = true;
                            buttons[1].visible = true;
                            buttons[2].visible = true;
                            buttons[3].visible = false;
                            volume[0].visible = false;
                            volume[1].visible = false;
                            checkBoxes[0].visible = false;
                            checkBoxes[1].visible = false;
                        }
                    }
                }
            }
            if(buttons[4].visible) { //When the back button is hit to return to menu (RESETS EVERYTHING
                if((x>buttons[4].x && x< buttons[4].x+buttons[4].width) && (y>buttons[4].y + 30 && y< buttons[4].y+buttons[4].height + 30)) {
                     loadScrollingImages();
                     menuMusic.setMicrosecondPosition(0);
                     menuMusic.start();
                     menuVolume.setValue(-20f);
                     setVolume();
                     elevatorDoors[0].x = 0;
                     elevatorDoors[1].x = 400;
                     for(int i = 0; i<normalTurrets.length; i++){ //Set up secound row of turrets
                         normalTurrets[i].y = normalTurrets[i].y-800;
                     }
                     bigTurret.y = bigTurret.y-800;
                     buttons[0].visible = true;
                     buttons[1].visible = true;
                     buttons[2].visible = true;
                     buttons[4].visible = false;
                     menu = true;
                     start = false;
                     tBackground = false;
                     doorsOpen = false;
                     update = false;
                }
            }
        }
        /**
         * Method called when the mouse is released
         * @param e MouseEvent object passed in from java
         */
        public void mouseReleased(MouseEvent e) {}
        /**
         * Method called when the mouse enters the program
         * @param e MouseEvent object passed in from java
         */
        public void mouseEntered(MouseEvent e) {}
        /**
         * Method called when the mouse exitsd the program
         * @param e MouseEvent object passed in from java
         */
        public void mouseExited(MouseEvent e) {}
        /**
         * Method called when the mouse is clicked
         * @param e MouseEvent object passed in from java
         */
        public void mouseClicked(MouseEvent e) {}
    }
    
    /**
     * Class to extend MouseMotionListener such that code can be executed upon certain mouse movement actions
     */
    private static class MouseMotion implements MouseMotionListener {
        /**
         * Method called when the mouse is moved
         * @param e MouseEvent object passed in from java
         */
        public void mouseMoved(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if(!start) { //Only enlarge before start is pressed
                for(int i = 0; i <buttons.length-1; i++) { //For enlarging buttons
                    if((x>buttons[i].x && x< buttons[i].x+buttons[i].width) && (y>buttons[i].y + 30 && y< buttons[i].y+buttons[i].height + 30)){
                        buttons[i].width = buttons[i].deWidth + 10;
                        buttons[i].height = buttons[i].deHeight + 10;
                        buttons[i].x = buttons[i].deX - 5;
                        buttons[i].y = buttons[i].deY - 5;
                    } else {
                        buttons[i].width = buttons[i].deWidth;
                        buttons[i].height = buttons[i].deHeight;
                        buttons[i].x = buttons[i].deX;
                        buttons[i].y = buttons[i].deY;
                    }
                }
            }
            if(buttons[4].visible) { //For the back button to the menu
                if((x>buttons[4].x && x< buttons[4].x+buttons[4].width) && (y>buttons[4].y + 30 && y< buttons[4].y+buttons[4].height + 30)){
                   buttons[4].width = buttons[4].deWidth + 10;
                   buttons[4].height = buttons[4].deHeight + 10;
                   buttons[4].x = buttons[4].deX - 5;
                   buttons[4].y = buttons[4].deY - 5;
                } else {
                   buttons[4].width = buttons[4].deWidth;
                   buttons[4].height = buttons[4].deHeight;
                   buttons[4].x = buttons[4].deX;
                   buttons[4].y = buttons[4].deY;
                    }
            }
        }
        /**
         * Method called when the mouse is dragged
         * @param e MouseEvent object passed in from java
         */
        public void mouseDragged(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            if(!start) { //Only move slider if not start
                if((x>volume[1].x + 10 && x<volume[1].x+volume[1].width-10) && (y>volume[1].y + 20 && y<volume[1].y+volume[1].height + 20)) {
                    volume[1].x = x-20;
                    if(volume[1].x<275)
                        volume[1].x = 275;
                    if(volume[1].x>475)
                        volume[1].x = 475;
                    setVolume();
                }
            }
        }
    }
    /**
     * Class that extend KetListener such that code can be executed when certain keyboard actions occur
     */
    private static class Keyboard implements KeyListener {
        
        /**
         * Method called when a key is typed
         * @param e KeyEvent object passed in from java
         */
        public void keyTyped(KeyEvent e) {}
        /**
         * Method called when a key is pressed
         * @param e KeyEvent object passed in from java
         */
        public void keyPressed(KeyEvent e) {
            if(!menu) {
                switch(e.getKeyCode()) {
                    case KeyEvent.VK_1:
                     noteClips[0].stop();
                        noteClips[0].flush();
                        noteClips[0].setMicrosecondPosition(0);
                        noteClips[0].start();
                        normalTurrets[4].loop = true;
                        break;
                    case KeyEvent.VK_2:
                        noteClips[1].stop();
                        noteClips[1].flush();
                        noteClips[1].setMicrosecondPosition(0);
                        noteClips[1].start();
                        normalTurrets[5].loop = true;
                        break;
                    case KeyEvent.VK_3:
                        noteClips[2].stop();
                        noteClips[2].flush();
                        noteClips[2].setMicrosecondPosition(0);
                        noteClips[2].start();
                        normalTurrets[6].loop = true;
                        break;
                    case KeyEvent.VK_4:
                        noteClips[3].stop();
                        noteClips[3].flush();
                        noteClips[3].setMicrosecondPosition(0);
                        noteClips[3].start();
                        normalTurrets[7].loop = true;
                        break;
                    case KeyEvent.VK_5:
                        noteClips[4].stop();
                        noteClips[4].flush();
                        noteClips[4].setMicrosecondPosition(0);
                        noteClips[4].start();
                        normalTurrets[0].loop = true;
                        break;
                    case KeyEvent.VK_6:
                        noteClips[5].stop();
                        noteClips[5].flush();
                        noteClips[5].setMicrosecondPosition(0);
                        noteClips[5].start();
                        normalTurrets[1].loop = true;
                        break;
                    case KeyEvent.VK_7:
                        noteClips[6].stop();
                        noteClips[6].flush();
                        noteClips[6].setMicrosecondPosition(0);
                        noteClips[6].start();
                        normalTurrets[2].loop = true;
                        break;
                    case KeyEvent.VK_8:
                        noteClips[7].stop();
                        noteClips[7].flush();
                        noteClips[7].setMicrosecondPosition(0);
                        noteClips[7].start();
                        normalTurrets[3].loop = true;
                        break;
                    case KeyEvent.VK_9:
                        noteClips[8].stop();
                        noteClips[8].flush();
                        noteClips[8].setMicrosecondPosition(0);
                        noteClips[8].start();
                        bigTurret.loop = true;
                        break;
                }
            }
        }
        /**
         * Method called when a key is released
         * @param e KeyEvent object passed in from java
         */
        public void keyReleased(KeyEvent e) {}
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Random;

/*******************
 * User: Micah Kline
 * Date: 8/2/13
 * Time: 9:44 PM
 * TODO-LIST
 * 0) Save High Score to File.
 * 1) Create Levels
 * 2) Create Different Speeds
 * 3) Create Better Apple Graphics
 * 4) Double Buffer Images for better Graphic Control
 * 5) Create Better Snake Graphics
 *
 *
 ********************/

public class GameBoard extends JPanel implements Runnable{


    //Snake List
    LinkedList<Snake> snake = new LinkedList<Snake>();
    // Food List
    LinkedList<Apple> apple = new LinkedList<Apple>();

    // Game Thread
    Thread gameLoop;

    Boolean isDemoMode = false;
    Boolean isPause = false;
    Boolean isStarted = false;
    Boolean isKeyPress = false;

    // Direction Traveling
    Boolean EAST = false;
    Boolean WEST = false;
    Boolean NORTH = false;
    Boolean SOUTH = false;
    String direction = "EAST";

    // This is used for snake Demo AI need to rename this To something else
    Boolean isEqual = false;

    //Score Keeping
    int gameLevel = 1;
    int gameScore = 0;
    int gameHighScore = 0;

    // How long to sleep thread
    // this is not the best way to handle interrupting a thread
    // It works for now
    int speed = 50;

    // Not needed for this implementation Used for double Buffered Images
//    AffineTransform transform = new AffineTransform();
//    Graphics2D mainGraphics;
//    BufferedImage backBuffer;


    public GameBoard(){
        // Setup Game Board
        this.setPreferredSize( new Dimension(200,200));
        this.setMinimumSize(this.getPreferredSize());
        this.setMaximumSize(this.getPreferredSize());
        this.setBackground( Color.black );
        // Add a listener to allow for KeyBoard Input
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);

                if (!isKeyPress) {
                    switch (e.getKeyCode()) {

                        case 37: //Left Arrow Key
                            if (!EAST && isDemoMode != true) {
                                direction = "WEST";
                                isKeyPress = true;
                            }
                            break;

                        case 38://Up Arrow Key
                            if (!SOUTH && isDemoMode != true) {
                                direction = "NORTH";
                                isKeyPress = true;
                            }
                            break;

                        case 39: //Right Arrow Key
                            if (!WEST && isDemoMode != true) {
                                direction = "EAST";
                                isKeyPress = true;
                            }
                            break;

                        case 40: //Down Arrow Key
                            if (!NORTH && isDemoMode != true) {
                                direction = "SOUTH";
                                isKeyPress = true;
                            }
                            break;
                        case 114: //F3 pauses and resumes Game
                            if (isStarted && isDemoMode != null && isDemoMode != true) {
                                isStarted = false;
                                isPause = true;
                                break;
                            } else {
                                isStarted = true;
                                isPause = false;
                                ResumeGame();
                                break;
                            }
                        case 113: // F2 Starts a New Game
                            isDemoMode = false;
                            isPause = false;
                            isStarted = false;
                            StartNewGame();

                        default:
                            //Prints Key Code of Key pressed
                            //System.out.println(e.getKeyCode());
                            break;
                    }
                }
            }
        });
        this.setFocusable(true);
        this.requestFocusInWindow();
        //board.requestFocus();
        this.setVisible(true);

        // Load Snake and Apple With Initial Values
        InitSnake();
        InitApple();
        // Load Demo Mode
        LoadDemoMode();

    }
    // Starts a new Game Resets Snake and Apple
    public void StartNewGame(){
        gameScore = 0;
        isStarted = true;
        resetSnake();
        InitApple();
        gameLoop = new Thread(this);
        gameLoop.start();
    }
    // Resumes the Current Game
    public void ResumeGame(){
        gameLoop = new Thread(this);
        gameLoop.start();
    }
    // Start the Game Thread And Runs DemoMode
    public void LoadDemoMode(){
        isDemoMode = true;
        isStarted = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }
    // Create an Apple and Pick a new Random Location to place it
    public void InitApple(){
        apple.clear();
        apple.add(new Apple(-10,-10));
        newApple();

    }

    public void CollisionDetect(){
    // check for Simple Collision with Apple and Snake
        int x = snake.get(0).getX();
        int y = snake.get(0).getY();
        int x1 = apple.get(0).getX();
        int y1 = apple.get(0).getY();

        // Checks For Collision With Apple
        if( x == x1 && y == y1){
          newApple();
          gameScore = gameScore + 100;
          snake.add(new Snake(-10, -100));
    }
        // check snake Collision
        for(int index = snake.size()-1; index > 1; index--){
            if( x == snake.get(index).getX() && y == snake.get(index).getY()){
                isDemoMode = true;
                resetSnake();
                break;
            }
        }
        // Add code to check for wall Collision
    }
    // this method should drop a new apple onto the screen
    public void newApple(){
        Random rand = new Random();

        apple.get(0).setX(rand.nextInt((40))*10+10);
        apple.get(0).setY(rand.nextInt((40))*10+10);
        // TODO
        // Need to check and see if apple was dropped on the snake
        // if it was Try get a new Apple
    }
    // Reset Snake and change high score if Applicable
    public void resetSnake(){
        snake.clear();
        isEqual = false;
        direction = "EAST";
        isKeyPress = true;
        if(gameScore > gameHighScore){
            gameHighScore = gameScore;
        }
        gameScore = 0;
        InitSnake();
    }
    public void InitSnake(){
        // This Method Should only be called once
        // To reset the snake resetSnake Method should be called.
        EAST = true;
        int initialSnakeSize = 3;
        int startX = 100;
        int startY = 100;


        while( initialSnakeSize != 0 ){

            snake.add( new Snake( startX,startY ) );

            // move next start position -10 from previous start
            startX -= 10;
            // reduce value to eventually end the while loop
            initialSnakeSize -= 1;
        }
    }
    // This is where the Demo Mode snake gets its moves from
    public void DemoModeAI(){

        int snakeX = snake.get(0).getX();
        int snakeY = snake.get(0).getY();
        int appleX = apple.get(0).getX();
        int appleY = apple.get(0).getY();

        if(snakeX == appleX || snakeY == appleY){ isEqual = false; }
        if(snakeX != appleX && isEqual != null && !isEqual){
            if(snakeX >= appleX && !direction.equals("EAST") && !direction.equals("WEST")){
                direction = "WEST";
                isEqual = true;
                isKeyPress = true;
                return;
            }
            if(snakeX <= appleX && !direction.equals("WEST") && !direction.equals("EAST")){
                isEqual = true;
                direction = "EAST";
                isKeyPress = true;
                return;
            }
        }
        if(snakeY != appleY && isEqual != null && !isEqual){
            if(snakeY > appleY && !direction.equals("SOUTH") && !direction.equals("NORTH")){
                isEqual = true;
                direction = "NORTH";
                isKeyPress = true;
                return;
            }
            if(snakeX <= appleX && !direction.equals("NORTH") && !direction.equals("SOUTH")){
                isEqual = true;
                direction = "SOUTH";
                isKeyPress = true;
            }
        }



    }

    public void MoveSnake(){
        // If a valid direction change is made change direction here
        if(isKeyPress){
            WEST = false;
            EAST = false;
            SOUTH = false;
            NORTH = false;

            if(direction.equals("WEST")){ WEST = true; }
            if(direction.equals("EAST")){ EAST = true; }
            if(direction.equals("SOUTH")){ SOUTH = true; }
            if(direction.equals("NORTH")){ NORTH = true; }
            isKeyPress = false;
        }
        // Iterate through Snake List
        for( int link = snake.size()-1; link >= 0; link--){
            // If snake is not the Head move each link up one link position
            if( link != 0 ){

                snake.get( link ).setX( snake.get( link - 1 ).getX() );
                snake.get( link ).setY( snake.get( link - 1 ).getY() );

            } else {
                // This will move the head in the set Direction
                if( WEST ){
                    snake.get( link ).decreaseX();
                    if(snake.get( link ).getX() < 10 ) {
                        snake.get( link ).setX( 480 );
                    }
                }
                if( EAST ){
                    snake.get( link ).increaseX();
                    if(snake.get( link ).getX() > 480 ) {
                        snake.get( link ).setX( 10 );
                    }
                }
                if( NORTH ){
                    snake.get( link ).decreaseY();
                    if(snake.get( link ).getY() < 10 ) {
                        snake.get( link ).setY( 420 );
                    }
                }
                if( SOUTH ){
                    snake.get( link ).increaseY();
                    if(snake.get( link ).getY() > 420 ) {
                        snake.get( link ).setY(10);
                    }
                }

            }
        }


    }
    // Drawing The objects To the Screen
    @Override
    public void paintComponent(Graphics graphics){
        super.paintComponent(graphics);
        Graphics2D g2d = (Graphics2D) graphics;

        // sets Background of Snake Pit
        g2d.setColor(Color.darkGray);
        g2d.fill(new Rectangle(10,10,480,420));


        drawSnake(g2d);
        drawApple(g2d);
        drawText(g2d);



    }
    // Draw The Apple
    public Graphics2D drawApple(Graphics2D g2d){
        g2d.setColor(Color.green);
        g2d.fill(new Rectangle(apple.get(0).getX(), apple.get(0).getY(), 8,8));
        return g2d;
    }
    // Draw The Snake
    public Graphics2D drawSnake(Graphics2D g2d){
        for ( int links = snake.size()-1; links > -1; links-- ){

            g2d.setColor(Color.red);

            if(links != 0){g2d.setColor(Color.white);}
            // Not using double buffering need to create a new Rectangle every time
            g2d.fill(new Rectangle(snake.get(links).getX(), snake.get(links).getY(),8,8) );


        }
        return g2d;
    }
    // Draw All Text That is Used in the game
    public Graphics2D drawText(Graphics2D g2d){
        Font oldFont = g2d.getFont();

        if(isDemoMode){

            g2d.setFont(new Font("Arial",Font.BOLD,40));
            g2d.setColor(Color.BLACK);

            g2d.drawString("Demo Mode",150,200);
            g2d.setFont(new Font("Arial", Font.BOLD, 25));
            g2d.setColor(Color.red);
            g2d.drawString("PRESS F2 TO STAR A NEW GAME", 50,260);
            g2d.setFont(oldFont);
        }
        if(isPause) {
            g2d.setFont(new Font("Arial",Font.BOLD,40));
            g2d.setColor(Color.BLACK);
            g2d.drawString("GAME IS PAUSED", 100, 200);
            g2d.setFont(oldFont);
        }

        // Score Box Text
        g2d.setColor(Color.lightGray);
        g2d.setFont(new Font(Font.MONOSPACED,Font.BOLD,20));
        g2d.drawString("Level: " + gameLevel, 500,40 );
        g2d.drawString("Score",500,80);
        g2d.drawString("" + gameScore,500,120);
        g2d.drawString("High Score",500,140);
        g2d.drawString("" + gameHighScore,500,160);


        return g2d;
    }

    @Override
    public void run() {
        //do{
        // while works better for this game do can cause errors
        Thread currentThread = Thread.currentThread();
            while( gameLoop == currentThread && isStarted != null && isStarted) {

                // simple way to control game speed
                try{
                    Thread.sleep(speed);
                }catch (Exception e ){
                    e.printStackTrace();
                }
//
                // If we are in DemoMode Run this
                if(isDemoMode){
                    DemoModeAI();
                }
                //Where is THE snake
                // Used for testing
                //System.out.println(snake.get(0).getX() + " , " + snake.get(0).getY() );

                MoveSnake();
                repaint();
                CollisionDetect();

        }//while(isStarted);
    }
}

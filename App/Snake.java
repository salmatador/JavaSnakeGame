package App;

import java.awt.*;

/**
 * *****************
 * User: Micah Kline
 * Date: 8/2/13
 * Time: 9:49 PM
 * ******************
 */
public class Snake {

    protected int x;
    protected int y;
    protected int wh;
    protected Shape shape;
    protected int step = 10;

    // default constructor sets width and height to 8;
    public Snake( int x, int y){
        setX( x );
        setY( y );
        // Change this value when switching to double buffered Usage
        setWH( 8 );
        // Not Needed until Double Buffering is added
        shape = new Rectangle( x, y, wh, wh );
    }

    public int getX() {
        return x;
    }

    public void setX( int x ){
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY( int y ){
        this.y = y;
    }

    public int getWH() {
        return wh;
    }

    public void setWH( int wh ){
        this.wh = wh;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape( Shape shape ){
        this.shape = shape;
    }

    // Move the snake
    public void increaseX(){
        x = x + step;
    }
    public void decreaseX(){
        x = x - step;
    }
    public void increaseY(){
        y = y + step;
    }
    public void decreaseY(){
        y = y - step;
    }


}

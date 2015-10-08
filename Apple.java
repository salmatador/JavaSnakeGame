import java.awt.*;

/**
 * *****************
 * <p/>
 * User: Micah
 * Date: 8/2/13
 * Time: 9:49 PM
 * ******************
 */
// This is basically the same Class as Snake.java
// Need to update to draw better Apples
public class Apple {

    protected int x;
    protected int y;
    protected int wh;
    protected Shape shape;
    protected int step = 10;

    // default constructor sets width and height to 8;
    public Apple(int x, int y){
        setX( x );
        setY( y );
        setWH( 8 );
        shape = new Rectangle( x, y, wh, wh );
    }

    public void setX( int x ){
        this.x = x;
    }

    public void setY( int y ){
        this.y = y;
    }

    public void setWH( int wh ){
        this.wh = wh;
    }
    public void setShape( Shape shape ){
        this.shape = shape;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
    public int getWH(){ return  wh; }
    public Shape getShape() { return shape; }




}

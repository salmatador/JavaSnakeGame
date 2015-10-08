package App;

import javax.swing.*;
import java.awt.*;

/**
 * *****************
 * <p/>
 * User: Micah Kline
 * Date: 8/2/13
 * Time: 9:40 PM
 * ******************
 */
public class ProgramEntry {


    JFrame window = new JFrame();
    GameBoard board = new GameBoard();
    String gameTitle = "App.Snake Game version 0.5a --- ReImagined By Micah Kline";

    public ProgramEntry(){
        initWindow();
    }

    public static void main(String[] args) {
        new ProgramEntry();
    }

    // Setup the Window and add the Game Board
    public void initWindow(){

        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Container contentPane = window.getContentPane();

        window.setMinimumSize(new Dimension( 640, 480 ) );
        window.setLocationRelativeTo(null);
        contentPane.add(board, BorderLayout.CENTER);
        window.setTitle(gameTitle);
        window.pack();
        window.setVisible( true );

    }


}

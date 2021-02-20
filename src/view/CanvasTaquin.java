package view;

import model.Cell;
import model.Environment;

import java.awt.*;

public class CanvasTaquin extends Canvas {

    public static final Color C2 = new Color(242, 234, 225);
    public static final Color C4 = new Color(219, 203, 186);
    public static final Color C8 = new Color(249, 186, 114);
    public static final Color C16 = new Color(232, 109, 16);
    public static final Color C32 = new Color(232, 80, 16);
    public static final Color C64 = new Color(219, 36, 4);
    public static final Color C128 = new Color(151, 226, 113);
    public static final Color C256 = new Color(81, 160, 41);
    public static final Color C512 = new Color(40, 99, 10);
    public static final Color C1024 = new Color(109, 172, 206);
    public static final Color C2048 = new Color(13, 59, 150);
    public static final Color C4096 = new Color(123, 43, 209);
    public static final Color C8192 = new Color(96, 24, 173);
    public static final Color C16384 = new Color(68, 12, 127);
    public static final Color C32768 = new Color(47, 5, 91);
    public static final Color C65536 = new Color(31, 3, 61);
    public static final Color C131072 = new Color(15, 0, 30);

    public static final Color EMPTY_CELL = new Color(163, 151, 138);
    public static final Color EMPTY_CELL_TEXT = new Color(100, 100, 100);
    public static final Color AGENT_CELL = new Color(232, 80, 16);
    public static final Color AGENT_CELL_TEXT = new Color(25, 25, 25);


    private Environment _taquin;

    public CanvasTaquin(Environment taquin) {
        _taquin = taquin;
        this.setBackground(new Color(96, 90, 83));
        this.requestFocus();
    }

    public void paint(Graphics g) {
        Cell toDisplay;
        Color cellColor, textColor;
        int coordX;
        int coordY = FrameTaquin.GAPCASE;
        for(int y = 0; y < _taquin.getSizeY(); y++) {
            coordX = FrameTaquin.GAPCASE;
            for(int x = 0; x < _taquin.getSizeX(); x++) {
                toDisplay = _taquin.getCell(x, y);
                if(toDisplay.hasAgent()) { cellColor = AGENT_CELL; textColor = AGENT_CELL_TEXT; }
                else { cellColor = EMPTY_CELL; textColor = EMPTY_CELL_TEXT; }
                g.setColor(cellColor);
                g.fillRect(coordX, coordY, FrameTaquin.COTECASE, FrameTaquin.COTECASE);
                g.setColor(textColor);
                g.setFont(new Font("Baskerville Old Face", Font.PLAIN, 60));
                g.drawString(toDisplay.getDisplayName(), coordX + ((toDisplay.getDisplayName().length() == 1)?20:5), coordY + 60);

                coordX+=(FrameTaquin.COTECASE+FrameTaquin.GAPCASE);
            }
            coordY+=(FrameTaquin.COTECASE+FrameTaquin.GAPCASE);
        }
    }
}

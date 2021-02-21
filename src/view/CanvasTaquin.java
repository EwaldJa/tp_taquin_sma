package view;

import model.Cell;
import model.Environment;
import utils.RandomUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CanvasTaquin extends Canvas {

    public static final Color C1 = new Color(242, 234, 225);
    public static final Color C2 = new Color(219, 203, 186);
    public static final Color C3 = new Color(249, 186, 114);
    public static final Color C4 = new Color(232, 109, 16);
    public static final Color C5 = new Color(232, 80, 16);
    public static final Color C6 = new Color(219, 36, 4);
    public static final Color C7 = new Color(151, 226, 113);
    public static final Color C8 = new Color(81, 160, 41);
    public static final Color C9 = new Color(40, 99, 10);
    public static final Color C10 = new Color(109, 172, 206);
    public static final Color C11 = new Color(13, 59, 150);
    public static final Color C12 = new Color(123, 43, 209);
    public static final Color C13 = new Color(96, 24, 173);
    public static final Color C14 = new Color(12, 90, 120);

    public static final List<Color> COLORS;

    static {
        COLORS = new ArrayList<>();
        COLORS.add(C1); COLORS.add(C2); COLORS.add(C3); COLORS.add(C4); COLORS.add(C5); COLORS.add(C6); COLORS.add(C7); COLORS.add(C8); COLORS.add(C9); COLORS.add(C10); COLORS.add(C11); COLORS.add(C12); COLORS.add(C13); COLORS.add(C14);
    }

    public static final Color EMPTY_CELL = new Color(163, 151, 138);
    public static final Color EMPTY_CELL_TEXT = new Color(100, 100, 100);
    public static final Color AGENT_CELL = COLORS.get(RandomUtils.randIntMavVal(COLORS.size()));
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

package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente les directions possibles de déplacement sur la grille de l'environnement
 */
public class Direction {

    /**
     * Instanciation des directions cardinales constantes utilisées dans le projet
     */
    static {
        NORTH = new Direction(0, -1);
        SOUTH = new Direction(0, +1);
        EAST = new Direction(+1, 0);
        WEST = new Direction(-1, 0);
    }

    /**
     * Définition des direction cardinales constantes utilisées dans ce projet
     */
    private static Direction NORTH, SOUTH, EAST, WEST;

    /**
     * Liste de toutes les Directions cardinales utilisées dans le projet
     */
    private static List<Direction> ALL_DIRECTIONS;

    /**
     * Coordonnées représentant la direction sur les axes Est-Ouest (x) et Nord-Sud (y)
     */
    private int x, y;

    /**
     * Crée une direction sans coordonnées
     */
    public Direction(){
        x=0;
        y=0;
    }

    /**
     * Instancie une direction spécifique avec les valeurs données
     * @param x_axis coordonnée de la direction sur l'axe Est-Ouest
     * @param y_axis coordonnée de la direction sur l'axe Nord-Surd
     */
    public Direction(int x_axis, int y_axis) {
        x = x_axis;
        y = y_axis;
    }

    /**
     * @return la liste des Directions cardinales utilisées dans le projet
     */
    public static List<Direction> getALL_DIRECTIONS() {
        if (ALL_DIRECTIONS == null) {
            ALL_DIRECTIONS = new ArrayList<>();
            ALL_DIRECTIONS.add(NORTH);
            ALL_DIRECTIONS.add(SOUTH);
            ALL_DIRECTIONS.add(EAST);
            ALL_DIRECTIONS.add(WEST);
        }
        return ALL_DIRECTIONS;
    }

    /**
     * @return la Direction correspondant au Nord
     */
    public static Direction getNORTH() {
        return NORTH;
    }

    /**
     * @return la Direction correspondant au Sud
     */
    public static Direction getSOUTH() {
        return SOUTH;
    }

    /**
     * @return la Direction correspondant à l'Est
     */
    public static Direction getEAST() {
        return EAST;
    }

    /**
     * @return la Direction correspondant à l'Ouest
     */
    public static Direction getWEST() {
        return WEST;
    }

    /**
     * @return la coordonnée sur l'axe Est-Ouest de la Direction
     */
    public int getX() {
        return x;
    }

    /**
     * @param x la nouvelle coordonnée sur l'axe Est-Ouest de la Direction
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return la coordonnée sur l'axe Nord-Sud de la Direction
     */
    public int getY() {
        return y;
    }

    /**
     * @param y la nouvelle coordonnée sur l'axe Nord-Sud de la Direction
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * @return une String représentant cette direction et permettant de l'afficher dans une console par exemple
     */
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }
}

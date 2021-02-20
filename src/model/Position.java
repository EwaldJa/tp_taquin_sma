package model;

import utils.exceptions.InvalidCoordinatesException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Position {

    /**
     * Entiers représentant les deux coordonnées de cette position
     */
    private int x, y;
    /**
     * Environnement dans lequel cette position est représentée
     */
    private Environment environment;

    /**
     * Instancie une position avec des coordonnées et un environnement dans lequel elle est représentée
     * @param x_coord la coordonnée sur l'axe des abscisses
     * @param y_coord la coordonnée sur l'axe des ordonnées
     * @param w l'environnement
     */
    public Position(int x_coord, int y_coord, Environment w) {
        environment = w; setX(x_coord); setY(y_coord);
    }

    /**
     * Ajoute les coordonnées correspondant à une direction à cette position
     * @param dir la direction à ajouter
     * @return cette Position, mise à jour
     */
    public Position addDirection(Direction dir) {
        if (possibleToAddX(dir.getX()) && possibleToAddY(dir.getY())) {
            addX(dir.getX());
            addY(dir.getY());
            return this;
        }
        throw new InvalidCoordinatesException(String.format("Unable to add direction '%s' to position '%s'", dir.toString(), this.toString()));
    }

    /**
     * Crée une nouvelle Position avec les mêmes coordonnées que la direction actuelle puis lui ajoute
     * @param dir la direction à ajouter
     * @return la nouvelle Position
     */
    public Position newAddDirection(Direction dir) {
        if (possibleToAddX(dir.getX()) && possibleToAddY(dir.getY())) {
            return new Position(x + dir.getX(), y + dir.getY(), environment);
        }
        throw new InvalidCoordinatesException(String.format("Unable to add direction '%s' to position '%s'", dir.toString(), this.toString()));
    }

    public List<Direction> getAvailableDirections() {
        List<Direction> allDirs = Direction.getALL_DIRECTIONS(), availableDirs = new ArrayList<>();
        for(Direction dir:allDirs) {
            if (possibleToAddX(dir.getX()) && possibleToAddY(dir.getY())) { availableDirs.add(dir); }
        }
        return availableDirs;
    }

    /**
     * @param anotherPos l'autre position avec laquelle calculer la distance
     * @return la distance euclidienne entre la position actuelle et la position passée en paramètre
     */
    public double distanceEuclid(Position anotherPos) {
        return Math.sqrt( Math.pow((this.x - anotherPos.getX()), 2.0) + Math.pow(this.y - anotherPos.getY(), 2.0) );
    }

    /**
     * @param anotherPos l'autre position avec laquelle calculer la distance
     * @return la distance de Manhattan entre la position actuelle et la position passée en paramètre
     */
    public double distanceManhattan(Position anotherPos) {
        return ( Math.abs((this.x - anotherPos.getX())) + Math.abs(this.y - anotherPos.getY()) );
    }

    /**
     * Augmente la coordonnée des abscisses de cette Position
     * @param toAdd la valeur à ajouter à la coordonnée des abscisses de cette Position
     */
    public void addX(int toAdd) {
        setX(x+toAdd);
    }

    /**
     * Augmente la coordonnée des ordonnées de cette Position
     * @param toAdd la valeur à ajouter à la coordonnée des ordonnées de cette Position
     */
    public void addY(int toAdd) {
        setY(y+toAdd);
    }

    /**
     * @return la valeur de la coordonnée des abscisses de cette Position
     */
    public int getX() {
        return x;
    }

    /**
     * @return la valeur de la coordonnée des ordonnées de cette Position
     */
    public int getY() {
        return y;
    }

    /**
     * Vérifie s'il est possible d'augmenter la coordonnée X tout en restant bien dans les limites de la grille
     * @param toAddX la valeur qu'on cherche à ajouter
     */
    public boolean possibleToAddX(int toAddX) {
        return environment.isValidX(x + toAddX);
    }

    /**
     * Vérifie s'il est possible d'augmenter la coordonnée Y tout en restant bien dans les limites de la grille
     * @param toAddY la valeur qu'on cherche à ajouter
     */
    public boolean possibleToAddY(int toAddY) {
        return environment.isValidY(y + toAddY);
    }

    /**
     * Défini la coordonnée des abscisses de cette Position
     * @param newX la nouvelle valeur à définir pour la coordonnée des abscisses de cette Position
     */
    public void setX(int newX) {
        if (!environment.isValidX(newX)) {
            throw new InvalidCoordinatesException(String.format("x coordinate value '%d' for Position '%s' is not valid", newX, toString()));
        }
        x = newX;
    }

    /**
     * Défini la coordonnée des ordonnées de cette Position
     * @param newY la nouvelle valeur à définir pour la coordonnée des ordonnées de cette Position
     */
    public void setY(int newY) {
        if (!environment.isValidY(newY)) {
            throw new InvalidCoordinatesException(String.format("y coordinate value '%d' for Position '%s' is not valid", newY, toString()));
        }
        y = newY;
    }

    /**
     * @return une String représentant cette position et permettant de l'afficher dans une console par exemple
     */
    public String toString() {
        return String.format("[%d, %d]", x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x &&
                y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}


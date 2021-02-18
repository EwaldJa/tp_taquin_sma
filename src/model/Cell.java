package model;

import utils.exceptions.EmptyCellException;
import utils.exceptions.FullCellException;

public class Cell {

    /**
     * Représente la position de la cellule dans l'environnement
     */
    private Position pos;

    /**
     * Représente le numéro associé à cette case
     */
    private int cell_number;
    /**
     * L'Agent actuellement sur la case
     */
    private AgentTile agent;

    /**
     * Construit une case avec une position, un numéro, mais sans Agent
     * @param x_coord position sur l'axe x de la case dans l'environnement
     * @param y_coord position sur l'axe y de la case dans l'environnement
     * @param w l'environnement dans lequel se situe la case
     */
    public Cell(int x_coord, int y_coord, Environment w) {
        pos = new Position(x_coord, y_coord, w);
        agent = null;
        cell_number = (y_coord * w.getSize_x()) + x_coord;
    }

    /**
     * @return la position de la cellule
     */
    public Position getPos() {
        return pos;
    }

    /**
     * @return la position sur l'axe des abscisses de la cellule dans l'environnement
     */
    public int getX() {
        return pos.getX();
    }

    /**
     * @return la position sur l'axe des ordonnées de la cellule dans l'environnement
     */
    public int getY() {
        return pos.getY();
    }

    /**
     * Enlève l'Agent actuellement sur la case s'il y en a un
     * @return un clone de l'Agent, pour éviter tout problème de pointeurs
     */
    public AgentTile removeAgent() {
        if(agent != null) {
            AgentTile res = agent;
            agent = null;
            return res; }
        else {
            throw new EmptyCellException(String.format("Cell %s does not contain an Agent", pos.toString())); }
    }

    /**
     * Ajoute un agent à la case si possible
     * @param newAgent l'Agent à ajouter
     */
    public void addAgent(AgentTile newAgent) {
        if(agent == null) {
            agent = newAgent; }
        else {
            throw new FullCellException(String.format("Cell %s already contains an Agent", pos.toString())); }
    }

    /**
     * @return true si la case contient un agent, false sinon
     */
    public boolean hasAgent() {
        return (agent != null);
    }

    /**
     * @return une String représentant la case et son contenu (éventuel Agent et/ou éventuel Objet)
     */
    public String getDisplayName() {
        if (agent == null) {
            return ""+cell_number; }
        else {
            return agent.getDisplayName(); }
    }
}

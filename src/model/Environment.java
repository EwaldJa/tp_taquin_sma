package model;

import utils.RandomUtils;
import utils.exceptions.EnvironmentNotInitializedException;
import utils.exceptions.InvalidCoordinatesException;
import utils.exceptions.MutexAlreadyReleasedException;
import utils.exceptions.NotSynchronizedMethodException;

import java.util.*;
import java.util.stream.Collectors;

public class Environment extends Observable {

    /**
     * Valeurs utilisées dans l'environnement
     */
    private int _size_x, _size_y, _nbAgents;
    private long _nbMoves;
    private Map<AgentTile, MailBox> _mailBoxes;
    private boolean _actionMutex, _initDone, _taquinFinished;

    /**
     * Grille représentant l'environnement
     */
    private List<List<Cell>> _taquin;

    /**
     * Liste des cellules rangées par ID
     */
    private List<Cell> _cellsById;

    /**
     * Les agents associés à leurs positions respectives
     */
    private Map<AgentTile, Position> _agentsPositions;


    /**
     * Construit un environnement avec toutes les valeurs qui seront utilisées
     * @param size_x la taille sur l'axe Est-Ouest de l'environnement
     * @param size_y la taille sur l'axe Nord-Sud de l'environnement
     */
    public Environment(int size_x, int size_y, int nbAgents) {
        _size_x = size_x; _size_y = size_y; _nbAgents = nbAgents;
        _initDone = false;
    }

    public Environment init() {
        _mailBoxes = Collections.synchronizedMap(new HashMap<>());
        _agentsPositions = Collections.synchronizedMap(new HashMap<>());
        _taquin = Collections.synchronizedList(new ArrayList<>());
        _cellsById = new ArrayList<>();
        _nbMoves = 0;
        _taquinFinished = false;
        _taquin = new ArrayList<>();
        for (int lines = 0; lines < _size_y; lines++) {
            List<Cell> line = new ArrayList<>();
            for(int columns = 0; columns < _size_x; columns++) {
                Cell c = new Cell(columns, lines, this);
                line.add(c);
                _cellsById.add(c); }
            _taquin.add(line); }

        int nbAgentsAdded = 0;
        int randX, randY;
        _agentsPositions = new HashMap<>();
        while(_nbAgents > nbAgentsAdded) {
            randX = RandomUtils.randIntMavVal(_size_x);
            randY = RandomUtils.randIntMavVal(_size_y);
            Cell c = _taquin.get(randY).get(randX);
            if (!c.hasAgent()) {
                AgentTile a = new AgentTile(this, _cellsById.get(nbAgentsAdded));
                c.addAgent(a);
                _agentsPositions.put(a, new Position(randX, randY, this));
                _mailBoxes.put(a, new MailBox(this, a));
                nbAgentsAdded++; } }
        _actionMutex = true;
        _initDone = true;
        return this;
    }

    public Environment run() {
        if (!_initDone) { throw new EnvironmentNotInitializedException("'run' method of 'Environment' called whereas the '_actionMutex' was still available."); }
        _mailBoxes.keySet().forEach(agent -> new Thread(agent).start());
        System.out.println("Started all Agents");
        return this;
    }

    public boolean taquinFinished() {
            return _taquinFinished;
    }

    public boolean checkFinished() {
        synchronized (this) {
            if (_actionMutex) { throw new NotSynchronizedMethodException("'getAllNearbyAgents' method of 'Environment' called whereas the '_actionMutex' was still available."); }
            for (AgentTile agent : _mailBoxes.keySet()) {
                if (!agent.isSatisfied()) { return false; } }
            return true; }
    }


    public void update() {
        synchronized (this) {
            if (_actionMutex) { throw new NotSynchronizedMethodException("'getAllNearbyAgents' method of 'Environment' called whereas the '_actionMutex' was still available."); }
            _taquinFinished = checkFinished();
            this.setChanged();
            this.notifyObservers();
            //TODO: remove display
            display(); }
    }


    public Position getAgentPos(AgentTile agent) {
        return _agentsPositions.get(agent);
    }

    public Cell getAgentCell(AgentTile agent) {
        Position currAgentPos = getAgentPos(agent);
        return _taquin.get(currAgentPos.getY()).get(currAgentPos.getX());
    }

    public MailBox getMailBox(AgentTile agent) {
        return _mailBoxes.get(agent);
    }

    public List<AgentTile> getAllNearbyAgents(AgentTile agent) {
        synchronized (this) {
            if (_actionMutex) { throw new NotSynchronizedMethodException("'getAllNearbyAgents' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was still available."); }
            if (!agent.hasActionMutex()) { throw new NotSynchronizedMethodException("'getAllNearbyAgents' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was not taken by this particular agent."); }
            List<AgentTile> nearbyAgents  = Collections.synchronizedList(new ArrayList<>());
            getAllNearbyCells(agent).parallelStream().forEach(cell -> {if (cell.hasAgent()) nearbyAgents.add(cell.getAgent());});
            return nearbyAgents; }
    }

    public List<Cell> getAllNearbyCells(AgentTile agent) {
        synchronized (this) {
            if (_actionMutex) { throw new NotSynchronizedMethodException("'getAllNearbyCells' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was still available."); }
            if (!agent.hasActionMutex()) { throw new NotSynchronizedMethodException("'getAllNearbyCells' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was not taken by this particular agent."); }
            Position currAgentPos = getAgentPos(agent);
            List<Direction> availableDirections = currAgentPos.getAvailableDirections();
            List<Cell> possibleCells  = Collections.synchronizedList(new ArrayList<>());
            for(Direction dir:availableDirections) {Position pos = currAgentPos.newAddDirection(dir); possibleCells.add(_taquin.get(pos.getY()).get(pos.getX()));}
            return possibleCells; }
    }

    public List<Cell> getAllAvailableNearbyCells(AgentTile agent) {
        synchronized (this) {
            if (_actionMutex) { throw new NotSynchronizedMethodException("'getAllAvailableNearbyCells' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was still available."); }
            if (!agent.hasActionMutex()) { throw new NotSynchronizedMethodException("'getAllAvailableNearbyCells' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was not taken by this particular agent."); }
            return getAllNearbyCells(agent).parallelStream().filter(cell -> !cell.hasAgent()).collect(Collectors.toList()); }
    }

    public void move(AgentTile agent, Cell destination) {
        synchronized (this) {
            _nbMoves++;
            if (_actionMutex) { throw new NotSynchronizedMethodException("'move' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was still available."); }
            if (!agent.hasActionMutex()) { throw new NotSynchronizedMethodException("'move' method of 'Environment' called by agent '" + agent.getDisplayName() + "' whereas the '_actionMutex' was not taken by this particular agent."); }
            Position currAgentPos = getAgentPos(agent);
            _taquin.get(destination.getY()).get(destination.getX()).addAgent(_taquin.get(currAgentPos.getY()).get(currAgentPos.getX()).removeAgent());
            _agentsPositions.put(agent, destination.getPos()); }
    }


    public void sendMessage(Message mail) {
        synchronized (this) {
            if (_actionMutex) { throw new NotSynchronizedMethodException("'sendMessage' method of 'Environment' called by agent '" + mail.getSender().getDisplayName() + "' whereas the '_actionMutex' was still available."); }
            if (!mail.getSender().hasActionMutex()) { throw new NotSynchronizedMethodException("'sendMessage' method of 'Environment' called by agent '" + mail.getSender().getDisplayName() + "' whereas the '_actionMutex' was not taken by this particular agent."); }
            _mailBoxes.get(mail.getRecipient()).addMessage(mail);
        }
    }

    public boolean takeActionMutex() {
        synchronized (this) {
            if(_actionMutex) {
                _actionMutex = false;
                return true; }
            return false; }
    }

    public void releaseActionMutex() {
        synchronized (this) {
            if(_actionMutex) { throw new MutexAlreadyReleasedException("Action mutex was released whereas it was already available."); }
            update();
            _actionMutex = !taquinFinished(); }
    }

    public Cell getCell(int x, int y) {
        synchronized (this) {
            if(!isValidX(x)) { throw new InvalidCoordinatesException("x value specified while trying to get a Cell is not valid (" + x + ")"); }
            if(!isValidY(y)) { throw new InvalidCoordinatesException("y value specified while trying to get a Cell is not valid (" + y + ")"); }
            return _taquin.get(y).get(x); }
    }

    /**
     * @return la taille sur l'axe Est-Ouest de l'environnement
     */
    public int getSizeX() {
        return _size_x;
    }

    /**
     * @return la taille sur l'axe Nord-Sud de l'environnement
     */
    public int getSizeY() {
        return _size_y;
    }

    /**
     * @param x the X value to check
     * @return true if x is valid, false otherwise
     */
    public boolean isValidX(int x) {
        return ( (getSizeX() > x) && (x >= 0) );
    }

    /**
     * @param y the Y value to check
     * @return true if y is valid, false otherwise
     */
    public boolean isValidY(int y) {
        return ( (getSizeY() > y) && (y >= 0) );
    }

    /**
     * @return le nombre de mouvements effectués depuis le lancement du taquin
     */
    public long getNbMoves() {
        return _nbMoves;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int lines = 0; lines < _size_y; lines++) {
            for(int columns = 0; columns < _size_x; columns++) {
                sb.append("|").append(_taquin.get(lines).get(columns).getDisplayName()); }
            sb.append("|\n"); }
        return sb.toString();
    }

    public void display() {
        System.out.println("\n--------------------------------");
        System.out.println("Move #" + _nbMoves + "\n");
        System.out.println(toString());
        System.out.println("\n--------------------------------");
    }
}

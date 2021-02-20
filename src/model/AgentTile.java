package model;

import utils.ConstantsUtils;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AgentTile implements Runnable {

    /**
     * Compte le nombre d'agents instanciés, pour avoir un ID unique
     */
    private static long _agentNbCounter;

    /**
     * Id et numéro de tuile de l'agent
     */
    private long _agent_id;

    /**
     * Environnement dans lequel évolue l'agent
     */
    private Environment _environment;

    /**
     * Représente le fait que l'agent aie atteint ou non sa case objectif
     */
    private boolean _isSatisfied;

    /**
     * Si l'agent possède le mutex d'action
     */
    private boolean _hasActionMutex;

    /**
     * Liste des messages envoyés
     */
    private List<Message> _sentMails;

    /**
     * Objectif de position de l'agent
     */
    private Cell _goal;

    private boolean canMove, isPushed, isPushing, ownPushRequestForwarded, ownPushRequestUnread, ownPushRequestExpiredOrUnsatisfied, ownPushRequestSatisfied;
    private double probaMove;
    private Position currentPos;
    private List<Cell> availableNearbyCells;
    private List<AgentTile> nearbyAgents;

    private AgentTile() {}

    /**
     * Constructeur de l'Agent, initialise les variables utilisées dans le programme, initialise l'id unique
     * @param theEnvironment environnement dans lequel évolue l'Agent
     */
    public AgentTile(Environment theEnvironment, Cell goal) {
        _agent_id = _agentNbCounter++;
        _environment = theEnvironment;
        _goal = goal;
        _isSatisfied = false;
        _hasActionMutex = false;
        _sentMails = new ArrayList<>();
    }

    private void perception() {
        _isSatisfied = _environment.getAgentCell(this).equals(_goal);
        availableNearbyCells = _environment.getAllAvailableNearbyCells(this);
        nearbyAgents = _environment.getAllNearbyAgents(this);
        canMove = !availableNearbyCells.isEmpty();
        currentPos = _environment.getAgentPos(this);
    }

    /**
     * Checks incoming messages
     * @return true if has moved, false otherwise
     */
    private boolean checkMessagesReceived() {
        MailBox mailBox = _environment.getMailBox(this);
        mailBox.clearObsolete();
        boolean hasSatisfiedAMail = false, hasMoved = false;
        int nbMailsRead = 0;
        while ( mailBox.hasNext() && ( RandomUtils.randDouble() > ( ((double)nbMailsRead) / ((double)(((hasSatisfiedAMail)?5:0) + mailBox.size())) ) ) ) {
            isPushed = true;
            Message mail = mailBox.next();
            if(!canMove) {
                if (forwardMessage(mail)) { mail.answer(Message.AnswerType.FORWARDED); hasSatisfiedAMail = true; }
                else { mailBox.delete(mail.answer(Message.AnswerType.UNSATISFIED)); } }
            else if (move(null)) {
                hasMoved = true;
                hasSatisfiedAMail = true;
                mailBox.delete(mail.answer(Message.AnswerType.SATISFIED)); }
            else {
                mailBox.delete(mail.answer(Message.AnswerType.UNSATISFIED)); }
            nbMailsRead++; }
        return hasMoved;
    }

    /**
     * Vérifie si les messages envoyés ont été vus, statisfaits ou transférés
     * @return le premier Message satisfait rencontré, null si aucun
     */
    private Message checkMessagesSent() {
        Message ownPush = null; isPushing = false; ownPushRequestExpiredOrUnsatisfied = false; ownPushRequestSatisfied = false; ownPushRequestUnread = false; ownPushRequestForwarded = false;
        boolean stopCheck = false;
        List<Message> toDelete = new ArrayList<>();
        for(Message mail:_sentMails) {
            switch (mail.getAnswer()) {
                case NOT_READ:
                    if(mail.getSender().equals(this)) {
                        isPushing = true;
                        ownPushRequestUnread = true; }
                    break;
                case FORWARDED:
                    if(mail.getSender().equals(this)) {
                        isPushing = true;
                        ownPushRequestForwarded = true; }
                    toDelete.add(mail);
                    break;
                case SATISFIED:
                    if(mail.getSender().equals(this)) {
                        ownPushRequestSatisfied = true;
                        ownPush = mail; }
                    toDelete.add(mail);
                    stopCheck = true;
                    break;
                case EXPIRED:
                case UNSATISFIED:
                    if(mail.getSender().equals(this)) {
                        ownPushRequestExpiredOrUnsatisfied = true; }
                    toDelete.add(mail);
                    break;
            }
            if(stopCheck) break;
        }
        _sentMails.removeAll(toDelete);
        return ownPush;
    }

    /**
     * Essaie de transférer une demande aux agents à proximité
     * @return true si le message a été transféré à au moins un agent proche, false sinon
     */
    private boolean forwardMessage(Message toForward) {
        boolean isForwarded = false;
        double max = Double.NEGATIVE_INFINITY, min = Double.POSITIVE_INFINITY;
        for(AgentTile agent:nearbyAgents) {
            double dist = _goal.getPos().distanceManhattan(_environment.getAgentPos(agent));
            if (dist > max) { max = dist; }
            if (dist < min) { min = dist; } }
        for(AgentTile agent:nearbyAgents) {
            Position agentPos = _environment.getAgentPos(agent);
            if ( !agent.isPushing && !agent.isPushed && RandomUtils.randDouble() > ( ((_goal.getPos().distanceManhattan(agentPos)-min)/(max-min)) * ConstantsUtils.FORWARD_MAIL_RATIO ) ) {
                Message forwarded = new Message(this, agent, currentPos, agentPos, toForward.getRequest(), Message.AnswerType.NOT_READ);
                _environment.sendMessage(forwarded);
                _sentMails.add(forwarded);
                isForwarded = true; } }
        return isForwarded;
    }

    /**
     * Tire une proba, et se déplace ou non
     * @return true si un déplacement a été fait, false sinon
     */
    private boolean move(Position priority) {
        Cell first = null;
        for(Cell c:availableNearbyCells) { if (c.getPos().equals(priority)) { first = c; availableNearbyCells.remove(c); break; } }
        if(first != null) { availableNearbyCells.add(0, first); }
        availableNearbyCells.sort(Comparator.comparingDouble(c -> _goal.getPos().distanceManhattan(c.getPos())));
        probaMove = ConstantsUtils.BASIC_MOVE_PROBABILITY + ((_isSatisfied)?ConstantsUtils.IS_SATISFIED_MOVE_PROBA_MODIFIER:0) + ((isPushed)?ConstantsUtils.IS_PUSHED_PROBA_MODIFIER:0) + ((ownPushRequestUnread)?ConstantsUtils.PUSH_REQUEST_UNREAD_MOVE_PROBA_MODIFIER:0) + ((ownPushRequestForwarded)?ConstantsUtils.PUSH_REQUEST_FORWARDED_MOVE_PROBA_MODIFIER:0) + ((ownPushRequestSatisfied)?ConstantsUtils.PUSH_REQUEST_SATISFIED_MOVE_PROBA_MODIFIER:0) + ((ownPushRequestExpiredOrUnsatisfied)?ConstantsUtils.PUSH_REQUEST_EXPIRED_OR_UNSATISFIED_MOVE_PROBA_MODIFIER:0) + ((availableNearbyCells.get(0).distanceManhattan(_goal) > currentPos.distanceManhattan(_goal.getPos()))?ConstantsUtils.SHORTEST_DIST_TO_TARGET_GREATER_THAN_ACTUAL:ConstantsUtils.SHORTEST_DIST_TO_TARGET_SMALLER_THAN_ACTUAL);
        System.out.print("Agent #" + getId() + " moving proba is " + probaMove);
        if (RandomUtils.randDouble() < probaMove && !availableNearbyCells.isEmpty()) {
            _environment.move(this, availableNearbyCells.get(0));
            checkSatisfiedReceivedMessage(currentPos);
            expireSentMessages(currentPos);
            System.out.print(", agent moved to Cell #" + availableNearbyCells.get(0).getCellNumber() + " while all available cells were : [");
            for(Cell c:availableNearbyCells) {System.out.print(c.getCellNumber() + ",");}
            System.out.println("].");
            return true; }
        else { System.out.println(", agent did not move."); return false; }
    }

    private void checkSatisfiedReceivedMessage(Position oldPos) {
        MailBox mailBox = _environment.getMailBox(this);
        mailBox.clearObsolete();
        while ( mailBox.hasNext() ) {
            Message mail = mailBox.next();
            if (oldPos.equals(mail.getRecipientPos())) {
                mailBox.delete(mail.answer(Message.AnswerType.SATISFIED)); }
            else { /*Ne devrait jamais arriver, vu qu'on clear au-dessus*/
                mailBox.delete(mail.answer(Message.AnswerType.EXPIRED)); } }
    }

    private void expireSentMessages(Position oldPos) {
        _sentMails.parallelStream().forEach(message -> { if (!oldPos.equals(message.getSenderPos())) message.answer(Message.AnswerType.EXPIRED);});
        _sentMails = _sentMails.parallelStream().filter(message -> message.getAnswer() != Message.AnswerType.EXPIRED).collect(Collectors.toList());
    }

    private void sendMoveMessage() {
        if(!nearbyAgents.isEmpty()) {
            nearbyAgents.sort(Comparator.comparingDouble(agent -> _goal.getPos().distanceManhattan(_environment.getAgentPos(agent))));
            _environment.sendMessage(new Message().sender(this).senderPos(currentPos).recipient(nearbyAgents.get(0)).recipientPos(_environment.getAgentPos(nearbyAgents.get(0))).request(Message.RequestType.MOVE).answer(Message.AnswerType.NOT_READ));
            isPushing = true;
            System.out.println("Agent #" + getId() + " pushed Agent #" + nearbyAgents.get(0).getId());
        }
    }

    @Override
    public void run() {
        System.out.println("Agent #" + getId() + " started.");
        while(!_environment.taquinFinished()) {
            while(!_environment.takeActionMutex()) {
                if(_environment.taquinFinished()) { return; }
                sleep(1); }
            _hasActionMutex = true;
            perception();
            boolean hasMoved = checkMessagesReceived();
            Message fulfilledMail = checkMessagesSent();
            if(!hasMoved && canMove) {
                if(fulfilledMail != null) {
                    hasMoved = move(fulfilledMail.getRecipientPos()); }
                else {
                    hasMoved = move(null); } }
            _isSatisfied = _environment.getAgentCell(this).equals(_goal);
            if(!hasMoved && !_isSatisfied && !isPushing) {
                sendMoveMessage(); }
            _hasActionMutex = false;
            _environment.releaseActionMutex();
            sleep(3); }
    }

    private void sleep(int factor) {
        try {
            Thread.sleep(RandomUtils.randIntBetween(10*factor, 100*factor));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return le numéro de l'agent prêt à afficher
     */
    public String getDisplayName() {
        return ""+_agent_id;
    }

    /**
     * @return l'id de l'agent
     */
    public long getId() { return _agent_id; }

    /**
     * @return true ou false selon que l'agent possède actuellement le mutex d'action ou non
     */
    public boolean hasActionMutex() {
        return _hasActionMutex;
    }

    /**
     * @return true ou false selon que l'agent se situe actuellement sur son objectif ou non
     */
    public boolean isSatisfied() {
        return _isSatisfied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentTile agentTile = (AgentTile) o;
        return _agent_id == agentTile._agent_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_agent_id);
    }

    public AgentTile clone() {
        AgentTile clone = new AgentTile();
        clone._agent_id = this._agent_id;
        clone._environment = this._environment;
        clone._goal = this._goal;
        clone._isSatisfied = this._isSatisfied;
        clone._hasActionMutex = this._hasActionMutex;
        clone._sentMails = new ArrayList<>(); clone._sentMails.addAll(this._sentMails);
        clone.canMove = this.canMove;
        clone.isPushed = this.isPushed;
        clone.isPushing = this.isPushing;
        clone.ownPushRequestForwarded = this.ownPushRequestForwarded;
        clone.ownPushRequestUnread = this.ownPushRequestUnread;
        clone.ownPushRequestExpiredOrUnsatisfied = this.ownPushRequestExpiredOrUnsatisfied;
        clone.ownPushRequestSatisfied = this.ownPushRequestSatisfied;
        clone.probaMove = this.probaMove;
        clone.currentPos = this.currentPos;
        clone.availableNearbyCells = new ArrayList<>(); clone.availableNearbyCells.addAll(this.availableNearbyCells);
        clone.nearbyAgents = new ArrayList<>(); clone.nearbyAgents.addAll(this.nearbyAgents);
        return clone;
    }
}

package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Environment {

    /**
     * Valeurs utilisées dans l'environnement
     */
    private int _size_x, _size_y;
    private Map<AgentTile, MailBox> _mailBoxes;


    /**
     * Construit un environnement avec toutes les valeurs qui seront utilisées
     * @param size_x la taille sur l'axe Est-Ouest de l'environnement
     * @param size_y la taille sur l'axe Nord-Sud de l'environnement
     */
    public Environment(int size_x, int size_y) {
        _size_x = size_x; _size_y = size_y;
        _mailBoxes = Collections.synchronizedMap(new HashMap<>());
    }

    public Position getAgentPos(AgentTile agent) {
        //TODO
    }


    public void sendMessage(Message mail) {
        _mailBoxes.get(mail.getRecipient()).addMessage(mail);
    }


    /**
     * @return la taille sur l'axe Est-Ouest de l'environnement
     */
    public int getSize_x() {
        return _size_x;
    }

    /**
     * @return la taille sur l'axe Nord-Sud de l'environnement
     */
    public int getSize_y() {
        return _size_y;
    }
}

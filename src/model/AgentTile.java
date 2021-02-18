package model;

import java.util.Objects;

public class AgentTile {

    /**
     * Compte le nombre d'agents instanciés, pour avoir un ID unique
     */
    private static long agentNbCounter;

    /**
     * Id et numéro de tuile de l'agent
     */
    long agent_id;

    /**
     * Environnement dans lequel évolue l'agent
     */
    private Environment environment;

    /**
     * Constructeur de l'Agent, initialise les variables utilisées dans le programme, initialise l'id unique
     * @param theEnvironment environnement dans lequel évolue l'Agent
     */
    public AgentTile(Environment theEnvironment) {
        agent_id = agentNbCounter++;
        environment = theEnvironment;
    }



    /**
     * @return le numéro de l'agent prêt à afficher
     */
    public String getDisplayName() {
        return ""+agent_id;
    }

    /**
     * @return l'id de l'agent
     */
    public long getId() { return agent_id; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentTile agentTile = (AgentTile) o;
        return agent_id == agentTile.agent_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(agent_id);
    }
}

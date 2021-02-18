package model;

import utils.exceptions.EmptyMailboxException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MailBox {

    private Environment _environment;
    private AgentTile _mailboxOwner;
    private List<Message> _mailList;

    public MailBox(AgentTile mailboxOwner) {
        _mailboxOwner = mailboxOwner;
        _mailList = Collections.synchronizedList(new ArrayList<>());
    }

    public boolean isEmpty() {
        return _mailList.isEmpty();
    }

    public Message next() {
        if (isEmpty()) { throw new EmptyMailboxException("Mailbox for agent #" + _mailboxOwner.getId() + " is empty but Message older() was requested.");}
        return _mailList.get(0);
    }

    public void delete() {
        _mailList.remove(0);
    }

    public void delete(Message toDelete) {
        _mailList.remove(toDelete);
    }

    public void addMessage(Message toAdd) {
        _mailList.add(toAdd);
    }

    public void clearObsolete() {
        Position agentCurrentPos = _environment.getAgentPos(_mailboxOwner);
        _mailList = _mailList.parallelStream().filter(message -> message.isNotExpired(_environment.getAgentPos(message.getSender()), agentCurrentPos)).collect(Collectors.toList());
    }
}

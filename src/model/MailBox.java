package model;

import utils.exceptions.EmptyMailboxException;
import utils.exceptions.NotEnoughMailsException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MailBox {

    private Environment _environment;
    private AgentTile _mailboxOwner;
    private List<Message> _mailList;
    private int _readIndex;

    public MailBox(Environment environment, AgentTile mailboxOwner) {
        _environment = environment;
        _mailboxOwner = mailboxOwner;
        _mailList = Collections.synchronizedList(new ArrayList<>());
    }

    public Message first() {
        if (isEmpty()) { throw new EmptyMailboxException("Mailbox for agent #" + _mailboxOwner.getId() + " is empty but Message older() was requested.");}
        return _mailList.get(0);
    }

    public boolean hasNext() {
        return (_mailList.size() > _readIndex + 1);
    }

    public Message next() {
        if(_mailList.size() <= _readIndex) { throw new NotEnoughMailsException("Mail #" + _readIndex + " for agent #" + _mailboxOwner.getId() + " but mailbox size is only " + _mailList.size() + "."); }
        return _mailList.get(_readIndex++);
    }

    public void resetReadIndex() {
        _readIndex = 0;
    }

    public void deleteFirst() {
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
        _mailList.parallelStream().forEach(message -> {if(message.isExpired(_environment.getAgentPos(message.getSender()), agentCurrentPos)) { message.answer(Message.AnswerType.EXPIRED); }});
        _mailList = _mailList.parallelStream().filter(message -> message.getAnswer() != Message.AnswerType.EXPIRED).collect(Collectors.toList());
        resetReadIndex();
    }

    public int getReadIndex() {
        return _readIndex;
    }

    public boolean isEmpty() {
        return _mailList.isEmpty();
    }

    public int size() {
        return _mailList.size();
    }


}

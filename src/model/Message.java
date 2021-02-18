package model;

public class Message {

    private static long message_number;

    public enum RequestType {
        MOVE
    }

    public enum AnswerType {
        SATISFIED,
        UNSATISFIED,
        FORWARDED,
        EXPIRED,
        NOT_READ
    }

    private long _id; /*Pour différentier 2 messages échangés entre le même couple expéditeur/destinataire*/
    private AgentTile _sender, _recipient;
    private Position _senderPos, _recipientPos;
    private RequestType _request;
    private AnswerType _answer;

    public Message() {}

    public Message(AgentTile sender, AgentTile recipient, Position senderPos, Position recipientPos, RequestType request, AnswerType answer) {
        _sender = sender;
        _recipient = recipient;
        _senderPos = senderPos;
        _recipientPos = recipientPos;
        _request = request;
        _answer = answer;
    }


    public Message sender       (AgentTile sender)      { _sender = sender;             return this; }
    public Message recipient    (AgentTile recipient)   { _recipient = recipient;       return this; }
    public Message senderPos    (Position senderPos)    { _senderPos = senderPos;       return this; }
    public Message recipientPos (Position recipientPos) { _recipientPos = recipientPos; return this; }
    public Message request      (RequestType request)   { _request = request;           return this; }
    public Message answer       (AnswerType answer)     { _answer = answer;             return this; }


    public boolean isNotExpired(Position currentSenderPos, Position currentRecipientPos) {
        return getSenderPos().equals(currentSenderPos) && getRecipientPos().equals(currentRecipientPos);
    }


    public AgentTile getSender() {
        return _sender;
    }

    public AgentTile getRecipient() {
        return _recipient;
    }

    public Position getSenderPos() {
        return _senderPos;
    }

    public Position getRecipientPos() {
        return _recipientPos;
    }

    public RequestType getRequest() {
        return _request;
    }

    public AnswerType getAnswer() {
        return _answer;
    }
}

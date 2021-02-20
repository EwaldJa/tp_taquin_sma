package view;

import model.Environment;
import view.utils.FrameTaquinWindowListener;
import view.utils.LaunchGameListener;
import view.utils.NewGameListener;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class FrameTaquin extends Frame implements Observer {

    public final static int COTECASE=80, GAPCASE=10;

    private int _sizeX, _sizeY, _nbAgents;
    private CanvasTaquin _canvas;
    private Environment _taquin;
    Label _movesLabel;
    private TextField _TFinputX, _TFinputY, _TFinputNbAgents;

    public FrameTaquin(int sizeX, int sizeY, int nbAgents) {
        _sizeX = sizeX; _sizeY = sizeY; _nbAgents = nbAgents;
        _taquin = new Environment(sizeX, sizeY, nbAgents).init();
        _taquin.addObserver(this);


        this.setTitle("PolyTaquin");
        this.setSize(new Dimension((_sizeX*(COTECASE+GAPCASE)+2*GAPCASE),(_sizeY*(COTECASE+GAPCASE)+2*GAPCASE+50+35)));
        this.setLayout(new BorderLayout());
        this.setBackground(new Color(255, 247, 239));


        this._canvas = new CanvasTaquin(_taquin);
        Panel controlPanel = new Panel(new GridLayout(1,3,10,10));
        Panel optionPanel = new Panel(new GridLayout(1,6,5,5));
        Panel largePanel = new Panel(new GridLayout(2,1,10,10));


        Button _BnewGame = new Button("Nouveau jeu"); _BnewGame.setBackground(new Color(242, 201, 157)); _BnewGame.addActionListener(new NewGameListener(this));
        Button _Blaunch = new Button("Lancer"); _Blaunch.setBackground(new Color(242, 201, 157)); _Blaunch.addActionListener(new LaunchGameListener(this));
        _movesLabel = new Label("Moves : " + _taquin.getNbMoves());
        controlPanel.add(_BnewGame);controlPanel.add(_Blaunch);controlPanel.add(_movesLabel);

        Label lblX = new Label("Nb cols : "); _TFinputX = new TextField(2); _TFinputX.setText(String.valueOf(_sizeX));
        Label lblY = new Label("Nb lignes : "); _TFinputY = new TextField(2); _TFinputY.setText(String.valueOf(_sizeY));
        Label lblAgents = new Label("Nb agents : "); _TFinputNbAgents = new TextField(2); _TFinputNbAgents.setText(String.valueOf(_nbAgents));
        optionPanel.add(lblX);optionPanel.add(this._TFinputX);
        optionPanel.add(lblY);optionPanel.add(this._TFinputY);
        optionPanel.add(lblAgents);optionPanel.add(this._TFinputNbAgents);


        largePanel.add(controlPanel);largePanel.add(optionPanel);

        this.add(_canvas, BorderLayout.CENTER);
        this.add(largePanel, BorderLayout.SOUTH);

        this.addWindowListener(new FrameTaquinWindowListener(this));
        this.setResizable(false);
        this.setVisible(true);

        _canvas.requestFocus();
    }

    public int getInputX() { return (Integer.parseInt(_TFinputX.getText()));}

    public int getInputY() { return (Integer.parseInt(_TFinputY.getText()));}

    public int getInputNbAgents() { return (Integer.parseInt(_TFinputNbAgents.getText()));}

    public int getSizeX() { return _sizeX; }

    public int getSizeY() { return _sizeY; }

    public int getNbAgents() { return _nbAgents; }

    public Environment getTaquin() { return _taquin; }

    @Override
    public void update(Observable o, Object arg) {
        update();
    }

    public void update() {
        _canvas.repaint();
        _canvas.requestFocus();
        _movesLabel.setText("Moves : " + _taquin.getNbMoves());
    }
}

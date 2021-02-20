package view.utils;

import view.FrameTaquin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewGameListener implements ActionListener {

    private FrameTaquin _frame;

    public NewGameListener(FrameTaquin frame) {
        _frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int inputX = _frame.getInputX();
        int inputY = _frame.getInputY();
        int inputNbAgents = _frame.getInputNbAgents();

        if( (inputX != _frame.getSizeX()) || (inputY != _frame.getSizeY()) || (inputNbAgents != _frame.getNbAgents()) ) {
            _frame.dispose();
            new FrameTaquin(inputX, inputY, inputNbAgents); }
        else {
            _frame.getTaquin().killAgents().init().update();
        }
    }
}

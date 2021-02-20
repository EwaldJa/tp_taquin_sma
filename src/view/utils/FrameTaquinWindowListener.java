package view.utils;

import view.FrameTaquin;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class FrameTaquinWindowListener implements WindowListener {

    private FrameTaquin _frame;

    public FrameTaquinWindowListener(FrameTaquin frame) {
        _frame = frame;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        _frame.getTaquin().killAgents();
        _frame.dispose();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}

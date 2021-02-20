package view.utils;

import view.FrameTaquin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LaunchGameListener implements ActionListener {

    private FrameTaquin _frame;

    public LaunchGameListener(FrameTaquin frame) {
        _frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        _frame.getTaquin().run();
    }
}

package com.ylinor.launcher;

import fr.theshark34.swinger.util.WindowMover;
import javax.swing.JFrame;


import static com.ylinor.launcher.YlinorLauncher.*;
import static com.ylinor.launcher.Resources.*;
import static fr.theshark34.swinger.Swinger.*;

public class LauncherFrame extends JFrame {
    private WindowMover mover = new WindowMover(this);

    public LauncherFrame() {
        this.setTitle(TITLE);
        this.setSize(SIZE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setIconImage(getResource(ICON));

        this.setContentPane(new LauncherPanel());

        this.addMouseListener(mover);
        this.addMouseMotionListener(mover);
    }

    @Override
    public LauncherPanel getContentPane() {
        return (LauncherPanel) super.getContentPane();
    }
}

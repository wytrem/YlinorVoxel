package com.ylinor.launcher;

import static com.ylinor.launcher.Resources.ICON;
import static com.ylinor.launcher.YlinorLauncher.SIZE;
import static com.ylinor.launcher.YlinorLauncher.TITLE;
import static fr.theshark34.swinger.Swinger.getResource;

import javax.swing.JFrame;

import fr.theshark34.swinger.util.WindowMover;


public class LauncherFrame extends JFrame {
    private static final long serialVersionUID = 2546381036428513497L;
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

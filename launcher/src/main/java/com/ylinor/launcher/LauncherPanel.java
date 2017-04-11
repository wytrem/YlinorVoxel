package com.ylinor.launcher;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedButton;
import fr.theshark34.swinger.textured.STexturedProgressBar;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


import static com.ylinor.launcher.Resources.*;
import static fr.theshark34.swinger.Swinger.*;

public class LauncherPanel extends JPanel implements KeyListener, SwingerEventListener {
    private Image background = getResource(BACKGROUND);

    private SColoredButton play = new SColoredButton(getTransparentWhite(100), getTransparentWhite(125));
    private STexturedProgressBar bar = new STexturedProgressBar(getResource(BAR_EMPTY), getResource(BAR_FILLED));

    public LauncherPanel() {
        this.setLayout(null);

        play.addEventListener(this);
        play.setBounds(100, 345, 425, 50);
        play.setText("Jouer");
        play.setTextColor(Color.WHITE);
        this.add(play);

        bar.setBounds(24, 500, 577, 63);
        this.add(bar);

        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                play.setFont(play.getFont().deriveFont(30F));
            }
        });
    }

    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        graphics.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }

    @Override
    public void onEvent(SwingerEvent event)
    {
        Thread thread = new Thread("Launch Thread") {
            @Override
            public void run() {
                setFieldsEnabled(false);

                try {
                    Launcher.update(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            bar.setMaximum((int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000));
                            bar.setValue((int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000));
                        }
                    });
                } catch (Exception e) {
                    Launcher.shutdown();
                    YlinorLauncher.handleCrash("Erreur pendant la mise à jour !", e);
                }

                try {
                    Launcher.launch();
                } catch (Exception e) {
                    YlinorLauncher.handleCrash(e instanceof LaunchException ? "Erreur de lancement !" :
                                        e instanceof InvocationTargetException ? "Le jeu a crashé !" :
                                        "Erreur inconnue !", e);
                }
            }
        };

        thread.start();
    }

    private void setFieldsEnabled(boolean enabled) {
        play.setEnabled(enabled);
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER || keyEvent.getKeyChar() == 10) {
            onEvent(new SwingerEvent(play, SwingerEvent.BUTTON_CLICKED_EVENT));
        }
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
    }
}

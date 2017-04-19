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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;


import static com.ylinor.launcher.Resources.*;
import static fr.theshark34.swinger.Swinger.*;

public class LauncherPanel extends JPanel implements KeyListener, SwingerEventListener {
    private Image background = getResource(BACKGROUND);

    private SColoredButton play = new SColoredButton(getTransparentWhite(100), getTransparentWhite(125));
    private STexturedProgressBar bar = new STexturedProgressBar(getResource(BAR_EMPTY), getResource(BAR_FILLED));

    private JLabel user = new JLabel("Connexion...", SwingConstants.CENTER);
    private JTextField usernameField = new JTextField(Launcher.getUsername("E-Mail"));
    private JPasswordField passwordField = new JPasswordField(Launcher.getUsername(null) == null ? "Mot de passe" : "");

    private User logged;

    public LauncherPanel() {
        this.setLayout(null);

        usernameField.setBounds(100, 315, 425, 25);
        usernameField.setBorder(null);
        usernameField.setOpaque(false);
        usernameField.setBackground(getTransparentWhite(100));
        usernameField.setForeground(Color.WHITE);
        usernameField.setCaretColor(usernameField.getForeground());
        usernameField.setFont(usernameField.getFont().deriveFont(18F));
        this.add(usernameField);

        passwordField.setBounds(100, 355, 425, 25);
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        passwordField.setBackground(getTransparentWhite(100));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(passwordField.getForeground());
        passwordField.setFont(usernameField.getFont());
        this.add(passwordField);

        user.setBounds(100, 275, 425, 25);
        user.setForeground(Color.WHITE);
        user.setFont(usernameField.getFont());
        this.add(user);

        play.addEventListener(this);
        play.setBounds(100, 400, 425, 50);
        play.setText("Jouer");
        play.setTextColor(Color.WHITE);
        this.add(play);

        bar.setBounds(24, 500, 577, 63);
        this.add(bar);

        setFieldsEnabled(false);

        SwingUtilities.invokeLater(() -> {
            play.setFont(play.getFont().deriveFont(30F));

            new Thread(() -> {
                try {
                    logged = Launcher.getUser();
                } catch (Exception e) {
                }

                if (logged == null) {
                    user.setText("Connecte toi !");
                    setFieldsEnabled(true);
                } else {
                    user.setText("<html>Bienvenue, <b>" + logged.getUsername() + "</b></html>");
                    usernameField.setText(logged.getEmail());

                    play.setEnabled(true);
                }
            }, "Refresh thread").start();
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
        Thread thread = new Thread(() -> {
            setFieldsEnabled(false);

            if (logged == null) {
                try {
                    Launcher.auth(usernameField.getText(), passwordField.getText());
                    logged = Launcher.getUser();
                } catch (AuthException e) {
                    JOptionPane.showMessageDialog(this, "Impossible de s'authentifier : " + e.getMessage(), "Erreur d'authentification", JOptionPane.WARNING_MESSAGE);
                    setFieldsEnabled(true);

                    return;
                }
                catch (Exception e) {
                    YlinorLauncher.handleCrash("Erreur pendant la connexion !", e);
                }
            }

            this.user.setText("<html>Bienvenue, <b>" + logged.getUsername() + "</b>");

            try {
                Launcher.update(() -> {
                    bar.setMaximum((int) (BarAPI.getNumberOfTotalBytesToDownload() / 1000));
                    bar.setValue((int) (BarAPI.getNumberOfTotalDownloadedBytes() / 1000));
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
        }, "Launch Thread");

        thread.start();
    }

    private void setFieldsEnabled(boolean enabled) {
        usernameField.setEnabled(enabled);
        passwordField.setEnabled(enabled);
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
package com.ylinor.launcher;

import static com.ylinor.launcher.Resources.BACKGROUND;
import static com.ylinor.launcher.Resources.BAR_EMPTY;
import static com.ylinor.launcher.Resources.BAR_FILLED;
import static fr.theshark34.swinger.Swinger.getResource;
import static fr.theshark34.swinger.Swinger.getTransparentWhite;


import com.ylinor.auth.client.YlinorUser;
import com.ylinor.auth.client.model.AuthException;
import com.ylinor.auth.client.model.User;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import fr.theshark34.openlauncherlib.LaunchException;
import fr.theshark34.supdate.BarAPI;
import fr.theshark34.swinger.colored.SColoredButton;
import fr.theshark34.swinger.event.SwingerEvent;
import fr.theshark34.swinger.event.SwingerEventListener;
import fr.theshark34.swinger.textured.STexturedProgressBar;


public class LauncherPanel extends JPanel
                implements KeyListener, SwingerEventListener {
    private static final long serialVersionUID = -8583455959529463374L;

    private Image background = getResource(BACKGROUND);

    private SColoredButton play = new SColoredButton(getTransparentWhite(100), getTransparentWhite(125));
    private STexturedProgressBar bar = new STexturedProgressBar(getResource(BAR_EMPTY), getResource(BAR_FILLED));

    private JLabel user = new JLabel("Connexion...", SwingConstants.CENTER);
    private PlaceholderTextField usernameField = new PlaceholderTextField();
    private PlaceholderPasswordField passwordField = new PlaceholderPasswordField();

    private YlinorUser auth = new YlinorUser();
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
        if (Launcher.getUsername(null) == null) {
            usernameField.setPlaceholder("E-Mail");
        }
        else {
            usernameField.setText(Launcher.getUsername(null));
        }
        this.add(usernameField);

        passwordField.setBounds(100, 355, 425, 25);
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        passwordField.setBackground(getTransparentWhite(100));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(passwordField.getForeground());
        passwordField.setFont(usernameField.getFont());
        passwordField.setPlaceholder("Mot de passe");
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
                    auth.fetch(Launcher.getToken());
                    logged = auth.user();
                }
                catch (Exception e) {
                }

                if (logged == null) {
                    user.setText("Connecte toi !");
                    setFieldsEnabled(true);
                }
                else {
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
    public void onEvent(SwingerEvent event) {
        Thread thread = new Thread(() -> {
            setFieldsEnabled(false);

            if (logged == null) {
                try {
                    auth.login(usernameField.getText(), passwordField.getText());
                    logged = auth.user();
                }
                catch (AuthException e) {
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
            }
            catch (Exception e) {
                Launcher.shutdown();
                YlinorLauncher.handleCrash("Erreur pendant la mise à jour !", e);
            }

            try {
                Launcher.launch(logged);
            } catch (Exception e) {
                YlinorLauncher.handleCrash(e instanceof LaunchException ? "Erreur de lancement !" : e instanceof InvocationTargetException ? "Le jeu a crashé !" : "Erreur inconnue !", e);
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

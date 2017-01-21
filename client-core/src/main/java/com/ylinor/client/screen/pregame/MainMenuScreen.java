package com.ylinor.client.screen.pregame;

import static com.ylinor.client.YlinorClient.client;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.ylinor.client.screen.YlinorScreen;


public class MainMenuScreen extends YlinorScreen {
    public MainMenuScreen() {
        VisTable table = new VisTable();

        VisTextButton play = new VisTextButton("Jouer");
        play.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                client().setScreen(null);
            }
        });
        table.add(play);
        table.setFillParent(true);

        addActor(table);
    }
}

package com.ylinor.client.screen.pregame;

import javax.inject.Inject;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.YlinorScreen;


public class ConnectingScreen extends YlinorScreen {
    @Inject
    private Assets assets;

    @Override
    public void show() {
        super.show();
        VisTable table = new VisTable();
        
        TextureRegion backgroundRegion = new TextureRegion(assets.screen.mainMenuBackground());
        table.setBackground(new TextureRegionDrawable(backgroundRegion));

        table.setFillParent(true);

        table.add(new VisLabel("coucou"));
        addActor(table);
    }
}

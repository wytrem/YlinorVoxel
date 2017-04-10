package com.ylinor.client.screen.pregame;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;
import com.kotcrab.vis.ui.widget.VisTextButton.VisTextButtonStyle;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.render.ScreenSystem;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.YlinorScreen;

public class MainMenuScreen extends YlinorScreen {

	@Wire
	private ScreenSystem screenSystem;

	@Wire
	private YlinorClient client;
	
	@Wire
	private Assets assets;

	public MainMenuScreen() {
	}

	Image logo;

	@Override
	public void show() {
		super.show();

		VisTable table = new VisTable();

		TextureRegion backgroundRegion = new TextureRegion(assets.screen.mainMenuBackground());
		table.setBackground(new TextureRegionDrawable(backgroundRegion));

		table.setFillParent(true);

		{
			logo = new Image(assets.screen.mainMenuLogo());
			logo.setWidth(350);
			logo.setHeight(150);
			table.addActor(logo);
		}

		{
			final VisTextButtonStyle ylButtonStyle = new VisTextButtonStyle();
			ylButtonStyle.font = assets.screen.liberation().generateFont(new FreeTypeFontParameter());
			ylButtonStyle.up = new NinePatchDrawable(assets.screen.bigTextButtonNormal());

			VisTextButton play = new VisTextButton("Jouer", ylButtonStyle);
			play.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent event, float x, float y) {
					super.clicked(event, x, y);
					screenSystem.setScreen(null);
					client.connectToServer();
					client.isInGame = true;
				}
			});

			table.add(play);
		}

		addActor(table);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}

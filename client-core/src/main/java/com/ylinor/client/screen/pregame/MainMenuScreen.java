package com.ylinor.client.screen.pregame;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import com.ylinor.client.network.ClientNetworkSystem;
import com.ylinor.client.resource.Assets;
import com.ylinor.client.screen.YlinorScreen;
import com.ylinor.packets.PacketLogin;

public class MainMenuScreen extends YlinorScreen {
    
    private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);
    
	@Inject
	private YlinorClient client;
	
	@Inject
	private Assets assets;

	@Inject
	private ClientNetworkSystem networkSystem;
	
    private String host = "git.ylinor.com";
    private int port = 18325;
	
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
					try {
						connectToServer();
					} catch (IOException e) {
						throw new RuntimeException(e); // TODO mieux gérer les exceptions :-D
					}
					client.isInGame = true;
				}
			});

			table.add(play);
		}

		addActor(table);
	}
	
	public void connectToServer() throws IOException {
        logger.info("Connecting to server {}:{}.", host, port);
        networkSystem.init(InetAddress.getByName(host), port);

        networkSystem.enqueuePacket(new PacketLogin(UUID.randomUUID())); // TODO
    }

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}
}

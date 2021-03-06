package com.goonsquad.galactictd.systems.initialization;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.goonsquad.galactictd.GalacticTDGame;
import com.goonsquad.galactictd.components.graphics.Renderable;
import com.goonsquad.galactictd.components.input.Event;
import com.goonsquad.galactictd.components.input.Touchable;
import com.goonsquad.galactictd.components.layers.Layer;
import com.goonsquad.galactictd.components.layers.LayerLevel;
import com.goonsquad.galactictd.components.positional.MoveToPoint;
import com.goonsquad.galactictd.components.positional.MovementDestination;
import com.goonsquad.galactictd.components.positional.MovementSpeed;
import com.goonsquad.galactictd.components.positional.ResetPosition;
import com.goonsquad.galactictd.components.positional.Spatial;
import com.goonsquad.galactictd.screens.PlayScreen;
import com.goonsquad.galactictd.screens.ScoreScreen;
import com.goonsquad.galactictd.systems.archetypes.HomeScreenArchetypeBuilder;
import com.goonsquad.galactictd.systems.positional.MoveToPointSystem;
import com.goonsquad.galactictd.systems.positional.ResetPositionSystem;
import com.goonsquad.galactictd.systems.state.ShowOverlaySystem;

public class HomeScreenInitSystem extends InitializationSystem {
    private HomeScreenArchetypeBuilder archetypeBuilder;
    private ComponentMapper<Spatial> spatialComponentMapper;
    private ComponentMapper<Renderable> renderableComponentMapper;
    private ComponentMapper<Touchable> touchableComponentMapper;
    private ComponentMapper<MoveToPoint> moveToPointComponentMapper;
    private ComponentMapper<MovementDestination> movementDestinationComponentMapper;
    private ComponentMapper<MovementSpeed> movementSpeedComponentMapper;
    private ComponentMapper<ResetPosition> resetPositionComponentMapper;
    private ComponentMapper<Layer> layerComponentMapper;
    private ShowOverlaySystem showOverlaySystem;
    private MoveToPointSystem moveToPointSystem;
    private ResetPositionSystem resetPositionSystem;

    private GalacticTDGame gameInstance;
    private Vector2 buttonSize;

    public HomeScreenInitSystem(GalacticTDGame game) {
        this.gameInstance = game;
        buttonSize = new Vector2(300f, 150f);
    }

    @Override
    protected void populateWorld() {
        createTitle();
        createPlayButton();
        createScoreButton();
        createQuitButton();
        createSettingsButton();
        createSettingsDock();
        createSettingsOverlay();
        showOverlaySystem.hideOverlay();
    }

    private void createTitle() {
        int title = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.uiLabel);

        Renderable titleRenderable = renderableComponentMapper.get(title);
        titleRenderable.texture = gameInstance.assets.manager.get("galacticTD.png", Texture.class);

        Spatial titleSpatial = spatialComponentMapper.get(title);
        titleSpatial.setBounds(
                (GalacticTDGame.UI_WIDTH / 3f), (GalacticTDGame.UI_HEIGHT * 5 / 8),
                (GalacticTDGame.UI_WIDTH / 3f), (GalacticTDGame.UI_HEIGHT / 3f));
    }

    private void createPlayButton() {
        int playButton = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.uiButton);

        Renderable playRenderable = renderableComponentMapper.get(playButton);
        playRenderable.texture = gameInstance.assets.manager.get("buttonPlay.png", Texture.class);

        Spatial playSpatial = spatialComponentMapper.get(playButton);
        playSpatial.setBounds(
                (GalacticTDGame.UI_WIDTH * 1f / 3f), (GalacticTDGame.UI_HEIGHT * 3f / 8f),
                (GalacticTDGame.UI_WIDTH * 1f / 3f), (GalacticTDGame.UI_HEIGHT * 1f / 3f));

        Touchable touchable = touchableComponentMapper.get(playButton);
        touchable.event = new Event() {
            @Override
            public void fireEvent() {
                gameInstance.getScreenManager().setScreen(PlayScreen.class);
            }
        };
    }

    private void createScoreButton() {
        int scoreButton = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.uiButton);

        Renderable scoreRenderable = renderableComponentMapper.get(scoreButton);
        scoreRenderable.texture = gameInstance.assets.manager.get("buttonScore.png", Texture.class);

        Spatial scoreSpatial = spatialComponentMapper.get(scoreButton);
        scoreSpatial.setBounds(
                (GalacticTDGame.UI_WIDTH * (1f / 24f)), (GalacticTDGame.UI_HEIGHT * (1f / 8f)),
                (buttonSize.x), (buttonSize.y));

        Touchable touchable = touchableComponentMapper.get(scoreButton);
        touchable.event = new Event() {
            @Override
            public void fireEvent() {
                gameInstance.getScreenManager().setScreen(ScoreScreen.class);
            }
        };
    }

    private void createQuitButton() {
        int quitButton = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.uiButton);

        Renderable quitRenderable = renderableComponentMapper.get(quitButton);
        quitRenderable.texture = gameInstance.assets.manager.get("buttonQuit.png", Texture.class);

        Spatial quitSpatial = spatialComponentMapper.get(quitButton);
        quitSpatial.setBounds(
                (GalacticTDGame.UI_WIDTH * (5f / 12f)), (GalacticTDGame.UI_HEIGHT / 24f),
                (buttonSize.x), (buttonSize.y));

        Touchable touchable = touchableComponentMapper.get(quitButton);
        touchable.event = new Event() {
            @Override
            public void fireEvent() {
                Gdx.app.exit();
            }
        };
    }

    private void createSettingsButton() {
        int settingsButton = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.uiButton);

        Renderable settingsRenderable = renderableComponentMapper.get(settingsButton);
        settingsRenderable.texture = gameInstance.assets.manager.get("settings.png", Texture.class);

        Spatial settingsSpatial = spatialComponentMapper.get(settingsButton);
        settingsSpatial.setBounds(
                (GalacticTDGame.UI_WIDTH - (GalacticTDGame.UI_WIDTH / 24f) - buttonSize.x), (GalacticTDGame.UI_HEIGHT / 8f),
                (buttonSize.x), (buttonSize.y));

        Touchable touchable = touchableComponentMapper.get(settingsButton);
        touchable.event = new Event() {
            @Override
            public void fireEvent() {
                showOverlaySystem.showOverlay();
            }
        };
    }

    private void createSettingsOverlay() {
        int overlay = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.overlayButton);

        Layer overlayLayer = layerComponentMapper.get(overlay);
        overlayLayer.layerLevel = LayerLevel.OVERLAY;

        Renderable overlayRenderable = renderableComponentMapper.get(overlay);
        overlayRenderable.texture = gameInstance.assets.manager.get("black.png", Texture.class);
        overlayRenderable.a = .65f;

        Spatial overlaySpatial = spatialComponentMapper.get(overlay);
        overlaySpatial.setBounds(0, 0, GalacticTDGame.UI_WIDTH, GalacticTDGame.UI_HEIGHT);

        Touchable overlayTouch = touchableComponentMapper.get(overlay);
        overlayTouch.event = new Event() {
            @Override
            public void fireEvent() {
                showOverlaySystem.hideOverlay();
            }
        };
    }

    private void createSettingsDock() {
        int settingsDock = archetypeBuilder.buildArchetype(HomeScreenArchetypeBuilder.dock);

        Layer dockLayer = layerComponentMapper.get(settingsDock);
        dockLayer.layerLevel = LayerLevel.OVERLAY_1;

        Renderable settingsRenderable = renderableComponentMapper.get(settingsDock);
        settingsRenderable.texture = gameInstance.assets.manager.get("border.png", Texture.class);

        Spatial dockSpatial = spatialComponentMapper.get(settingsDock);
        dockSpatial.width = GalacticTDGame.UI_WIDTH / 4f;
        dockSpatial.height = dockSpatial.width;

        ResetPosition dockResetCords = resetPositionComponentMapper.get(settingsDock);
        dockResetCords.resetPositionX = GalacticTDGame.UI_WIDTH;
        dockResetCords.resetPositionY = 0 - dockSpatial.height;

        dockSpatial.x = dockResetCords.resetPositionX;
        dockSpatial.y = dockResetCords.resetPositionY;

        MoveToPoint dockMoveToPoint = moveToPointComponentMapper.get(settingsDock);
        dockMoveToPoint.moving = false;

        MovementDestination movementDestination = movementDestinationComponentMapper.create(settingsDock);
        movementDestination.destinationX = GalacticTDGame.UI_WIDTH - dockSpatial.width / 2;
        movementDestination.destinationY = dockSpatial.height / 2f;

        MovementSpeed movementSpeed = movementSpeedComponentMapper.create(settingsDock);
        movementSpeed.unitsPerSecond = 3000f;
    }
}

package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Prueba extends ApplicationAdapter {
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private TiledMap map;
    private SpriteBatch batch;
    private Texture textura;
    private int frameWidth = 256;
    private int frameHeight = 256;
    //private int numFrames = 8; // Ajusta según la cantidad de frames
    private Animation<TextureRegion> attackAnimation;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private float stateTime = 0;
    private float jumpTime = 0;
    private boolean isJumping = true;
    private TextureRegion currentFrame;
    private MapLayer capaSuelo;
    private float personajeX;
    private float personajeY;

    @Override
    public void create() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);

        map = new TmxMapLoader().load("TiledJuego/MapaJuego.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        capaSuelo = map.getLayers().get("Capa suelo");
        batch = new SpriteBatch();
        attackAnimation = createAnimation("Personaje/Attack_2_out.png",frameWidth, 148,4);
        runAnimation = createAnimation("Personaje/Run_out.png", 256, 256, 8);
        jumpAnimation = createAnimation("Personaje/Jump_out.png", frameWidth, frameHeight, 12);

    }

    private Animation<TextureRegion> createAnimation(String pathAnimacion, int frameWidth, int frameHeight, int numFrames) {
        textura = new Texture(Gdx.files.internal(pathAnimacion));
        TextureRegion[][] tmpFrames = TextureRegion.split(textura, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[numFrames];

        int frameIndex = 0;
        for (int i = 0; i < numFrames; i++) {
            frames[frameIndex++] = tmpFrames[0][i];
        }

        float frameDuration = 0.1f; // Ajusta la duración de cada frame
        return new Animation<>(frameDuration, frames);
    }

    @Override
    public void render() {

        camera.update();
        renderer.setView(camera);
        renderer.render();

        stateTime += Gdx.graphics.getDeltaTime();

        // Obtén la posición en términos de celdas
        personajeX = Gdx.graphics.getWidth() / 2f - frameWidth / 2f;
        personajeY = (int) 128;

        if (Gdx.input.justTouched()) {
            isJumping = true;
            jumpTime = 0;
        }

        if (isJumping) {
            jumpTime += Gdx.graphics.getDeltaTime();

            // Verifica si la animación de salto ha terminado
            if (jumpTime > jumpAnimation.getAnimationDuration()) {
                isJumping = false;
                jumpTime = 0;
            }
            // Obtén el frame actual de la animación de salto
            currentFrame = jumpAnimation.getKeyFrame(jumpTime, false); // Utiliza 'false' para no repetir la animación
        } else {
            // Renderiza el frame de correr
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }

        batch.begin();
        batch.draw(currentFrame, personajeX, personajeY);
        batch.end();
    }



    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        batch.dispose();
        textura.dispose();
    }

}
package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	private SpriteBatch batch;
	private Personaje personaje;
	private Button attackButton;
	private Stage stage;

	@Override
	public void create() {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);

		map = new TmxMapLoader().load("TiledJuego/MapaJuego.tmx");
		renderer = new OrthogonalTiledMapRenderer(map);

		// Crear el personaje en una posición inicial
		personaje = new Personaje(Gdx.graphics.getWidth() / 2f - 256 / 2f, 128);
		batch = new SpriteBatch();

		Texture buttonTexture = new Texture(Gdx.files.internal("TiledJuego/boton_ataque.png"));
		TextureRegionDrawable buttonDrawable = new TextureRegionDrawable(new TextureRegion(buttonTexture));

		// Crear el botón
		attackButton = new Button(buttonDrawable);
		attackButton.setPosition(1600, 300); // Ajusta las coordenadas según tu diseño
		attackButton.setSize(200, 200); // Ajusta el tamaño según tu diseño

		// Configurar el evento de clic del botón
		attackButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				// Lógica cuando se hace clic en el botón de ataque
				personaje.attack();
			}
		});

		// Crear el escenario y agregar el botón al escenario
		stage = new Stage(new ScreenViewport());
		stage.addActor(attackButton);
		attackButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.log("entro 2",attackButton.isPressed()+" "+attackButton.isOver());
				return true;
			}


		});
		Gdx.input.setInputProcessor(new InputAdapter() {
			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				Vector3 touchPoint = new Vector3(screenX, screenY, 0);
				camera.unproject(touchPoint);

				// Obtener las coordenadas del botón
				float buttonX = attackButton.getX();
				float buttonY = attackButton.getY();

				// Obtener el ancho y alto del botón
				float buttonWidth = attackButton.getWidth();
				float buttonHeight = attackButton.getHeight();

				// Verificar si el toque ocurrió dentro de la región del botón de ataque
				if (touchPoint.x >= buttonX && touchPoint.x <= buttonX + buttonWidth &&
						touchPoint.y >= buttonY && touchPoint.y <= buttonY + buttonHeight) {
					// Lógica cuando se hace clic en el botón de ataque
					personaje.attack();
				}
				else {
					personaje.jump();
				}
				return true;
			}
		});
	}

	@Override
	public void render() {
		camera.update();
		renderer.setView(camera);
		renderer.render();
		personaje.render(batch);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
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
	}
}


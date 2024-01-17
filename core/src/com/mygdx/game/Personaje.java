package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Personaje {
    private float personajeX;
    private float personajeY;
    private Texture textura;
    private float stateTime = 0;
    private float jumpTime = 0;
    private boolean isJumping = false;
    private boolean isAttacking = false;
    private float attackTime = 0;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> runAnimation;
    private Animation<TextureRegion> jumpAnimation;
    private Animation<TextureRegion> attackAnimation;

    public Personaje(float initialX, float initialY) {
        personajeX = initialX;
        personajeY = initialY;
        stateTime = 0;

        runAnimation = createAnimation("Personaje/Run_out.png", 256, 256, 8);
        jumpAnimation = createAnimation("Personaje/Jump_out.png", 256, 256, 12);
        attackAnimation = createAnimation("Personaje/Attack_2_out.png", 256, 148, 4);
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
    public void attack() {
        if (!isAttacking) {
            isAttacking = true;
            attackTime = 0;
        }
    }

    public void jump() {
        if (!isJumping && !isAttacking) {
            isJumping = true;
            jumpTime = 0;
        }
    }
    public void render(SpriteBatch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        if (isAttacking) {
            attackTime += Gdx.graphics.getDeltaTime();

            // Verifica si la animación de ataque ha terminado
            if (attackTime > attackAnimation.getAnimationDuration()) {
                isAttacking = false;
                attackTime = 0;
            }
            // Obtén el frame actual de la animación de ataque
            currentFrame = attackAnimation.getKeyFrame(attackTime, false);
        }
        else if (isJumping) {
            jumpTime += Gdx.graphics.getDeltaTime();

            // Verifica si la animación de salto ha terminado
            if (jumpTime > jumpAnimation.getAnimationDuration()) {
                isJumping = false;
                jumpTime = 0;
            }
            // Obtén el frame actual de la animación de salto
            currentFrame = jumpAnimation.getKeyFrame(jumpTime, false); // Utiliza 'false' para no repetir la animación
        }else {
            // Renderiza el frame de correr
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }
        batch.begin();
        batch.draw(currentFrame, personajeX, personajeY);
        batch.end();
    }
}


package de.diegrafen.exmatrikulatortd.view.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 *
 * Das Spielobjekt eines Turms
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:03
 */
public class TowerObject extends BaseObject {

    /**
     * Konstruktor für Turm-Objekte
     * @param name Der Name des Spielobjektes
     * @param assetsName Die mit dem Objekt assoziierten Assets
     * @param xPosition Die x-Position
     * @param yPosition Die y-Position
     */
    public TowerObject(String name, String assetsName, float xPosition, float yPosition) {
        super(name, assetsName, xPosition, yPosition);
    }


    public TowerObject(Tower tower) {
        super(tower.getTowerName(), tower.getAssetsName());
        setNewPosition(tower.getxPosition(), tower.getyPosition());
        System.out.println("Turm erzeugt!");
    }

    /**
     * Update-Methode. Aktualisiert den Zustand des Objektes
     *
     * @param deltaTime Die Zeit zwischen zwei Frames
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

    /**
     * Zeichnet das Objekt auf dem Bildschirm
     *
     * @param spriteBatch Der spriteBatch, mit dem Objekt gerendert wird
     */
    @Override
    public void draw(SpriteBatch spriteBatch) {
        super.draw(spriteBatch);
        spriteBatch.draw(getCurrentSprite(), getxPosition(), getyPosition());
    }

    /**
     * Entfernt das Spielobjekt
     */
    @Override
    public void dispose() {
        super.dispose();
    }
}

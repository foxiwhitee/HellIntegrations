package foxiwhitee.hellmod.integration.draconic.client.gui.modulargui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class GuiEffectRenderer {
    private final List<GuiEffect> effects = new ArrayList<>();


    public void updateEffects() {
        Iterator<GuiEffect> i = this.effects.iterator();

        while (i.hasNext()) {
            GuiEffect effect = i.next();

            if (effect.isAlive()) {
                effect.onUpdate();
                continue;
            }
            i.remove();
        }
    }


    public void renderEffects(float partialTick) {
        for (GuiEffect effect : this.effects) {

            if (effect.isTransparent()) {
                GL11.glEnable(3042);

                GL11.glAlphaFunc(516, 0.0F);
            }


            GL11.glDisable(2896);

            effect.renderParticle(partialTick);

            if (effect.isTransparent()) {
                GL11.glAlphaFunc(516, 0.1F);
            }
        }
    }

    public void addEffect(GuiEffect effect) {
        this.effects.add(effect);
    }

    public void clearEffects() {
        this.effects.clear();
    }

    public List<GuiEffect> getActiveEffects() {
        return this.effects;
    }
}
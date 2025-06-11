package foxiwhitee.hellmod.integration.botania.tile.spreaders;

import net.minecraft.entity.Entity;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.common.block.tile.mana.TileSpreader;
import vazkii.botania.common.entity.EntityManaBurst;

import java.util.Random;
import java.util.UUID;

public abstract class TileCustomSpreader extends TileSpreader {
    public TileCustomSpreader() {}

    public static int SIZE_PARTICLES = 4;

    public int getMaxMana() {
        return getManaPerSec() * 10;
    }

    public void pingback(IManaBurst burst, UUID expectedIdentity) {
        if (getIdentifier().equals(expectedIdentity)) {
            this.pingbackTicks = 10;
            Entity e = (Entity)burst;
            this.lastPingbackX = e.posX;
            this.lastPingbackY = e.posY;
            this.lastPingbackZ = e.posZ;
            setCanShoot(false);
        }
    }

    public EntityManaBurst getBurst(boolean fake) {
        EntityManaBurst burst = super.getBurst(fake);
        if (burst != null) {
            switch ((new Random()).nextInt(3)) {
                case 0:
                    burst.setColor(20470888);
                    break;
                case 1:
                    burst.setColor(40027263);
                    break;
                default:
                    burst.setColor(1555150);
                    break;
            }
            burst.setStartingMana(Math.min(getCurrentMana(), getManaPerSec()) / SIZE_PARTICLES);
            burst.setMana(Math.min(getCurrentMana(), getManaPerSec()) / SIZE_PARTICLES);
            burst.setManaLossPerTick(20.0F);
            burst.setMinManaLoss(120);
        }
        return burst;
    }

    public abstract String getName();

    public abstract int getManaPerSec();
}

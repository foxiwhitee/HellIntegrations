package foxiwhitee.hellmod.integration.draconic.client.sound;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.draconic.tile.TileFusionCraftingCore;
import net.minecraft.client.audio.ITickableSound;
import net.minecraft.client.audio.PositionedSound;
import net.minecraft.util.ResourceLocation;

public class FusionRotationSound extends PositionedSound implements ITickableSound {
    private final TileFusionCraftingCore tile;

    public FusionRotationSound(TileFusionCraftingCore tile) {
        super(new ResourceLocation(HellCore.MODID, "fusion_rotation"));
        this.tile = tile;
        this.xPosF = tile.xCoord + 0.5F;
        this.yPosF = tile.yCoord + 0.5F;
        this.zPosF = tile.zCoord + 0.5F;
        this.repeat = true;
        this.volume = 1.5F;
    }

    public boolean isDonePlaying() {
        return (this.tile.isInvalid() || !this.tile.craftingInProgress());
    }

    public void update() {
        this.field_147663_c = 0.1F + (this.tile.getCraftingStage() - 1000) / 1000.0F * 1.9F;
    }
}

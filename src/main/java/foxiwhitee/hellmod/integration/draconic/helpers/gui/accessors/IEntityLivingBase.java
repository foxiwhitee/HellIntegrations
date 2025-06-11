package foxiwhitee.hellmod.integration.draconic.helpers.gui.accessors;

import net.minecraft.entity.player.EntityPlayer;

public interface IEntityLivingBase {
    void setCustomSpawner(boolean paramBoolean);

    void setRecentlyHit(int paramInt);

    int callGetExperiencePoints(EntityPlayer paramEntityPlayer);
}

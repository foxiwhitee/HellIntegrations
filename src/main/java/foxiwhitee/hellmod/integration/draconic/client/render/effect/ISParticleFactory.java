package foxiwhitee.hellmod.integration.draconic.client.render.effect;

import foxiwhitee.hellmod.integration.draconic.helpers.Cord3D;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public interface ISParticleFactory {
    EntityFX getEntityFX(int paramInt, World paramWorld, Cord3D paramVec3D1, Cord3D paramVec3D2, int... paramVarArgs);
}


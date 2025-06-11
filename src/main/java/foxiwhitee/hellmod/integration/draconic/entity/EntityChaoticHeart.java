package foxiwhitee.hellmod.integration.draconic.entity;

import com.brandon3055.brandonscore.common.utills.Utills;
import com.brandon3055.draconicevolution.client.handler.ParticleHandler;
import com.brandon3055.draconicevolution.client.render.particle.Particles;
import com.brandon3055.draconicevolution.common.ModItems;
import com.brandon3055.draconicevolution.common.blocks.DraconicBlock;
import com.brandon3055.draconicevolution.common.blocks.multiblock.MultiblockHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.config.HellConfig;
import foxiwhitee.hellmod.integration.draconic.DraconicEvolutionIntegration;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

import java.util.ArrayList;
import java.util.List;

public class EntityChaoticHeart extends Entity {
    //Class taken from Draconic Evolution

    public int age = 0;
    public float rotation = 0f;
    public float rotationInc = 0.5f;
    public int opPhase = 0;
    private double yStop;
    private int coresConsumed = 0;
    private List<MultiblockHelper.TileLocation> blocks = new ArrayList<MultiblockHelper.TileLocation>();

    public EntityChaoticHeart(World world) {
        super(world);
        this.setSize(0.25F, 0.25F);
    }

    public EntityChaoticHeart(World par1World, double x, double y, double z) {
        super(par1World);
        this.setPosition(x, y, z);
        this.motionX = 0;
        this.motionY = 0;
        this.motionZ = 0;
        this.setSize(0.25F, 0.25F);
        this.yStop = y + 1.5D;
    }

    @Override
    protected void entityInit() {
        renderDistanceWeight = 10;
        getDataWatcher().addObject(11, (float) yStop);
        getDataWatcher().addObject(12, rotationInc);
        getDataWatcher().addObject(13, coresConsumed);
        getDataWatcher().addObject(14, opPhase);
    }

    @Override
    public boolean attackEntityFrom(DamageSource damageSource, float dmg) {
        return false;
    }

    private void randomBolt() {
        int x = -10 + worldObj.rand.nextInt(200);
        int z = -10 + worldObj.rand.nextInt(200);
        EntityLightningBolt bolt = new EntityLightningBolt(worldObj, x, worldObj.getTopSolidOrLiquidBlock(z, z) - 1, z);
        if (!worldObj.isRemote) worldObj.addWeatherEffect(bolt);
    }

    @Override
    public void onUpdate() {

        if (!worldObj.isRemote) getDataWatcher().updateObject(14, opPhase);
        if (!worldObj.isRemote) getDataWatcher().updateObject(13, coresConsumed);
        if (!worldObj.isRemote) getDataWatcher().updateObject(12, rotationInc);
        if (!worldObj.isRemote) getDataWatcher().updateObject(11, (float) yStop);
        yStop = getDataWatcher().getWatchableObjectFloat(11);
        rotationInc = getDataWatcher().getWatchableObjectFloat(12);
        coresConsumed = getDataWatcher().getWatchableObjectInt(13);
        opPhase = getDataWatcher().getWatchableObjectInt(14);
        motionX = 0;
        motionZ = 0;
        motionY = 0;


        age++;
        rotation += rotationInc;
        super.onUpdate();

        switch (opPhase) {
            case 0: {
                motionY = 0.02f;
                rotationInc += 0.2f;

                if (posY > yStop) {
                    opPhase = 1;
                }

                int rTime = 2000;
                if (dimension == 0) {
                    WorldInfo worldInfo = worldObj.getWorldInfo();
                    worldInfo.setRainTime(rTime);
                    worldInfo.setThunderTime(rTime);
                    worldInfo.setRaining(true);
                    worldInfo.setThundering(true);
                }
                break;
            }
            case 1: {
                opPhase = 2;
                motionY = -0.01f;
                randomBolt();
                EntityLightningBolt bolt = new EntityLightningBolt(worldObj, posX, posY, posZ);
                if (!worldObj.isRemote) worldObj.addWeatherEffect(bolt);
                this.worldObj.playSoundAtEntity(this, "mob.wither.death", 0.4F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                break;
            }
            case 2: {
                if (coresConsumed == HellConfig.coresNeedsForChaotic || age > 1240) {
                    age = 1240;
                    opPhase = 3;
                    if (coresConsumed < 2) {
                        EntityHeart item = new EntityHeart(worldObj, posX, posY, posZ, new ItemStack(DraconicEvolutionIntegration.chaoticHeart));
                        item.motionX = 0;
                        item.motionY = 1;
                        item.motionZ = 0;
                        item.delayBeforeCanPickup = 0;
                        if (!worldObj.isRemote) worldObj.spawnEntityInWorld(item);
                        this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                        this.setDead();
                    }
                    break;
                }
                @SuppressWarnings("unchecked") List<EntityItem> items = worldObj.getEntitiesWithinAABB(EntityItem.class, AxisAlignedBB.getBoundingBox(posX - 5, posY - 5, posZ - 5, posX + 5, posY + 5, posZ + 5));
                for (EntityItem item : items) {
                    if (item.isDead)
                        continue;

                    ItemStack stack = item.getEntityItem();
                    if (Utills.getDistanceAtoB(posX, posY + 0.5, posZ, item.posX, item.posY, item.posZ) < 1) {
                        if (coresConsumed == HellConfig.coresNeedsForChaotic || worldObj.isRemote) break;
                        if (stack.getItem() != ModItems.awakenedCore) {
                            item.motionX = 1;
                            item.motionY = 6;
                            item.motionZ = 1;
                            continue;
                        }
                        int needed = HellConfig.coresNeedsForChaotic - coresConsumed;
                        if (stack.stackSize >= needed) {
                            coresConsumed = HellConfig.coresNeedsForChaotic;
                            stack.stackSize -= needed;
                        } else {
                            coresConsumed += stack.stackSize;
                            stack.stackSize = 0;
                            item.setDead();
                        }
                        this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    } else {
                        item.motionX = ((posX - item.posX)) * 0.1;
                        item.motionY = ((posY + 0.5 - item.posY)) * 0.1;
                        item.motionZ = ((posZ - item.posZ)) * 0.1;
                    }
                }
                break;
            }
            case 3: {
                rotationInc += 0.3f;
                int maxBlocks = coresConsumed / 2;
                if (age % 10 == 0) {
                    blocks = new ArrayList<MultiblockHelper.TileLocation>();
                    for (int x = (int) posX - 5; x <= (int) posX + 5; x++) {
                        for (int y = (int) posY - 5; y <= (int) posY + 5; y++) {
                            for (int z = (int) posZ - 5; z <= (int) posZ + 5; z++) {
                                if (worldObj.getBlock(x, y, z) instanceof DraconicBlock && worldObj.getBlockMetadata(x, y, z) == 1) {
                                    MultiblockHelper.TileLocation block = new MultiblockHelper.TileLocation(x, y, z);
                                    if (!blocks.contains(block) && blocks.size() < maxBlocks) blocks.add(block);
                                }
                            }
                        }
                    }
                }
                if (age > 1600) opPhase = 4;

                EntityLightningBolt bolt = new EntityLightningBolt(worldObj, posX, posY, posZ);
                if (age > 1600) worldObj.addWeatherEffect(bolt);
                break;
            }
            case 4: {
                if (blocks.size() == 0) {
                    if (!worldObj.isRemote) {
                        EntityHeart item = new EntityHeart(worldObj, posX, posY, posZ, new ItemStack(DraconicEvolutionIntegration.chaoticHeart));
                        item.motionX = 0;
                        item.motionY = 0;
                        item.motionZ = 0;
                        item.delayBeforeCanPickup = 0;
                        worldObj.spawnEntityInWorld(item);
                    }
                    this.worldObj.playSoundAtEntity(this, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
                    this.setDead();
                    break;
                }
                for (MultiblockHelper.TileLocation tile : blocks) {
                    if (!worldObj.isRemote)
                        worldObj.setBlock(tile.getXCoord(), tile.getYCoord(), tile.getZCoord(), DraconicEvolutionIntegration.chaotic_block, 0, 2);
                    worldObj.createExplosion(null, tile.getXCoord(), tile.getYCoord(), tile.getZCoord(), 4, false);
                }
                worldObj.createExplosion(null, posX, posY, posZ, 4, false);

                this.setDead();
            }
        }

        if (worldObj.isRemote) spawnParticles();
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
        if (opPhase == 0) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 10; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat);
                double offsetZ = Math.cos(nextFloat);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 1, 200f - colourMod, 1f - colourMod, 24f - colourMod, 10);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 1) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 100; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat);
                double offsetZ = Math.cos(nextFloat);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 3, 1f, 1f, 1f, 100, -100);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 1) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 10; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat);
                double offsetZ = Math.cos(nextFloat);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 1, 200f - colourMod, 1f - colourMod, 24f - colourMod, 10);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 2) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 10; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat) * (rand.nextFloat() * 10);
                double offsetZ = Math.cos(nextFloat) * (rand.nextFloat() * 10);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 3, 200f - colourMod, 1f - colourMod, 24f - colourMod, 100, -100);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 2) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 10; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat);
                double offsetZ = Math.cos(nextFloat);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 1, 200f - colourMod, 1f - colourMod, 24f - colourMod, 10);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 3) {

            float colourMod = 360f;
            double correctY = posY + 0.5;
            int nextFloat = rand.nextInt();
            double offsetX = Math.sin(nextFloat) * (rand.nextFloat() * 10);
            double offsetZ = Math.cos(nextFloat) * (rand.nextFloat() * 10);
            EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX, correctY, posZ, posX + offsetX, correctY, posZ + offsetZ, 3, 200f - colourMod, 1f - colourMod, 24f - colourMod, 100, -100);
            ParticleHandler.spawnCustomParticle(particle, 64);

            for (MultiblockHelper.TileLocation tile : blocks) {
                particle = new Particles.AdvancedSeekerParticle(worldObj, posX, correctY, posZ, tile.getXCoord() + rand.nextDouble(), tile.getYCoord() + rand.nextDouble(), tile.getZCoord() + rand.nextDouble(), 3, 1f, 1f, 1f, 100, -100);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 3) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 10; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat);
                double offsetZ = Math.cos(nextFloat);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 1, 200f - colourMod, 1f - colourMod, 24f - colourMod, 10);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
        if (opPhase == 3) {
            float colourMod = 360f;
            double correctY = posY + 0.5;
            for (int i = 0; i < 10; i++) {
                int nextFloat = rand.nextInt();
                double offsetX = Math.sin(nextFloat) * (rand.nextFloat() * 10);
                double offsetZ = Math.cos(nextFloat) * (rand.nextFloat() * 10);
                EntityFX particle = new Particles.AdvancedSeekerParticle(worldObj, posX + offsetX, correctY, posZ + offsetZ, posX, correctY, posZ, 3, 1f, 1f, 1f, 100, -100);
                ParticleHandler.spawnCustomParticle(particle, 64);
            }
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (age < 2000) {
            age = 2000;
            opPhase = 2;
        }
    }


    @Override
    public void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("Age", age);
        compound.setInteger("Phase", opPhase);
        compound.setFloat("RotationSpeed", rotationInc);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound) {
        age = compound.getInteger("Age");
        opPhase = compound.getInteger("Phase");
        rotationInc = compound.getFloat("RotationSpeed");
    }
}

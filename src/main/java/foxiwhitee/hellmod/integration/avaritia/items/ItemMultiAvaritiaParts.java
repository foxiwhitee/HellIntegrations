package foxiwhitee.hellmod.integration.avaritia.items;

import appeng.api.AEApi;
import appeng.api.exceptions.MissingDefinition;
import appeng.api.implementations.items.IItemGroup;
import appeng.api.parts.IPart;
import appeng.api.parts.IPartHelper;
import appeng.api.parts.IPartItem;
import appeng.api.util.AEColor;
import appeng.core.AEConfig;
import appeng.core.features.AEFeature;
import appeng.core.features.ActivityState;
import appeng.core.features.ItemStackSrc;
import appeng.integration.IntegrationRegistry;
import appeng.integration.IntegrationType;
import appeng.items.AEBaseItem;
import com.google.common.base.Preconditions;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.integration.avaritia.parts.EnumPartsAvaritiaTerminals;
import foxiwhitee.hellmod.items.part.NameResolver;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class ItemMultiAvaritiaParts extends AEBaseItem implements IPartItem, IItemGroup {
    private static final int INITIAL_REGISTERED_CAPACITY = EnumPartsAvaritiaTerminals.values().length;
    private static final Comparator<Map.Entry<Integer, EnumPartsWithVariant>> REGISTERED_COMPARATOR = new RegisteredComparator();
    public static ItemMultiAvaritiaParts instance;
    private final NameResolver nameResolver;
    private final Map<Integer, EnumPartsWithVariant> registered;
    public ItemMultiAvaritiaParts(IPartHelper partHelper) {
        Preconditions.checkNotNull(partHelper);
        this.setUnlocalizedName("avaritiaPart");
        this.registered = new HashMap(INITIAL_REGISTERED_CAPACITY);
        this.setCreativeTab(HellCore.HELL_TAB);
        this.nameResolver = new NameResolver(this.getClass());
        this.setFeature(EnumSet.of(AEFeature.Core));
        partHelper.setItemBusRenderer(this);
        this.setHasSubtypes(true);
        instance = this;

        for(EnumPartsAvaritiaTerminals part : EnumPartsAvaritiaTerminals.values()) {
            if (part != EnumPartsAvaritiaTerminals.InvalidTypeAvaritia) {
                if (part.isCable()) {
                    for(AEColor color : AEColor.values()) {
                        this.createPart(part, color);
                    }
                }

                this.createPart(part, 0);
            }
        }

    }

    public Map<Integer, EnumPartsWithVariant> getRegistered() {
        return registered;
    }

    @Nonnull
    public ItemStackSrc createPart(EnumPartsAvaritiaTerminals mat) {
        Preconditions.checkNotNull(mat);
        return this.createPart(mat, 0);
    }

    @Nonnull
    public ItemStackSrc createPart(EnumPartsAvaritiaTerminals mat, AEColor color) {
        Preconditions.checkNotNull(mat);
        Preconditions.checkNotNull(color);
        int varID = color.ordinal();
        return this.createPart(mat, varID);
    }

    @Nonnull
    private ItemStackSrc createPart(EnumPartsAvaritiaTerminals mat, int varID) {
        assert mat != null;

        assert varID >= 0;

        for(EnumPartsWithVariant p : this.getRegistered().values()) {
            if (p.part == mat && p.variant == varID) {
                boolean enabled = true;

                for(AEFeature f : mat.getFeature()) {
                    enabled = enabled && AEConfig.instance.isFeatureEnabled(f);
                }

                for(IntegrationType integrationType : mat.getIntegrations()) {
                    enabled &= IntegrationRegistry.INSTANCE.isEnabled(integrationType);
                }

                int partDamage = mat.getBaseDamage() + varID;
                return new ItemStackSrc(this, partDamage, ActivityState.from(enabled));
            }
        }

        boolean enabled = true;

        for(AEFeature f : mat.getFeature()) {
            enabled = enabled && AEConfig.instance.isFeatureEnabled(f);
        }

        for(IntegrationType integrationType : mat.getIntegrations()) {
            enabled &= IntegrationRegistry.INSTANCE.isEnabled(integrationType);
        }

        int partDamage = mat.getBaseDamage() + varID;
        ActivityState state = ActivityState.from(enabled);
        ItemStackSrc output = new ItemStackSrc(this, partDamage, state);
        EnumPartsWithVariant pti = new EnumPartsWithVariant(mat, varID);
        this.processMetaOverlap(enabled, partDamage, mat, pti);
        return output;
    }

    private void processMetaOverlap(boolean enabled, int partDamage, EnumPartsAvaritiaTerminals mat, EnumPartsWithVariant pti) {
        assert partDamage >= 0;

        assert mat != null;

        assert pti != null;

        EnumPartsWithVariant registeredEnumParts = (EnumPartsWithVariant)this.getRegistered().get(partDamage);
        if (registeredEnumParts != null) {
            throw new IllegalStateException("Meta Overlap detected with type " + mat + " and damage " + partDamage + ". Found " + registeredEnumParts + " there already.");
        } else {
            if (enabled) {
                this.getRegistered().put(partDamage, pti);
            }

        }
    }

    public int getDamageByType(EnumPartsAvaritiaTerminals t) {
        Preconditions.checkNotNull(t);

        for(Map.Entry<Integer, EnumPartsWithVariant> pt : this.getRegistered().entrySet()) {
            if (((EnumPartsWithVariant)pt.getValue()).part == t) {
                return (Integer)pt.getKey();
            }
        }

        return -1;
    }

    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    public IIcon getIconFromDamage(int dmg) {
        EnumPartsWithVariant registeredType = (EnumPartsWithVariant)this.getRegistered().get(dmg);
        if (registeredType != null) {
            return registeredType.ico;
        } else {
            String formattedRegistered = Arrays.toString(this.getRegistered().keySet().toArray());
            throw new MissingDefinition("Tried to get the icon from a non-existent part with damage value " + dmg + ". There were registered: " + formattedRegistered + '.');
        }
    }

    public boolean onItemUse(ItemStack is, EntityPlayer player, World w, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return this.getTypeByStack(is) == EnumPartsAvaritiaTerminals.InvalidTypeAvaritia ? false : AEApi.instance().partHelper().placeBus(is, x, y, z, side, player, w);
    }

    public String getUnlocalizedName(ItemStack is) {
        return "item." + this.getName(is).toLowerCase();
    }

    public String getItemStackDisplayName(ItemStack is) {
        EnumPartsAvaritiaTerminals pt = this.getTypeByStack(is);
        if (pt.isCable()) {
            AEColor[] variants = AEColor.values();
            int itemDamage = is.getItemDamage();
            EnumPartsWithVariant registeredEnumParts = (EnumPartsWithVariant)this.getRegistered().get(itemDamage);
            if (registeredEnumParts != null) {
                return super.getItemStackDisplayName(is) + " - " + variants[registeredEnumParts.variant].toString();
            }
        }

        return pt.getExtraName() != null ? super.getItemStackDisplayName(is) + " - " + pt.getExtraName().getLocal() : super.getItemStackDisplayName(is);
    }

    public void registerIcons(IIconRegister iconRegister) {
        for(Map.Entry<Integer, EnumPartsWithVariant> part : this.getRegistered().entrySet()) {
            String tex = HellCore.MODID + ":ae2/terminals/" + this.getName(new ItemStack(this, 1, (Integer)part.getKey())).toLowerCase();
            ((EnumPartsWithVariant)part.getValue()).ico = iconRegister.registerIcon(tex);
        }

    }

    protected void getCheckedSubItems(Item sameItem, CreativeTabs creativeTab, List<ItemStack> itemStacks) {
        List<Map.Entry<Integer, EnumPartsWithVariant>> types = new ArrayList(this.getRegistered().entrySet());
        types.sort(REGISTERED_COMPARATOR);

        for(Map.Entry<Integer, EnumPartsWithVariant> part : types) {
            itemStacks.add(new ItemStack(this, 1, (Integer)part.getKey()));
        }

    }

    private String getName(ItemStack is) {
        Preconditions.checkNotNull(is);
        EnumPartsAvaritiaTerminals stackType = this.getTypeByStack(is);
        String typeName = stackType.name();
        return typeName;
    }

    @Nonnull
    public EnumPartsAvaritiaTerminals getTypeByStack(ItemStack is) {
        Preconditions.checkNotNull(is);
        EnumPartsWithVariant pt = (EnumPartsWithVariant)this.getRegistered().get(is.getItemDamage());
        return pt != null ? pt.part : EnumPartsAvaritiaTerminals.InvalidTypeAvaritia;
    }

    @Nullable
    public IPart createPartFromItemStack(ItemStack is) {
        EnumPartsAvaritiaTerminals type = this.getTypeByStack(is);
        Class<? extends IPart> part = type.getPart();
        if (part == null) {
            return null;
        } else {
            try {
                if (type.getConstructor() == null) {
                    type.setConstructor(part.getConstructor(ItemStack.class));
                }

                return (IPart)type.getConstructor().newInstance(is);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
                throw new IllegalStateException("Unable to construct IBusPart from IBusItem : " + part.getName() + " ; Possibly didn't have correct constructor( ItemStack )", e);
            }
        }
    }

    public int variantOf(int itemDamage) {
        EnumPartsWithVariant registeredEnumParts = (EnumPartsWithVariant)this.getRegistered().get(itemDamage);
        return registeredEnumParts != null ? registeredEnumParts.variant : 0;
    }

    @Nullable
    public String getUnlocalizedGroupName(Set<ItemStack> others, ItemStack is) {
        return null;
    }

    protected static final class EnumPartsWithVariant {
        private final EnumPartsAvaritiaTerminals part;
        private final int variant;
        @SideOnly(Side.CLIENT)
        private IIcon ico;

        private EnumPartsWithVariant(EnumPartsAvaritiaTerminals part, int variant) {
            assert part != null;

            assert variant >= 0;

            this.part = part;
            this.variant = variant;
        }

        public String toString() {
            return "EnumPartsWithVariant{part=" + this.part + ", variant=" + this.variant + ", ico=" + this.ico + '}';
        }
    }

    private static final class RegisteredComparator implements Comparator<Map.Entry<Integer, EnumPartsWithVariant>> {
        private RegisteredComparator() {
        }

        public int compare(Map.Entry<Integer, EnumPartsWithVariant> o1, Map.Entry<Integer, EnumPartsWithVariant> o2) {
            return ((EnumPartsWithVariant)o1.getValue()).part.name().compareTo(((EnumPartsWithVariant)o2.getValue()).part.name());
        }
    }
}

package foxiwhitee.hellmod.items.part;

import appeng.api.AEApi;
import appeng.api.config.Upgrades;
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
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.parts.EnumParts;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ItemParts extends AEBaseItem implements IPartItem, IItemGroup {
    private static final int INITIAL_REGISTERED_CAPACITY = (EnumParts.values()).length;

    private static final Comparator<Map.Entry<Integer, EnumPartsWithVariant>> REGISTERED_COMPARATOR = new RegisteredComparator();

    public static ItemParts instance;

    private final NameResolver nameResolver;

    private final Map<Integer, EnumPartsWithVariant> registered;

    public ItemParts(IPartHelper partHelper) {
        Preconditions.checkNotNull(partHelper);
        setUnlocalizedName("tmpart");
        setCreativeTab(HellCore.HELL_TAB);
        this.registered = new HashMap<>(INITIAL_REGISTERED_CAPACITY);
        this.nameResolver = new NameResolver((Class)getClass());
        setFeature(EnumSet.of(AEFeature.Core));
        partHelper.setItemBusRenderer(this);
        setHasSubtypes(true);
        instance = this;
        for (EnumParts part : EnumParts.values()) {
            if (part != EnumParts.InvalidType) {
                if (part.isCable())
                    for (AEColor color : AEColor.values())
                        createPart(part, color);
                createPart(part, 0);
            }
        }
    }

    @Nonnull
    public ItemStackSrc createPart(EnumParts mat) {
        Preconditions.checkNotNull(mat);
        return createPart(mat, 0);
    }

    @Nonnull
    public ItemStackSrc createPart(EnumParts mat, AEColor color) {
        Preconditions.checkNotNull(mat);
        Preconditions.checkNotNull(color);
        int varID = color.ordinal();
        return createPart(mat, varID);
    }

    @Nonnull
    private ItemStackSrc createPart(EnumParts mat, int varID) {
        assert mat != null;
        assert varID >= 0;
        for (EnumPartsWithVariant p : this.registered.values()) {
            if (p.part == mat && p.variant == varID) {
                boolean bool = true;
                for (AEFeature f : mat.getFeature())
                    bool = (bool && AEConfig.instance.isFeatureEnabled(f));
                for (IntegrationType integrationType : mat.getIntegrations())
                    bool &= IntegrationRegistry.INSTANCE.isEnabled(integrationType);
                int i = mat.getBaseDamage() + varID;
                return new ItemStackSrc((Item)this, i, ActivityState.from(bool));
            }
        }
        boolean enabled = true;
        for (AEFeature f : mat.getFeature())
            enabled = (enabled && AEConfig.instance.isFeatureEnabled(f));
        for (IntegrationType integrationType : mat.getIntegrations())
            enabled &= IntegrationRegistry.INSTANCE.isEnabled(integrationType);
        int partDamage = mat.getBaseDamage() + varID;
        ActivityState state = ActivityState.from(enabled);
        ItemStackSrc output = new ItemStackSrc((Item)this, partDamage, state);
        EnumPartsWithVariant pti = new EnumPartsWithVariant(mat, varID);
        processMetaOverlap(enabled, partDamage, mat, pti);
        return output;
    }

    private void processMetaOverlap(boolean enabled, int partDamage, EnumParts mat, EnumPartsWithVariant pti) {
        assert partDamage >= 0;
        assert mat != null;
        assert pti != null;
        EnumPartsWithVariant registeredEnumParts = this.registered.get(Integer.valueOf(partDamage));
        if (registeredEnumParts != null)
            throw new IllegalStateException("Meta Overlap detected with type " + mat + " and damage " + partDamage + ". Found " + registeredEnumParts + " there already.");
        if (enabled)
            this.registered.put(Integer.valueOf(partDamage), pti);
    }

    public int getDamageByType(EnumParts t) {
        Preconditions.checkNotNull(t);
        for (Map.Entry<Integer, EnumPartsWithVariant> pt : this.registered.entrySet()) {
            if ((pt.getValue()).part == t)
                return ((Integer)pt.getKey()).intValue();
        }
        return -1;
    }

    @SideOnly(Side.CLIENT)
    public int getSpriteNumber() {
        return 0;
    }

    public IIcon getIconFromDamage(int dmg) {
        EnumPartsWithVariant registeredType = this.registered.get(Integer.valueOf(dmg));
        if (registeredType != null)
            return registeredType.ico;
        String formattedRegistered = Arrays.toString(this.registered.keySet().toArray());
        throw new MissingDefinition("Tried to get the icon from a non-existent part with damage value " + dmg + ". There were registered: " + formattedRegistered + '.');
    }

    public boolean onItemUse(ItemStack is, EntityPlayer player, World w, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (getTypeByStack(is) == EnumParts.InvalidType)
            return false;
        return AEApi.instance().partHelper().placeBus(is, x, y, z, side, player, w);
    }

    public String getUnlocalizedName(ItemStack is) {
        return "item." + getName(is).toLowerCase();
    }

    public String getItemStackDisplayName(ItemStack is) {
        EnumParts pt = getTypeByStack(is);
        if (pt.isCable()) {
            AEColor[] variants = AEColor.values();
            int itemDamage = is.getItemDamage();
            EnumPartsWithVariant registeredEnumParts = this.registered.get(Integer.valueOf(itemDamage));
            if (registeredEnumParts != null)
                return super.getItemStackDisplayName(is) + " - " + variants[registeredEnumParts.variant].toString();
        }

        return super.getItemStackDisplayName(is);
    }

    public void registerIcons(IIconRegister iconRegister) {
        for (Map.Entry<Integer, EnumPartsWithVariant> part : this.registered.entrySet()) {
            String tex = HellCore.MODID + ":" + getName(new ItemStack((Item)this, 1, ((Integer)part.getKey()).intValue())).toLowerCase();
            (part.getValue()).ico = iconRegister.registerIcon(tex);
        }
    }

    protected void getCheckedSubItems(Item sameItem, CreativeTabs creativeTab, List<ItemStack> itemStacks) {
        List<Map.Entry<Integer, EnumPartsWithVariant>> types = new ArrayList<>(this.registered.entrySet());
        types.sort(REGISTERED_COMPARATOR);
        for (Map.Entry<Integer, EnumPartsWithVariant> part : types)
            itemStacks.add(new ItemStack((Item)this, 1, ((Integer)part.getKey()).intValue()));
    }

    private String getName(ItemStack is) {
        Preconditions.checkNotNull(is);
        EnumParts stackType = getTypeByStack(is);
        String typeName = stackType.name();
        return typeName;
    }

    @Nonnull
    public EnumParts getTypeByStack(ItemStack is) {
        Preconditions.checkNotNull(is);
        EnumPartsWithVariant pt = this.registered.get(Integer.valueOf(is.getItemDamage()));
        if (pt != null)
            return pt.part;
        return EnumParts.InvalidType;
    }

    @Nullable
    public IPart createPartFromItemStack(ItemStack is) {
        EnumParts type = getTypeByStack(is);
        Class<? extends IPart> part = type.getPart();
        if (part == null)
            return null;
        try {
            if (type.getConstructor() == null)
                type.setConstructor(part.getConstructor(new Class[] { ItemStack.class }));
            return type.getConstructor().newInstance(new Object[] { is });
        } catch (InstantiationException|IllegalAccessException|java.lang.reflect.InvocationTargetException|NoSuchMethodException e) {
            throw new IllegalStateException("Unable to construct IBusPart from IBusItem : " + part.getName() + " ; Possibly didn't have correct constructor( ItemStack )", e);
        }
    }

    public int variantOf(int itemDamage) {
        EnumPartsWithVariant registeredEnumParts = this.registered.get(Integer.valueOf(itemDamage));
        if (registeredEnumParts != null)
            return registeredEnumParts.variant;
        return 0;
    }

    @Nullable
    public String getUnlocalizedGroupName(Set<ItemStack> others, ItemStack is) {
        return null;
    }

    private static final class EnumPartsWithVariant {
        private final EnumParts part;

        private final int variant;

        @SideOnly(Side.CLIENT)
        private IIcon ico;

        private EnumPartsWithVariant(EnumParts part, int variant) {
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
        private RegisteredComparator() {}

        public int compare(Map.Entry<Integer, ItemParts.EnumPartsWithVariant> o1, Map.Entry<Integer, ItemParts.EnumPartsWithVariant> o2) {
            return (o1.getValue()).part.name().compareTo((o2.getValue()).part.name());
        }
    }
}

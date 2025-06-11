package foxiwhitee.hellmod.items.part;

import appeng.items.AEBaseItem;

import java.util.regex.Pattern;

public class NameResolver {
    private static final Pattern ITEM_MULTI_PART = Pattern.compile("AItemMultiParts", 16);
    private static final Pattern ITEM_MULTI_MATERIAL = Pattern.compile("AItemMultiMaterial", 16);
    private static final Pattern QUARTZ = Pattern.compile("Quartz", 16);
    private final Class<? extends AEBaseItem> withOriginalName;

    public NameResolver(Class<? extends AEBaseItem> withOriginalName) {
        this.withOriginalName = withOriginalName;
    }

    public String getName(String subName) {
        String name = this.withOriginalName.getSimpleName();
        if (name.startsWith("ItemMultiPart")) {
            name = ITEM_MULTI_PART.matcher(name).replaceAll("ItemPart");
        } else if (name.startsWith("ItemMultiMaterial")) {
            name = ITEM_MULTI_MATERIAL.matcher(name).replaceAll("ItemMaterial");
        }

        if (subName != null) {
            if (subName.startsWith("P2PTunnel")) {
                return "ItemPart.P2PTunnel";
            }

            if (subName.equals("CertusQuartzTools")) {
                return QUARTZ.matcher(name).replaceAll("CertusQuartz");
            }

            if (subName.equals("NetherQuartzTools")) {
                return QUARTZ.matcher(name).replaceAll("NetherQuartz");
            }

            name = name + '.' + subName;
        }

        return name;
    }
}
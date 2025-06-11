package foxiwhitee.hellmod;


import appeng.api.AEApi;
import cpw.mods.fml.common.registry.GameRegistry;
import foxiwhitee.hellmod.items.*;
import foxiwhitee.hellmod.items.part.ItemParts;
import foxiwhitee.hellmod.utils.helpers.RegisterUtils;
import net.minecraft.item.Item;

import java.util.HashMap;
import java.util.Map;


public class ModItems {

    public static ItemParts ITEM_PARTS = new ItemParts(AEApi.instance().partHelper());

    public static ItemCustomStorageCells storageCells = new ItemCustomStorageCells();

    public static Item wireless_staff = new ItemWirelessStaff("wireless_staff");

    public static Item fluidDrop = new ItemFluidDrop();

    public static Item emptyFluidCell = new ItemEmptyFluidCell("emptyFluidCell");

    public static Item fluidStorageComponent = new ItemFluidStorageComponent("fluidComponent");

    public static Item fluidCells = new ItemFluidStorageCells();

    public static final Map<String, DefaultItem> ITEMS = new HashMap<>();

    static {
        addItems("storageComponents", "storageComponent", "256K",
                "1M", "4M", "16M", "65M", "262M",
                "1G", "4G", "16G", "67G", "268G",
                "1T", "4T", "17T", "68T", "274T",
                "1P", "4P", "17P", "70P", "281P",
                "1E", "4E", "9E");
        addItems("magic", "", "asgardianIngot", "helheimIngot", "valhallaIngot", "midgardIngot");

        addItems("storageCells", "", "advancedEmptyCell", "hybridEmptyCell", "ultimateEmptyCell", "quantumEmptyCell", "singularEmptyCell");
    }

    public static void addItems(String category, String prefix, String... itemNames) {
        String fullName = "";
        if (category != "0") {
            category += "/";
        } else {
            category = "";
        }
        for (String itemName : itemNames) {
            fullName = prefix + itemName;
            ITEMS.put(fullName, new DefaultItem(fullName, category + fullName));
        }
    }

    public static void registerItems() {
        RegisterUtils.registerItem(fluidDrop);
        RegisterUtils.registerItem(storageCells);
        RegisterUtils.registerItem(wireless_staff);
        RegisterUtils.registerItem(emptyFluidCell);
        RegisterUtils.registerItem(fluidStorageComponent);
        RegisterUtils.registerItem(fluidCells);
        GameRegistry.registerItem(ITEM_PARTS, "hell-part");
    }

}

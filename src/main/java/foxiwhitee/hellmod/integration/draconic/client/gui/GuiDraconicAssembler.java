package foxiwhitee.hellmod.integration.draconic.client.gui;

import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.client.gui.HellBaseGui;
import foxiwhitee.hellmod.client.gui.buttons.NoTextureButton;
import foxiwhitee.hellmod.integration.draconic.container.ContainerDraconicAssembler;
import foxiwhitee.hellmod.integration.draconic.container.slots.DraconicAssemblerSlot;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.inventory.Slot;

public class GuiDraconicAssembler extends HellBaseGui {
    private int side;
    private ContainerDraconicAssembler containerDraconicAssembler;

    public GuiDraconicAssembler(ContainerDraconicAssembler container) {
        super(container, 520, 490);
        this.containerDraconicAssembler = container;
    }

    @Override
    protected String getBackground() {
        return "gui/gui_dragon_assembler.png";
    }

    protected String getBackgroundUpgrade() {
        return "gui/gui_dragon_assembler_upgrade.png";
    }

    public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
        int h = (this.width - this.xSize / 2) / 2;
        int k = (this.height - this.ySize / 2) / 2;

        if (side != 1) {
            bindTexture(HellCore.MODID, this.getBackground());
            UtilGui.drawTexture(h, k , 0, 1, (double) xSize / 2, (double) ySize / 2, xSize, ySize, getSizeTexture()[0], getSizeTexture()[1]);
            ((NoTextureButton)(this.buttonList.get(0))).xPosition = this.guiLeft + 362;
            drawIfInMouse(mouseX, mouseY,  362, 160, 10, 89, LocalizationUtils.localize("tooltip.draconic-assembler.button"));
        } else {
            bindTexture(HellCore.MODID, this.getBackgroundUpgrade());
            UtilGui.drawTexture(h, k, 0, 1, (double) (xSize + 50) / 2, (double) ySize / 2, xSize + 50, ySize, getSizeTexture()[0], getSizeTexture()[1]);
            ((NoTextureButton)(this.buttonList.get(0))).xPosition = this.guiLeft + 362 + 27;
            drawIfInMouse(mouseX, mouseY,  362 + 27, 160, 10, 89, LocalizationUtils.localize("tooltip.draconic-assembler.button"));
        }
        bindTexture(HellCore.MODID, this.getBackground());
        Slot[] slots = this.containerDraconicAssembler.getInventorySlots();
        for (Slot slot : slots) drawSlot((DraconicAssemblerSlot) slot);

        if (this.containerDraconicAssembler.getTile().getCraftingTicks() > 0) {
            double l = this.containerDraconicAssembler.getTile().gaugeProcessScaled((double) 14 / 2);
            UtilGui.drawTexture(h + 114, k + 84, 151, 753, 31, (int)(l + 1.0D), 62, (int)(l + 1.0D) * 2, getSizeTexture()[0], getSizeTexture()[1]);
        }

        if (this.containerDraconicAssembler.getTile().getEnergyStored() > 0) {
            double l = this.containerDraconicAssembler.getTile().gaugeEnergyScaled(75);
            String storageString = LocalizationUtils.localize("tooltip.draconicAssembler.storage") + " ";
            String tooltipStorage = storageString + this.containerDraconicAssembler.getTile().getEnergyStored() + "/" + this.containerDraconicAssembler.getTile().getMaxEnergyStored() +" RF";
            UtilGui.drawTexture(h + 92, k + 127, 0, 750, (int)(l + 1.0D), 14, (int)(l + 1.0D) * 2, 14 * 2, getSizeTexture()[0], getSizeTexture()[1]);
            drawIfInMouse(mouseX, mouseY,  92+122, 127+122, 84, 10, tooltipStorage);
        }
    }

    public void drawSlot(DraconicAssemblerSlot slot) {
        int[] levels = this.containerDraconicAssembler.getTile().getLevels();
        int slotIndex = slot.getSlotIndex();
        int level = levels[slotIndex] - 1;
        if (level >= 0) {
            int startX = 217 + 51 * level;
            UtilGui.drawTexture(this.guiLeft + slot.xDisplayPosition - 4, this.guiTop + slot.yDisplayPosition - 4, startX, 720, 24, 24, 48, 48, getSizeTexture()[0], getSizeTexture()[1], this, HellCore.MODID, this.getBackground());
        }
    }

    @Override
    protected int[] getSizeTexture() {
        return new int[]{768, 768};
    }

    public void initGui() {
        super.initGui();
        this.buttonList.add(new NoTextureButton(1, this.guiLeft + 362, this.guiTop + 160, 10, 89));
    }

    protected void actionPerformed(GuiButton btn) {
        int id = (btn != null) ? btn.id : 0;
        int oldSide = this.side;
        this.side = (this.side == id) ? 0 : id;

        if (oldSide == 1) {
            Slot[] slots = this.containerDraconicAssembler.getUpgradesSlots();
            for (int i = 0; i < slots.length; i++) {
                Slot slot = slots[i];
                slot.xDisplayPosition = -9000;
            }
        }
        if (oldSide == 0) {
            Slot[] slots = this.containerDraconicAssembler.getUpgradesSlots();
            for (int i = 0; i < slots.length; i++) {
                Slot slot = slots[i];
                slot.xDisplayPosition = 368;
            }
        }

    }
}

package foxiwhitee.hellmod.client.gui;

import appeng.client.gui.AEBaseGui;
import appeng.client.gui.widgets.GuiTabButton;
import appeng.core.localization.GuiText;
import appeng.core.sync.AppEngPacket;
import appeng.core.sync.GuiBridge;
import appeng.core.sync.network.NetworkHandler;
import appeng.core.sync.packets.PacketSwitchGuis;
import foxiwhitee.hellmod.HellCore;
import foxiwhitee.hellmod.container.ContainerAdvancedDrive;
import foxiwhitee.hellmod.tile.TileAdvancedDrive;
import foxiwhitee.hellmod.utils.helpers.UtilGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

public class GuiAdvancedDrive extends AEBaseGui {
  private GuiTabButton priority;

  public GuiAdvancedDrive(InventoryPlayer inventoryPlayer, TileAdvancedDrive te) {
    super((Container)new ContainerAdvancedDrive(inventoryPlayer, te));
    this.xSize = 220;
    this.ySize = 238;
  }
  
  protected void actionPerformed(GuiButton par1GuiButton) {
    super.actionPerformed(par1GuiButton);
    if (par1GuiButton == this.priority)
      NetworkHandler.instance.sendToServer((AppEngPacket)new PacketSwitchGuis(GuiBridge.GUI_PRIORITY)); 
  }
  
  public void initGui() {
    super.initGui();
    this.buttonList.add(this.priority = new GuiTabButton(this.guiLeft + 198, this.guiTop, 66, GuiText.Priority.getLocal(), itemRender) {
      @Override
      public void drawButton(Minecraft minecraft, int x, int y) {

      }
    });
  }
  
  public void drawFG(int offsetX, int offsetY, int mouseX, int mouseY) {}
  
  public void drawBG(int offsetX, int offsetY, int mouseX, int mouseY) {
    bindTexture(HellCore.MODID, "gui/gui_advanced_driver.png");
    UtilGui.drawTexture(offsetX, offsetY, 0, 0, this.xSize, this.ySize, this.xSize, this.ySize);
  }
}
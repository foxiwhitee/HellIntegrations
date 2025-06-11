package foxiwhitee.hellmod.commands;

import foxiwhitee.hellmod.utils.localization.LocalizationUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;

public class CommandReloadLocalization extends CommandBase {
    @Override
    public String getCommandName() {
        return "reloadLocalization";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/reloadLocalization";
    }

    @Override
    public void processCommand(ICommandSender iCommandSender, String[] strings) {
        LocalizationUtils.findUnlocalizedNames();
        iCommandSender.addChatMessage(new ChatComponentText("Localization reloaded successfully."));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}

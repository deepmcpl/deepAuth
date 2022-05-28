package pl.animekkk.anauth.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.animekkk.anauth.user.helper.ChatHelper;

public class LoginTypeCommand extends Command {

    public LoginTypeCommand() {
        super("logintype");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            ChatHelper.sendMessage(sender, "&7Ta komenda jest tylko dla graczy.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;

    }
}

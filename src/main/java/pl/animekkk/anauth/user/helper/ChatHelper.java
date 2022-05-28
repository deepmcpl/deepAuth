package pl.animekkk.anauth.user.helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatHelper {

    public static String format(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static boolean broadcastMessage(String message) {
        ProxyServer.getInstance().broadcast(createFormattedText(message));
        return true;
    }


    public static boolean sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(createFormattedText(message));
        return true;
    }

    public static boolean sendMessage(CommandSender sender, String message) {
        sender.sendMessage(createFormattedText(message));
        return true;
    }

    public static TextComponent createFormattedText(String message) {
        return new TextComponent(format(message));
    }

}

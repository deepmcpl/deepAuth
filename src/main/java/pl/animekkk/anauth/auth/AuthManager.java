package pl.animekkk.anauth.auth;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.animekkk.anauth.AuthPlugin;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.helper.ChatHelper;
import pl.animekkk.anauth.user.helper.TFAHelper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

public class AuthManager {

    private final AuthPlugin plugin;

    public AuthManager(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public void generate2FA(AuthUser authUser, ProxiedPlayer player) {
        ProxyServer.getInstance().getScheduler().schedule(plugin, () -> {
            if(!player.isConnected()) return;

            String secret = TFAHelper.generateSecret();
            authUser.setPassword(secret.getBytes(StandardCharsets.UTF_8));

            String qrData = TFAHelper.generateQRData(secret, "animekkk");

            final TextComponent message = new TextComponent();
            message.addExtra(ChatHelper.createFormattedText("&7Link został ukryty. "));
            final TextComponent qrCode = new TextComponent("Kliknij tutaj");
            qrCode.setColor(ChatColor.DARK_AQUA);
            qrCode.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://auth.vopp.top?data=" + qrData));
            message.addExtra(qrCode);
            message.addExtra(ChatHelper.createFormattedText("&7, aby zobaczyć &3kod QR&7."));
            player.sendMessage(message);

            ChatHelper.sendMessage(player, "&7Teraz wpisz &3/register <kod> &7i przepisz kod z aplikacji.");
        }, 3, TimeUnit.SECONDS);
    }

}

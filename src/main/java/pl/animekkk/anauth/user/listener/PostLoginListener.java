package pl.animekkk.anauth.user.listener;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.animekkk.anauth.auth.AccountType;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.auth.LoginType;
import pl.animekkk.anauth.user.helper.ChatHelper;

import java.util.ArrayList;
import java.util.List;

public class PostLoginListener implements Listener {

    private final AuthUserManager authUserManager;

    public PostLoginListener(AuthUserManager authUserManager) {
        this.authUserManager = authUserManager;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer player = event.getPlayer();
        ChatHelper.clearChat(player);
        AuthUser authUser = this.authUserManager.getUser(player.getName());
        if(authUser == null) {
            player.disconnect(ChatHelper.createFormattedText("&7Coś poszło nie tak.\nZgłoś tą sytuację do administracji.\n&c(Error 002)"));
            return;
        }
        if(authUser.getUuid() == null) authUser.setUuid(player.getUniqueId());
        if(!authUser.isRegisterComplete()) authUser.setLoginType(null);
        if(authUser.getAuthState() == AuthState.LOGGED) authUser.setAuthState(AuthState.TO_LOGIN);
        if(authUser.getAuthState() == AuthState.TO_LOGIN) {
            ChatHelper.sendMessage(player, "&7Twoje metoda logowania to: &3" + authUser.getLoginType().getFullName() + "&7.");
            if(authUser.getAccountType() == AccountType.PREMIUM
                    && authUser.getLoginType() == LoginType.PREMIUM) {
                authUser.setAuthState(AuthState.LOGGED);
                //TODO Move to play server
                ChatHelper.sendMessage(player, "&cDEBUG: Move to play server");
            } else if(authUser.getLoginType() == LoginType.PASSWORD) {
                ChatHelper.sendMessage(player, "&7Wpisz &3/login <hasło>&7, aby się zalogować.");
            } else if(authUser.getLoginType() == LoginType.TFA) {
                ChatHelper.sendMessage(player, "&7Wpisz &3/login <kod>&7, aby się zalogować.");
            } else {
                player.disconnect(ChatHelper.createFormattedText("&7Coś poszło nie tak.\nZgłoś tą sytuację do administracji.\n&c(Error 003)"));
            }
        } else if(authUser.getAuthState() == AuthState.TO_REGISTER) {
            ChatHelper.sendMessage(player, "&7Wybierz metodę logowania:");

            final TextComponent separator = new TextComponent(" | ");
            separator.setColor(ChatColor.GRAY);
            separator.setBold(true);
            List<TextComponent> textComponents = new ArrayList<>();
            for(LoginType loginType : LoginType.values()) {
                if(loginType.isOnlyPremium() && authUser.getAccountType() == AccountType.NON_PREMIUM) continue;
                TextComponent loginTypeComponent = new TextComponent(loginType.getFullName());
                loginTypeComponent.setColor(ChatColor.DARK_AQUA);
                loginTypeComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/logintype " + loginType.getCode()));
                textComponents.add(loginTypeComponent);
                textComponents.add(separator);
            }
            textComponents.remove(textComponents.size() - 1); // Removing last separator
            final TextComponent finalMessage = new TextComponent();
            textComponents.forEach(finalMessage::addExtra);
            player.sendMessage(finalMessage);
            ChatHelper.sendMessage(player, "\n&7Kliknij na daną metodę logowania, aby ją wybrać lub użyj komendy &3/logintype&7.");
        } else {
            player.disconnect(ChatHelper.createFormattedText("&7Coś poszło nie tak.\nZgłoś tą sytuację do administracji.\n&c(Error 001)"));
        }
    }

}

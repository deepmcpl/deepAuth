package pl.animekkk.anauth.auth.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.animekkk.anauth.auth.AuthConfig;
import pl.animekkk.anauth.auth.AuthManager;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.auth.LoginType;
import pl.animekkk.anauth.auth.event.LoginTypeChangeEvent;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.helper.ChatHelper;

public class LoginTypeChangeListener implements Listener {

    private final AuthManager authManager;
    private final AuthConfig authConfig;

    public LoginTypeChangeListener(AuthManager authManager, AuthConfig authConfig) {
        this.authManager = authManager;
        this.authConfig = authConfig;
    }

    @EventHandler
    public void onLoginTypeChange(LoginTypeChangeEvent event) {
        AuthUser authUser = event.getAuthUser();
        ProxiedPlayer player = event.getPlayer();
        LoginType loginType = event.getNewLoginType();
        ChatHelper.sendMessage(player, "&7Twoja nowa metoda logowania to &3" + loginType.getFullName() + "&7.");
        if(loginType == LoginType.PREMIUM) {
            authUser.setAuthState(AuthState.LOGGED);
            authUser.setRegisterComplete(true);
            player.connect(authConfig.getSuccessLoginServer());
            ChatHelper.clearChat(player);
        } else if(loginType == LoginType.PASSWORD) {
            ChatHelper.sendMessage(player, "&7Użyj komendy &3/register <hasło> <hasło>&7, aby dokończyć rejestrację.");
        } else if(loginType == LoginType.TFA) {
            ChatHelper.sendMessage(player, "&cUWAGA! &7Nie pokazuj &3kodu QR&7, do któego adres zaraz ci się wyświetli.");
            this.authManager.generate2FA(authUser, player);
        }
    }

}

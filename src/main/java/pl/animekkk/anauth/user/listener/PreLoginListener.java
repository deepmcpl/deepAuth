package pl.animekkk.anauth.user.listener;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.animekkk.anauth.auth.AccountType;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.helper.ChatHelper;

public class PreLoginListener implements Listener {

    private final AuthUserManager authUserManager;

    public PreLoginListener(AuthUserManager authUserManager) {
        this.authUserManager = authUserManager;
    }

    @EventHandler
    public void onPreLogin(PreLoginEvent event) {
        if(event.isCancelled()) return;
        PendingConnection connection = event.getConnection();
        if(ProxyServer.getInstance().getPlayer(connection.getUniqueId()) != null) {
            event.setCancelReason(ChatHelper.createFormattedText("&7Jesteś już połączony z serwerem."));
            event.setCancelled(true);
            return;
        }
        AuthUser authUser = this.authUserManager.getUser(connection.getName());
        if(authUser == null) {
            authUserManager.createUser(connection.getName());
            event.setCancelReason(ChatHelper.createFormattedText("&7Twoje konto jest właśnie &3weryfikowane&7.\n&7Spróbuj wejść ponownie za chwile."));
            event.setCancelled(true);
        } else connection.setOnlineMode(authUser.getAccountType() == AccountType.PREMIUM);

    }

}

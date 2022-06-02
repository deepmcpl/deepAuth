package pl.animekkk.anauth.user.listener;

import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;

public class PlayerDisconnectListener implements Listener {

    private final AuthUserManager authUserManager;

    public PlayerDisconnectListener(AuthUserManager authUserManager) {
        this.authUserManager = authUserManager;
    }

    @EventHandler
    public void onDisconnect(PlayerDisconnectEvent event) {
        AuthUser authUser = this.authUserManager.getUser(event.getPlayer().getName());
        if(authUser == null) return;
        authUser.setPlayer(null);
        authUser.setSessionTime(0);
    }

}

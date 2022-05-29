package pl.animekkk.anauth.user.listener;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;

public class ChatListener implements Listener {

    private final AuthUserManager authUserManager;

    public ChatListener(AuthUserManager authUserManager) {
        this.authUserManager = authUserManager;
    }

    @EventHandler
    public void onCommand(ChatEvent event) {
        if(event.isCancelled()) return;
        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        AuthUser authUser = authUserManager.getUser(player.getName());
        if(authUser == null) return;
        if(authUser.getAuthState() != AuthState.LOGGED
                && !(event.getMessage().startsWith("/login") || event.getMessage().startsWith("/logintype") || event.getMessage().startsWith("/register"))) {
            event.setCancelled(true);
        }
    }

}

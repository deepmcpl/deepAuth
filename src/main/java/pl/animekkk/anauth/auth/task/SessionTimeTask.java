package pl.animekkk.anauth.auth.task;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.helper.ChatHelper;

public class SessionTimeTask implements Runnable {

    private final AuthUserManager authUserManager;

    public SessionTimeTask(AuthUserManager authUserManager) {
        this.authUserManager = authUserManager;
    }

    private final static int LOGIN_TIME_SECONDS = 180;

    @Override
    public void run() {
        authUserManager.getUsers().stream().filter(authUser -> authUser.getAuthState() != AuthState.LOGGED && authUser.isOnline()).forEach(authUser -> {
            int sessionTime = authUser.addSessionTime();
            ProxiedPlayer player = authUser.getPlayer();
            if(player != null) {
                if(sessionTime > LOGIN_TIME_SECONDS) {
                    player.disconnect(ChatHelper.createFormattedText("&7Czas logowania minął."));
                    authUser.setSessionTime(0);
                    return;
                }
                ChatHelper.sendActionBar(player, "&7Zostało ci &3" + (LOGIN_TIME_SECONDS - authUser.getSessionTime()) + "s &7do zalogowania się.");
            }
        });
    }

}

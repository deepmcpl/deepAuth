package pl.animekkk.anauth;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import pl.animekkk.anauth.auth.AuthConfig;
import pl.animekkk.anauth.auth.AuthManager;
import pl.animekkk.anauth.auth.command.LoginCommand;
import pl.animekkk.anauth.auth.command.LoginTypeCommand;
import pl.animekkk.anauth.auth.command.RegisterCommand;
import pl.animekkk.anauth.auth.listener.LoginTypeChangeListener;
import pl.animekkk.anauth.auth.task.SessionTimeTask;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.listener.ChatListener;
import pl.animekkk.anauth.user.listener.PlayerDisconnectListener;
import pl.animekkk.anauth.user.listener.PreLoginListener;
import pl.animekkk.anauth.user.listener.PostLoginListener;
import redis.clients.jedis.JedisPooled;

import java.util.concurrent.TimeUnit;

public final class AuthPlugin extends Plugin {

    private AuthManager authManager;
    private AuthUserManager authUserManager;
    private AuthConfig authConfig;
    private final JedisPooled jedis = new JedisPooled();

    @Override
    public void onEnable() {
        this.authManager = new AuthManager(this);
        this.authUserManager = new AuthUserManager(this, jedis);
        this.authConfig = new AuthConfig(this);

        final PluginManager pluginManager = this.getProxy().getPluginManager();
        registerListeners(pluginManager);
        registerCommands(pluginManager);
        registerTasks();

        //this.authUserManager.loadUsers();
    }

    @Override
    public void onDisable() {
        this.authUserManager.saveUsers();
    }

    public void registerListeners(PluginManager pluginManager) {
        pluginManager.registerListener(this, new PostLoginListener(authUserManager, authConfig));
        pluginManager.registerListener(this, new PreLoginListener(authUserManager));
        pluginManager.registerListener(this, new LoginTypeChangeListener(authManager, authConfig));
        pluginManager.registerListener(this, new ChatListener(authUserManager));
        pluginManager.registerListener(this, new PlayerDisconnectListener(authUserManager));
    }

    public void registerCommands(PluginManager pluginManager) {
        pluginManager.registerCommand(this, new LoginCommand(authUserManager, authConfig));
        pluginManager.registerCommand(this, new RegisterCommand(authUserManager, authConfig));
        pluginManager.registerCommand(this, new LoginTypeCommand(authUserManager));
    }

    public void registerTasks() {
        ProxyServer.getInstance().getScheduler().schedule(this, new SessionTimeTask(authUserManager), 1, 1, TimeUnit.SECONDS);
    }

}

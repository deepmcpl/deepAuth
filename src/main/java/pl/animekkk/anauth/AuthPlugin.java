package pl.animekkk.anauth;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import pl.animekkk.anauth.auth.AuthManager;
import pl.animekkk.anauth.auth.command.LoginCommand;
import pl.animekkk.anauth.auth.command.LoginTypeCommand;
import pl.animekkk.anauth.auth.command.RegisterCommand;
import pl.animekkk.anauth.auth.listener.LoginTypeChangeListener;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.listener.PreLoginListener;
import pl.animekkk.anauth.user.listener.PostLoginListener;

public final class AuthPlugin extends Plugin {

    private AuthManager authManager;
    private AuthUserManager authUserManager;

    @Override
    public void onEnable() {
        this.authManager = new AuthManager(this);
        this.authUserManager = new AuthUserManager(this);

        final PluginManager pluginManager = this.getProxy().getPluginManager();
        registerListeners(pluginManager);
        registerCommands(pluginManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerListeners(PluginManager pluginManager) {
        pluginManager.registerListener(this, new PostLoginListener(authUserManager));
        pluginManager.registerListener(this, new PreLoginListener(authUserManager));
        pluginManager.registerListener(this, new LoginTypeChangeListener(authManager));
    }

    public void registerCommands(PluginManager pluginManager) {
        pluginManager.registerCommand(this, new LoginCommand(authUserManager));
        pluginManager.registerCommand(this, new RegisterCommand(authUserManager));
        pluginManager.registerCommand(this, new LoginTypeCommand(authUserManager));

    }

}

package pl.animekkk.anauth.auth;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import pl.animekkk.anauth.AuthPlugin;

import java.io.File;
import java.io.IOException;

public class AuthConfig {

    @Getter
    private final ServerInfo successLoginServer;

    public AuthConfig(AuthPlugin plugin) {
        Configuration configuration;
        try {
            if(!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
            File configFile = new File(plugin.getDataFolder(), "config.yml");
            if(!configFile.exists()) configFile.createNewFile();

            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            ProxyServer.getInstance().stop("Cannot load config file.");
            throw new RuntimeException(e);
        }
        this.successLoginServer = plugin.getProxy().getServerInfo(configuration.getString("successLoginServer"));
    }

}

package pl.animekkk.anauth.user;

import net.md_5.bungee.api.ProxyServer;
import pl.animekkk.anauth.AuthPlugin;
import pl.animekkk.anauth.auth.AccountType;
import pl.animekkk.anauth.auth.AuthInfo;
import pl.animekkk.anauth.user.helper.PremiumHelper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AuthUserManager {

    private final AuthPlugin plugin;
    private final HashMap<String, AuthUser> authUsers = new HashMap<>();
    private final Set<String> checkingNames = new HashSet<>();

    public AuthUserManager(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public AuthUser getUser(String name) {
        return this.authUsers.get(name);
    }

    public void addUser(AuthUser authUser) {
        this.authUsers.put(authUser.getName(), authUser);
    }

    public void createUser(String name) {
        if(this.checkingNames.contains(name)) return;
        this.checkingNames.add(name);
        ProxyServer.getInstance().getScheduler().runAsync(this.plugin, () -> {
            try {
                final URL url = new URL(PremiumHelper.HAS_PAID_URL.replace("%name%", name));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(7500);
                connection.connect();

                AuthUser authUser = new AuthUser(name);
                if(connection.getResponseCode() == 200) {
                    authUser.setAccountType(AccountType.PREMIUM);
                } else {
                    authUser.setAccountType(AccountType.NON_PREMIUM);
                }
                authUser.setAuthInfo(new AuthInfo(System.currentTimeMillis(), System.currentTimeMillis()));
                this.addUser(authUser);
                this.checkingNames.remove(name);

                connection.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}

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
    private final HashMap<UUID, AuthUser> authUsers = new HashMap<>();
    private final Set<UUID> checkingUUIDs = new HashSet<>();

    public AuthUserManager(AuthPlugin plugin) {
        this.plugin = plugin;
    }

    public AuthUser getUser(UUID uuid) {
        return this.authUsers.get(uuid);
    }

    public void addUser(AuthUser authUser) {
        this.authUsers.put(authUser.getUuid(), authUser);
    }

    public void createUser(UUID uuid) {
        if(this.checkingUUIDs.contains(uuid)) return;
        this.checkingUUIDs.add(uuid);
        ProxyServer.getInstance().getScheduler().runAsync(this.plugin, () -> {
            try {
                final URL url = new URL(PremiumHelper.HAS_PAID_URL.replace("%uuid%", uuid.toString()));
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(7500);
                connection.connect();

                AuthUser authUser = new AuthUser(uuid);
                if(connection.getResponseCode() == 200) {
                    authUser.setAccountType(AccountType.PREMIUM);
                } else {
                    authUser.setAccountType(AccountType.NON_PREMIUM);
                }
                authUser.setAuthInfo(new AuthInfo(System.currentTimeMillis(), System.currentTimeMillis()));
                this.addUser(authUser);
                this.checkingUUIDs.remove(uuid);

                connection.disconnect();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}

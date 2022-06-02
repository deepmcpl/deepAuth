package pl.animekkk.anauth.user;

import lombok.Data;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.animekkk.anauth.auth.AccountType;
import pl.animekkk.anauth.auth.AuthInfo;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.auth.LoginType;

import java.io.Serializable;
import java.util.UUID;

@Data
public class AuthUser implements Serializable {

    private final String name;
    private UUID uuid;

    private AccountType accountType;
    private AuthState authState = AuthState.TO_REGISTER;
    private LoginType loginType;
    private boolean isRegisterComplete = false;

    private byte[] password;

    private AuthInfo authInfo;

    private transient ProxiedPlayer player;
    private transient int sessionTime = 0;

    public int addSessionTime(int sessionTime) {
        this.sessionTime += sessionTime;
        return this.sessionTime;
    }

    public int addSessionTime() {
        this.addSessionTime(1);
        return this.sessionTime;
    }

    public boolean isOnline() {
        return this.player != null;
    }

}

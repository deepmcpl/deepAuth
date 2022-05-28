package pl.animekkk.anauth.user;

import lombok.Data;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import pl.animekkk.anauth.auth.AccountType;
import pl.animekkk.anauth.auth.AuthInfo;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.auth.LoginType;

import java.util.UUID;

@Data
public class AuthUser {

    private final UUID uuid;

    private AccountType accountType;
    private AuthState authState = AuthState.TO_REGISTER;
    private LoginType loginType;

    private byte[] password;

    private AuthInfo authInfo;

    private ProxiedPlayer player;


}

package pl.animekkk.anauth.auth.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;
import pl.animekkk.anauth.auth.LoginType;
import pl.animekkk.anauth.user.AuthUser;

@AllArgsConstructor
public class LoginTypeChangeEvent extends Event {

    @Getter
    private final AuthUser authUser;
    @Getter
    private final ProxiedPlayer player;
    @Getter
    private final LoginType oldLoginType;
    @Getter
    private final LoginType newLoginType;

}

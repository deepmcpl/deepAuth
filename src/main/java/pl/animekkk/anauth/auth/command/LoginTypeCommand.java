package pl.animekkk.anauth.auth.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import pl.animekkk.anauth.auth.AccountType;
import pl.animekkk.anauth.auth.LoginType;
import pl.animekkk.anauth.auth.event.LoginTypeChangeEvent;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.helper.ChatHelper;

import java.util.ArrayList;
import java.util.List;

public class LoginTypeCommand extends Command {

    private final AuthUserManager authUserManager;

    public LoginTypeCommand(AuthUserManager authUserManager) {
        super("logintype");
        this.authUserManager = authUserManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            ChatHelper.sendMessage(sender, "&7Ta komenda jest tylko dla graczy.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        AuthUser authUser = authUserManager.getUser(player.getName());
        if(authUser == null) {
            player.disconnect(ChatHelper.createFormattedText("&7Coś poszło nie tak.\nZgłoś tą sytuację do administracji.\n&c(Error 004)"));
            return;
        }
        if (args.length != 1) {
            ChatHelper.sendMessage(player,
                    "&7Niepoprawne użycie komendy. &3(" + this.getUsage(authUser.getAccountType() == AccountType.PREMIUM) + ")");
            return;
        }
        if(authUser.getLoginType() != null) {
            //TOPO Allow user to change login type
            ChatHelper.sendMessage(player, "&7Nie możesz użyć tej komendy.");
            return;
        }
        LoginType loginType = LoginType.getByCodeName(args[0]);
        if(loginType == null) {
            ChatHelper.sendMessage(player,
                    "&7Niepoprawne użycie komendy. &3(" + this.getUsage(authUser.getAccountType() == AccountType.PREMIUM) + ")");
            return;
        }
        if(loginType.isOnlyPremium() && authUser.getAccountType() == AccountType.NON_PREMIUM) {
            ChatHelper.sendMessage(player, "&7Nie możesz wybrać tej metody logowania.");
            return;
        }
        ProxyServer.getInstance().getPluginManager().callEvent(new LoginTypeChangeEvent(authUser, player, authUser.getLoginType(), loginType));
        authUser.setLoginType(loginType);
    }

    private String getUsage(boolean isPremium) {
        final List<String> loginTypes = new ArrayList<>();
        for(LoginType loginType : LoginType.values()) {
            if(loginType.isOnlyPremium() && !isPremium) continue;
            loginTypes.add(loginType.getCode());
        }
        return "/" + this.getName() + " <" + String.join("/", loginTypes) + ">";
    }
}

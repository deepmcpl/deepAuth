package pl.animekkk.anauth.auth.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.codec.digest.DigestUtils;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.auth.LoginType;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.helper.ChatHelper;
import pl.animekkk.anauth.user.helper.TFAHelper;

import java.util.Arrays;

public class LoginCommand extends Command {

    private final AuthUserManager authUserManager;

    public LoginCommand(AuthUserManager authUserManager) {
        super("login");
        this.authUserManager = authUserManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            ChatHelper.sendMessage(sender, "&7Ta komenda jest tylko dla graczy.");
            return;
        }
        ProxiedPlayer player = (ProxiedPlayer) sender;
        AuthUser authUser = authUserManager.getUser(player.getName());
        if(authUser == null) {
            player.disconnect(ChatHelper.createFormattedText("&7Coś poszło nie tak.\nZgłoś tą sytuację do administracji.\n&c(Error 008)"));
            return;
        }
        LoginType loginType = authUser.getLoginType();
        final String commandUsage = getUsage(loginType);
        if(loginType == LoginType.TFA) {
            if(args.length != 1) {
                ChatHelper.sendMessage(player, "&7Niepoprawne użycie komendy. &3(%usage%)".replace("%usage%", commandUsage));
                return;
            }
            if(!TFAHelper.isCodeGood(args[0], new String(authUser.getPassword()))) {
                ChatHelper.sendMessage(player, "&7Ten kod nie jest poprawny.");
                return;
            }
            ChatHelper.sendMessage(player, "&7Poprawnie zalogowałeś się na swoje konto!");
            authUser.setAuthState(AuthState.LOGGED);
            //TODO Move to play server
            ChatHelper.sendMessage(player, "&cDEBUG: Move to play server");
        } else if(loginType == LoginType.PASSWORD) {
            if(args.length != 1) {
                ChatHelper.sendMessage(player, "&7Niepoprawne użycie komendy. &3(%usage%)".replace("%usage%", commandUsage));
                return;
            }
            String password = args[0];
            if(!Arrays.equals(DigestUtils.sha256(password), authUser.getPassword())) {
                ChatHelper.sendMessage(player, "&7Podane hasło nie jest poprawne!");
                return;
            }
            ChatHelper.sendMessage(player, "&7Poprawnie zalogowałeś się na swoje konto!");
            authUser.setAuthState(AuthState.LOGGED);
            //TODO Move to play server
            ChatHelper.sendMessage(player, "&cDEBUG: Move to play server");
        } else {
            ChatHelper.sendMessage(player, "&7Nie możesz użyć tej komendy.");
        }
    }

    private String getUsage(LoginType loginType) {
        if(loginType == LoginType.PASSWORD) return "/login <hasło>";
        else if(loginType == LoginType.TFA) return "/login <kod>";
        else return "&c- błąd, zgłos to do administracji -";
    }
}

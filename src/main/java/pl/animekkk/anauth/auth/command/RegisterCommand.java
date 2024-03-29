package pl.animekkk.anauth.auth.command;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import org.apache.commons.codec.digest.DigestUtils;
import pl.animekkk.anauth.auth.AuthConfig;
import pl.animekkk.anauth.auth.AuthState;
import pl.animekkk.anauth.auth.LoginType;
import pl.animekkk.anauth.user.AuthUser;
import pl.animekkk.anauth.user.AuthUserManager;
import pl.animekkk.anauth.user.helper.ChatHelper;
import pl.animekkk.anauth.user.helper.TFAHelper;

public class RegisterCommand extends Command {

    private final AuthUserManager authUserManager;
    private final AuthConfig authConfig;

    public RegisterCommand(AuthUserManager authUserManager, AuthConfig authConfig) {
        super("register");
        this.authUserManager = authUserManager;
        this.authConfig = authConfig;
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
            player.disconnect(ChatHelper.createFormattedText("&7Coś poszło nie tak.\nZgłoś tą sytuację do administracji.\n&c(Error 007)"));
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
            ChatHelper.sendMessage(player, "&7Poprawnie założyłeś swoje konto!");
            authUser.setAuthState(AuthState.LOGGED);
            authUser.setRegisterComplete(true);
            player.connect(authConfig.getSuccessLoginServer());
            ChatHelper.clearChat(player);
        } else if(loginType == LoginType.PASSWORD) {
            if(args.length != 2) {
                ChatHelper.sendMessage(player, "&7Niepoprawne użycie komendy. &3(%usage%)".replace("%usage%", commandUsage));
                return;
            }
            String password = args[0];
            if(!password.equals(args[1])) {
                ChatHelper.sendMessage(player, "&7Podane hasła nie są identyczne.");
                return;
            }
            ChatHelper.sendMessage(player, "&7Poprawnie założyłeś swoje konto!");
            authUser.setPassword(DigestUtils.sha256(password));
            authUser.setAuthState(AuthState.LOGGED);
            authUser.setRegisterComplete(true);
            player.connect(authConfig.getSuccessLoginServer());
            ChatHelper.clearChat(player);
        } else {
            ChatHelper.sendMessage(player, "&7Nie możesz użyć tej komendy.");
        }
    }

    private String getUsage(LoginType loginType) {
        if(loginType == LoginType.PASSWORD) return "/register <hasło> <hasło>";
        else if(loginType == LoginType.TFA) return "/register <kod>";
        else return "&c- błąd, zgłos to do administracji -";
    }
}

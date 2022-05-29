package pl.animekkk.anauth.user.helper;

import com.google.gson.JsonObject;
import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import dev.samstevens.totp.util.Utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TFAHelper {

    private final static SecretGenerator secretGenerator = new DefaultSecretGenerator(32);
    private final static TimeProvider timeProvider = new SystemTimeProvider();
    private final static CodeGenerator codeGenerator = new DefaultCodeGenerator();
    private final static CodeVerifier codeVerifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

    public static String generateSecret() {
        return secretGenerator.generate();
    }

    public static String generateQRData(String secret, String name) {
        JsonObject object = new JsonObject();
        object.addProperty("name", name);
        object.addProperty("secret", secret);
        return Base64.getEncoder().encodeToString(object.toString().getBytes(StandardCharsets.UTF_8));
    }

    public static boolean isCodeGood(String code, String secret) {

        return codeVerifier.isValidCode(secret, code);
    }

}

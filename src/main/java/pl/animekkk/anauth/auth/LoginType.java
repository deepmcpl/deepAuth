package pl.animekkk.anauth.auth;

import lombok.Getter;

public enum LoginType {

    PASSWORD("HASŁO", "haslo", false),
    PREMIUM("LOGOWANIE PREMIUM", "premium", true),
    TFA("2FA", "2fa", false);

    @Getter
    private final String fullName;
    @Getter
    private final String code;
    @Getter
    private final boolean onlyPremium;

    LoginType(String fullName, String code, boolean onlyPremium) {
        this.fullName = fullName;
        this.code = code;
        this.onlyPremium = onlyPremium;
    }

}

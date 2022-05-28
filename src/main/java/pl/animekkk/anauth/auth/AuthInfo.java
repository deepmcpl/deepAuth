package pl.animekkk.anauth.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthInfo {

    private long createTime;
    private long lastJoinTime;

}

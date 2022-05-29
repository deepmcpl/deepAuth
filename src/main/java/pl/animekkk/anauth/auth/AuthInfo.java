package pl.animekkk.anauth.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class AuthInfo implements Serializable {

    private long createTime;
    private long lastJoinTime;

}

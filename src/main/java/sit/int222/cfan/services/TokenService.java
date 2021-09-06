package sit.int222.cfan.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sit.int222.cfan.entities.User;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Service
public class TokenService {
    @Value("${cfan.token.secret}")
    private String secret;

    @Value("${cfan.token.issuer}")
    private String issuer;

    private Algorithm algorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String tokenize(User user) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Bangkok"));
        calendar.add(Calendar.MINUTE, 60 * 24);
        Date expiresAt = calendar.getTime();

        return JWT.create()
                .withIssuer(issuer)
                .withClaim("principal", user.getUserid())
                .withClaim("role", user.getStatus().toString())
                .withExpiresAt(expiresAt)
                .sign(algorithm());
    }

    public DecodedJWT verify(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm())
                    .withIssuer(issuer)
                    .build();

            return verifier.verify(token);

        } catch (Exception e) {
            return null;
        }
    }
}

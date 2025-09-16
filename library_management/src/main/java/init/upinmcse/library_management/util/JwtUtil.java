package init.upinmcse.library_management.util;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import init.upinmcse.library_management.constant.TokenType;
import init.upinmcse.library_management.exception.InvalidTokenException;
import init.upinmcse.library_management.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String SIGNER_KEY;

    @Value("${jwt.accessExpiryMinutes}")
    private long ACCESS_EXPIRY_SECONDS;

    @Value("${jwt.refreshExpiryMinutes}")
    private long REFRESH_EXPIRY_SECONDS;

    public String generateToken(User user, TokenType tokenType) {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        long expiryTimeInSeconds =
                tokenType == TokenType.ACCESS_TOKEN
                        ? ACCESS_EXPIRY_SECONDS
                        : REFRESH_EXPIRY_SECONDS;

        log.info("authorities : {}", user.getRoles());

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getEmail())
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(expiryTimeInSeconds, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(claimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    public String extractJwtId(String token, TokenType typeToken) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (signedJWT.getJWTClaimsSet() != null && signedJWT.getJWTClaimsSet().getJWTID() != null) {
            return signedJWT.getJWTClaimsSet().getJWTID();
        }
        return null;
    }

    public String extractUsername(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        if (signedJWT.getJWTClaimsSet() != null && signedJWT.getJWTClaimsSet().getSubject() != null) {
            return signedJWT.getJWTClaimsSet().getSubject();
        }

        return null;
    }

    public void validateToken(String token, boolean isRefresh) throws ParseException, JOSEException {
        if (token == null || token.trim().isEmpty()) {
            throw new InvalidTokenException("Invalid token");
        }

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT.getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESH_EXPIRY_SECONDS, ChronoUnit.HOURS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (expiryTime.before(new Date())) {
            throw new InvalidTokenException("Invalid token");
        }

        boolean verified = signedJWT.verify(verifier);
        if (!verified) {
            throw new InvalidTokenException("Invalid token");
        }

    }


    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getRoleName());
            });

        return stringJoiner.toString();
    }
}

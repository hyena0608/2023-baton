package touch.baton.infra.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.IncorrectClaimException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.MissingClaimException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.oauth.exception.OauthRequestException;

@RequiredArgsConstructor
@Component
public class JwtDecoder {

    private final JwtConfig jwtConfig;

    public Claims parseJwtToken(final String token) {
        try {
            final JwtParser jwtParser = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .requireIssuer(jwtConfig.getIssuer())
                    .build();

            return jwtParser.parseClaimsJws(token).getBody();
        } catch (SignatureException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_SIGNATURE_IS_WRONG);
        } catch (MalformedJwtException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_FORM_IS_WRONG);
        } catch (MissingClaimException | IncorrectClaimException e) {
            throw new OauthRequestException(ClientErrorCode.JWT_CLAIM_IS_WRONG);
        }
    }
}
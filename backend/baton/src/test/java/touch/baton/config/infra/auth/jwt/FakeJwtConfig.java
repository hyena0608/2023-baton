package touch.baton.config.infra.auth.jwt;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import touch.baton.infra.auth.jwt.JwtConfig;
import touch.baton.infra.auth.jwt.JwtDecoder;
import touch.baton.infra.auth.jwt.JwtEncoder;

@TestConfiguration
public abstract class FakeJwtConfig {

    @Bean
    JwtEncoder jwtEncoder() {
        return new JwtEncoder(fakeJwtConfig());
    }

    @Bean
    JwtDecoder jwtDecoder() {
        return new JwtDecoder(fakeJwtConfig());
    }

    private JwtConfig fakeJwtConfig() {
        return new JwtConfig("test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key", "test_issuer", 30);
    }

    @Bean
    JwtEncoder jwtExpireEncoder() {
        return new JwtEncoder(mockJwtExpireConfig());
    }

    private JwtConfig mockJwtExpireConfig() {
        return new JwtConfig("test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key_test_secret_key", "test_issuer", -1);
    }
}

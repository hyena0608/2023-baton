package touch.baton.domain.oauth.query.controller.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import touch.baton.domain.common.exception.ClientErrorCode;
import touch.baton.domain.member.command.Supporter;
import touch.baton.domain.member.command.vo.ReviewCount;
import touch.baton.domain.member.command.vo.SocialId;
import touch.baton.domain.oauth.command.exception.OauthRequestException;
import touch.baton.domain.oauth.command.repository.OauthSupporterCommandRepository;
import touch.baton.domain.technicaltag.command.SupporterTechnicalTags;
import touch.baton.infra.auth.jwt.JwtDecoder;

import java.util.Collections;

@Component
public class AuthSupporterPrincipalArgumentResolver extends UserPrincipalArgumentResolver {

    private final OauthSupporterCommandRepository oauthSupporterCommandRepository;

    public AuthSupporterPrincipalArgumentResolver(final JwtDecoder jwtDecoder, final OauthSupporterCommandRepository oauthSupporterCommandRepository) {
        super(jwtDecoder);
        this.oauthSupporterCommandRepository = oauthSupporterCommandRepository;
    }

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthSupporterPrincipal.class);
    }

    @Override
    protected Object getGuest() {
        return Supporter.builder()
                .reviewCount(new ReviewCount(0))
                .member(null)
                .supporterTechnicalTags(new SupporterTechnicalTags(Collections.emptyList()))
                .build();
    }

    @Override
    protected Object getUser(final String socialId) {
        return oauthSupporterCommandRepository.joinByMemberSocialId(new SocialId(socialId))
                .orElseThrow(() -> new OauthRequestException(ClientErrorCode.JWT_CLAIM_SOCIAL_ID_IS_WRONG));
    }

    @Override
    protected boolean isAuthorizationRequired(final MethodParameter parameter) {
        return parameter.getParameterAnnotation(AuthSupporterPrincipal.class).required();
    }
}

package the_t.mainproject.domain.auth.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.member.domain.Member;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MemberAuthMapper {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(target = "password", source = "password", qualifiedByName = "encodePassword")
    public abstract Member joinToMember(JoinReq joinReq);

    @Named("encodePassword")
    public String encodePassword(String password) { return this.passwordEncoder.encode(password); }
}

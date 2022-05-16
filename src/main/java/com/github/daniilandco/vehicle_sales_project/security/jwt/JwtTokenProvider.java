package com.github.daniilandco.vehicle_sales_project.security.jwt;

import com.github.daniilandco.vehicle_sales_project.exception.auth.UserIsNotLoggedInException;
import com.github.daniilandco.vehicle_sales_project.model.token.Token;
import com.github.daniilandco.vehicle_sales_project.model.user.User;
import com.github.daniilandco.vehicle_sales_project.repository.TokenRepository;
import com.github.daniilandco.vehicle_sales_project.repository.UserRepository;
import com.github.daniilandco.vehicle_sales_project.security.AuthContextHandler;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtTokenProvider {
    private final UserDetailsService userDetailsService;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private AuthContextHandler authContextHandler;
    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.access.secret}")
    private String accessSecretKey;
    @Value("${jwt.refresh.secret}")
    private String refreshSecretKey;
    @Value("${jwt.header}")
    private String authorizationHeader;
    @Value("${jwt.access.expiration}")
    private int accessValidityInSeconds;
    @Value("${jwt.refresh.expiration}")
    private int refreshValidityInSeconds;

    public JwtTokenProvider(@Qualifier("userDetailsServiceImplementation") UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init() {
        accessSecretKey = Base64.getEncoder().encodeToString(accessSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    public TokenResponse createToken(String email) {
        Claims claims = Jwts.claims().setSubject(email);

        Date now = new Date(System.currentTimeMillis());
        Date accessValidity = new Date(now.getTime() + accessValidityInSeconds);
        Date refreshValidity = new Date(now.getTime() + refreshValidityInSeconds);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(accessValidity)
                .signWith(SignatureAlgorithm.HS256, accessSecretKey.getBytes())
                .compact();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(refreshValidity)
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey.getBytes())
                .compact();
        return new TokenResponse(accessToken, refreshToken);
    }

    public Token saveToken(Long id, String refreshToken) throws UserIsNotLoggedInException {
        User user = userRepository.findById(id).get();
        Optional<Token> tokenData = tokenRepository.findByUser(user);
        if (tokenData.isPresent()) {
            tokenData.get().setRefreshToken(refreshToken);
            return tokenRepository.save(tokenData.get());
        }
        Token token = new Token(refreshToken, user);
        return tokenRepository.save(token);
    }

    public void removeToken(String refreshToken) {
        tokenRepository.deleteByRefreshToken(refreshToken);
    }


    public boolean validateAccessToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(accessSecretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public boolean validateRefreshToken(String token) {
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(refreshSecretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token);
        return !claimsJws.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(accessSecretKey.getBytes(StandardCharsets.UTF_8)).parseClaimsJws(token).getBody().getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader(authorizationHeader);
    }
}

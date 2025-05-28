package br.com.avila.security.jwt;

import br.com.avila.data.dto.security.TokenDTO;
import br.com.avila.exception.InvalidJwtAuthenticationException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

// Marca a classe como um serviço gerenciado pelo Spring
@Service
public class JwtTokenProvider {

    // Pega o valor da chave secreta do application.properties ou usa "secret" por padrão
    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    // Define o tempo de validade do token (1 hora por padrão)
    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000;

    // Injeta o serviço que carrega os dados do usuário
    @Autowired
    private UserDetailsService userDetailsService;

    // Algoritmo usado para assinar os tokens
    Algorithm algorithm = null;

    // Inicializa o algoritmo com a chave secreta codificada em Base64
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    // Cria um token de acesso e um refresh token com base no nome de usuário e nas roles
    public TokenDTO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds); // data de expiração
        String accessToken = getAccessToken(username, roles, now, validity); // gera token de acesso
        String refreshToken = getRefreshToken(username, roles, now); // gera token de refresh
        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO refreshToken( String refreshToken) {

        if (refreshTokenContainsBearer(refreshToken)) {
            refreshToken= refreshToken.substring("Bearer ".length()); // remove o prefixo "Bearer "
        }
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(refreshToken);
       String username = decodedJWT.getSubject();
       List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

       return createAccessToken(username, roles);
    }

    // Gera o refresh token (sem issuer)
    private String getRefreshToken(String username, List<String> roles, Date now) {
        Date refreshTokenValidy = new Date(now.getTime() + (validityInMilliseconds * 3));
        return JWT.create()
                .withClaim("roles", roles) // adiciona roles
                .withIssuedAt(now) // data de criação
                .withExpiresAt(refreshTokenValidy) // expiração
                .withSubject(username) // identifica o usuário
                .sign(algorithm); // assina o token
    }

    // Gera o access token com issuer (quem emitiu)
    private String getAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString(); // ex: http://localhost:8080
        return JWT.create()
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withSubject(username)
                .withIssuer(issuerUrl) // quem gerou o token
                .sign(algorithm);
    }

    // Obtém autenticação (Authentication) a partir de um token JWT
    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token); // decodifica o token
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject()); // busca o usuário
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities()); // cria o objeto de autenticação do Spring
    }

    // Decodifica e valida o token usando o algoritmo e chave secreta
    private DecodedJWT decodedToken(String token) {
        Algorithm alg = Algorithm.HMAC256(secretKey.getBytes());
        JWTVerifier verifier = JWT.require(alg).build(); // cria o verificador
        return verifier.verify(token); // verifica e retorna o token decodificado
    }

    // Extrai o token JWT do header Authorization do tipo "Bearer <token>"
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (refreshTokenContainsBearer(bearerToken)) {
            return bearerToken.substring("Bearer ".length()); // remove o prefixo "Bearer "
        }
        return null;
    }

    private static boolean refreshTokenContainsBearer(String refreshToken) {
        return StringUtils.isNotBlank(refreshToken) && refreshToken.startsWith("Bearer ");
    }

    // Valida o token verificando se está expirado
    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        try {
            if (decodedJWT.getExpiresAt().before(new Date())) {
                return false; // token expirado
            }
            return true; // token válido
        } catch (Exception e) {
            throw new InvalidJwtAuthenticationException("Expired or invalid JWT token");
        }
    }


}


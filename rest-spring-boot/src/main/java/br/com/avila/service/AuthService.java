package br.com.avila.service;

import br.com.avila.data.dto.V1.PersonDTO;
import br.com.avila.data.dto.security.AccountCredentialsDTO;
import br.com.avila.data.dto.security.TokenDTO;
import br.com.avila.exception.RequiredObjectIsNullException;
import br.com.avila.mapper.ObjectMapper;
import br.com.avila.model.Person;
import br.com.avila.model.User;
import br.com.avila.repository.UserRepository;
import br.com.avila.security.jwt.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<TokenDTO> signIn(AccountCredentialsDTO credentials) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                      credentials.getUsername(), credentials.getPassword()
                ));

        var user = userRepository.findByUsername(credentials.getUsername());
        if(user == null) {throw new UsernameNotFoundException("User " + credentials.getUsername() + " not found");}

        var token = tokenProvider.createAccessToken(credentials.getUsername(), user.getRoles());

        return ResponseEntity.ok(token);
    }

    public ResponseEntity<TokenDTO> refreshToken(String username, String refreshToken) {

        var user = userRepository.findByUsername(username);
        TokenDTO token;
        if(user != null){
            token = tokenProvider.refreshToken(refreshToken);
        } else {
            throw new UsernameNotFoundException("User " + username + " not found");
        }
        return ResponseEntity.ok(token);

    }


    public AccountCredentialsDTO create(AccountCredentialsDTO user) {

        if(user == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one new user");
        var entity = new User();
        entity.setFullName(user.getFullname());
        entity.setUserName(user.getUsername());
        entity.setPassword(generateHashedPassword(user.getPassword()));
        entity.setAccountNonExpired(true);
        entity.setAccountNonLocked(true);
        entity.setCredentialsNonExpired(true);
        entity.setEnabled(true);
        var dto = userRepository.save(entity)   ;
      //  return ObjectMapper.parseObject(dto, AccountCredentialsDTO.class);
     return new AccountCredentialsDTO( dto.getUsername(), dto.getPassword(), dto.getFullName());

    }


    private String generateHashedPassword(String password) {

        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pbkdf2", pbkdf2Encoder); // encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);

        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
       return passwordEncoder.encode(password);
       // var pass2 = passwordEncoder.encode("123admin");
       /// System.out.println(pass1);
       //System.out.println(pass2);


    }
}


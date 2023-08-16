package com.blogstack.helper;

import com.blogstack.commons.BlogStackCommonConstants;
import com.blogstack.entities.BlogStackRoleDetail;
import com.blogstack.entities.BlogStackUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtHelper {

    public static final long JWT_TOKEN_VALIDITY =  1000 * 60 * 15;

    public static final long JWT_REFRESH_TOKEN_VALIDITY = 1000 * 60 * 60 * 24;

    private String secret;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //for retrieveing any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token for user
    public String generateToken(String emailId, Set<BlogStackRoleDetail> blogStackRoleDetails,String jwtSecret) {
        this.secret = jwtSecret;
        Map<String, Object> claims = new HashMap<>();
        List<String> blogStackUserRoles = blogStackRoleDetails.stream()
                                         .map(roleDetails -> roleDetails.getBrdRoleName())
                                         .collect(Collectors.toList());

        claims.put(BlogStackCommonConstants.ROLE_CONSTANT,blogStackUserRoles);
        return doGenerateToken(claims, emailId,jwtSecret);
    }

    //while creating the token -
    //1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
    //2. Sign the JWT using the HS512 algorithm and secret key.
    //3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject,String secret) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String generateRefreshToken(String emailId, String secret){
        Map<String, Object> claims = new HashMap<>();
        return doGenerateRefreshToken(claims, emailId, secret);
    }

    private String doGenerateRefreshToken(Map<String, Object> claims, String subject, String jwtSecret) {

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_REFRESH_TOKEN_VALIDITY ))
                .signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
    }

    public String getSubject(String token)
    {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Boolean validateToken(String token) {

        return (!isTokenExpired(token));
    }
}
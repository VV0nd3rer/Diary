package com.kverchi.diary.model.entity;

/**
 * Created by Liudmyla Melnychuk on 18.2.2019.
 */

import com.kverchi.diary.security.SecretsManager;
import org.hibernate.annotations.ColumnTransformer;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;

@Entity
@Table(name="oauth_client_credentials")
public class OauthClientCredentials {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="credentials_id")
    private int credentialsId;

    @ColumnTransformer(
            read =  "pgp_sym_decrypt(refresh_token::bytea, '" + SecretsManager.Constants.ENCODING_KEY + "')",
            write = "pgp_sym_encrypt(?, '" + SecretsManager.Constants.ENCODING_KEY + "')"
    )
    @Column(name="refresh_token")
    private String refreshToken;

    @Column(name="token_url")
    private String tokenUrl;

    @ColumnTransformer(
            read =  "pgp_sym_decrypt(oauth_client_id::bytea, '" + SecretsManager.Constants.ENCODING_KEY + "')",
            write = "pgp_sym_encrypt(?, '" + SecretsManager.Constants.ENCODING_KEY + "')"
    )
    @Column(name="oauth_client_id")
    private String oauthClientId;

    @ColumnTransformer(
            read =  "pgp_sym_decrypt(oauth_secret::bytea, '" + SecretsManager.Constants.ENCODING_KEY + "')",
            write = "pgp_sym_encrypt(?, '" + SecretsManager.Constants.ENCODING_KEY + "')"
    )
    @Column(name="oauth_secret")
    private String oauthSecret;

    @ColumnTransformer(
            read =  "pgp_sym_decrypt(access_token::bytea, '" + SecretsManager.Constants.ENCODING_KEY + "')",
            write = "pgp_sym_encrypt(?, '" + SecretsManager.Constants.ENCODING_KEY + "')"
    )
    @Column(name="access_token")
    private String accessToken;

    @Column(name="token_expires")
    private long tokenExpires;

    @ColumnTransformer(
            read =  "pgp_sym_decrypt(credentials_email::bytea, '" + SecretsManager.Constants.ENCODING_KEY + "')",
            write = "pgp_sym_encrypt(?, '" + SecretsManager.Constants.ENCODING_KEY + "')"
    )
    @Column(name="credentials_email")
    private String credentialsEmail;

    public int getCredentialsId() {
        return credentialsId;
    }

    public void setCredentialsId(int credentialsId) {
        this.credentialsId = credentialsId;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getOauthClientId() {
        return oauthClientId;
    }

    public void setOauthClientId(String oauthClientId) {
        this.oauthClientId = oauthClientId;
    }

    public String getOauthSecret() {
        return oauthSecret;
    }

    public void setOauthSecret(String oauthSecret) {
        this.oauthSecret = oauthSecret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getTokenExpires() {
        return tokenExpires;
    }

    public void setTokenExpires(long tokenExpires) {
        this.tokenExpires = tokenExpires;
    }

    public String getCredentialsEmail() {
        return credentialsEmail;
    }

    public void setCredentialsEmail(String credentialsEmail) {
        this.credentialsEmail = credentialsEmail;
    }
}

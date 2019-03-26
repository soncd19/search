package com.home.search.properties;

/**
 * Created by soncd on 07/12/2018
 */
public class ElasticContainer {
    private String imageUrl;
    private String version;
    private String credentialUsername;
    private String credentialPassword;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getVersion() {
        return version;
    }

    public String getCredentialUsername() {
        return credentialUsername;
    }

    public String getCredentialPassword() {
        return credentialPassword;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCredentialUsername(String credentialUsername) {
        this.credentialUsername = credentialUsername;
    }

    public void setCredentialPassword(String credentialPassword) {
        this.credentialPassword = credentialPassword;
    }
}

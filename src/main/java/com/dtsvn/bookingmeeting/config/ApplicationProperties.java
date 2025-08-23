package com.dtsvn.bookingmeeting.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Meeting Jhipster.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 * See {@link tech.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private final Liquibase liquibase = new Liquibase();
    private final Security security = new Security();

    // jhipster-needle-application-properties-property

    public Liquibase getLiquibase() {
        return liquibase;
    }

    public Security getSecurity() {
        return security;
    }

    // jhipster-needle-application-properties-property-getter

    public static class Liquibase {

        private Boolean asyncStart = true;

        public Boolean getAsyncStart() {
            return asyncStart;
        }

        public void setAsyncStart(Boolean asyncStart) {
            this.asyncStart = asyncStart;
        }
    }

    public static class Security {
        private final Jwt jwt = new Jwt();

        public Jwt getJwt() {
            return jwt;
        }

        public static class Jwt {
            private String secretKey;
            private Long expiration;
            private final RefreshToken refreshToken = new RefreshToken();

            public String getSecretKey() {
                return secretKey;
            }

            public void setSecretKey(String secretKey) {
                this.secretKey = secretKey;
            }

            public Long getExpiration() {
                return expiration;
            }

            public void setExpiration(Long expiration) {
                this.expiration = expiration;
            }

            public RefreshToken getRefreshToken() {
                return refreshToken;
            }

            public static class RefreshToken {
                private Long expiration;

                public Long getExpiration() {
                    return expiration;
                }

                public void setExpiration(Long expiration) {
                    this.expiration = expiration;
                }
            }
        }
    }
    // jhipster-needle-application-properties-property-class
}

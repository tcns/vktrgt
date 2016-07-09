package ru.tcns.vktrgt.config;

/**
 * Application constants.
 */
public final class Constants {

    // Spring profile for development, production and "fast", see http://jhipster.github.io/profiles.html
    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_PRODUCTION = "prod";
    public static final String SPRING_PROFILE_FAST = "fast";
    // Spring profile used when deploying with Spring Cloud (used when deploying to CloudFoundry)
    public static final String SPRING_PROFILE_CLOUD = "cloud";
    // Spring profile used when deploying to Heroku
    public static final String SPRING_PROFILE_HEROKU = "heroku";

    public static final String PROD_HOST = "https://vktrgt.herokuapp.com";

    public static final String APPLICATION_NAME="vktrgt";

    public static final String SYSTEM_ACCOUNT = "system";

    public static final String GOOGLE_DRIVE_TOKEN = "google.drive.token";
    public static final String VK_TOKEN = "vk.token";

    public static final String VK_CLIENT_ID = "5385314";
    public static final String VK_CLIENT_SECRET = "6u54vy2z5xAisi0lmDQ2";

    public static final String GOOGLE_SECRET = "387946429490-qf7svfmvf8hvu8mo1v876p0j9395m4cp.apps.googleusercontent.com";

    public static final String GOOGLE_FOLDER = "VKTRGT_FOLDER";

    private Constants() {
    }
}

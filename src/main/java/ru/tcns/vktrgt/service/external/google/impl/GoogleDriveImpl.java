package ru.tcns.vktrgt.service.external.google.impl;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import org.apache.commons.io.IOUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.tcns.vktrgt.config.AuthFactory;
import ru.tcns.vktrgt.config.Constants;
import ru.tcns.vktrgt.config.GoogleCredential;


import javax.inject.Inject;
import java.io.*;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by TIMUR on 28.05.2016.
 */
@Service
public class GoogleDriveImpl {
    private static final JsonFactory JSON_FACTORY =
        JacksonFactory.getDefaultInstance();
    private static final java.io.File DATA_STORE_DIR = new java.io.File("resources/drive-credentials.json");
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    private static HttpTransport HTTP_TRANSPORT;
    private static final List<String> SCOPES =
        Arrays.asList(DriveScopes.DRIVE);
    @Inject
    AuthFactory authFactory;
    @Inject
    private Environment env;

    public Credential authorize() throws IOException, GeneralSecurityException {
        HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        InputStream in =
            GoogleDriveImpl.class.getResourceAsStream("/client_secret2.json");
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        GoogleAuthorizationCodeFlow flow =
            new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();
        String hostName = "http://localhost:8080";
        if (Arrays.asList(env.getActiveProfiles()).contains(Constants.SPRING_PROFILE_PRODUCTION)) {
            hostName = Constants.PROD_HOST;
        }
        GoogleCredential credential = new GoogleCredential.Builder()
            .setTransport(HTTP_TRANSPORT)
            .setJsonFactory(JSON_FACTORY)
            .setServiceAccountPrivateKeyFromP12File(GoogleDriveImpl.class.getResourceAsStream("/vktrgt.p12"))
                .setServiceAccountScopes(SCOPES)
                .setServiceAccountId("drive-893@vktrgt-1322.iam.gserviceaccount.com")
                .build();

        //Credential credential = authFactory.authorize("user", hostName);
        //Credential credential = new AuthorizationCodeInstalledApp(
        //  flow, new LocalServerReceiver.Builder().setPort(58459).build()).authorize("user");
        System.out.println(
            "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    public Drive getDriveService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Drive.Builder(
            HTTP_TRANSPORT, JSON_FACTORY, credential)
            .setApplicationName(Constants.APPLICATION_NAME)
            .build();
    }

    public String getOrCreateFolder(String parent, String name) {
        try {
            if (parent != null) {
                parent = getOrCreateFolder(null, parent);
            }
            List<File> files = null;
            if (parent!=null) {
                files = getDriveService().files().list().setQ("'" + parent + "'" + " in parents and name='" + name + "'").execute().getFiles();
            } else {
                files = getDriveService().files().list().setQ("name='" + name + "'").execute().getFiles();
            }

            if (files != null && !files.isEmpty()) {
                return files.get(0).getId();
            }
            File fileMetadata = new File();
            fileMetadata.setName(name);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");
            if (parent != null) {
                fileMetadata.setParents(Arrays.asList(parent));
            }

            File returnFile = getDriveService().files().create(fileMetadata).execute();
            return returnFile.getId();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return null;
    }

    public File saveFile(String mimeType, String name, InputStream stream, String folder) {
        try {
            File body = new File();
            body.setName(name);
            body.setMimeType(mimeType);
            body.setParents(Arrays.asList(folder));
            InputStreamContent fileContent = new InputStreamContent(mimeType, stream);
            File returnFile = getDriveService().files().create(body, fileContent).execute();
            Permission permission = new Permission();
            permission.setRole("reader");
            permission.setType("anyone");
            permission.set("WithLink", true);
            getDriveService().permissions().create(returnFile.getId(), permission).execute();
            return getDriveService().files().get(returnFile.getId()).setFields("webContentLink").execute();
        } catch (Exception ex) {

        }
        return null;
    }
}

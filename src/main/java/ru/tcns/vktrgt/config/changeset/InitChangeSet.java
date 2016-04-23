package ru.tcns.vktrgt.config.changeset;

import com.github.mongobee.changeset.ChangeLog;
import com.github.mongobee.changeset.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;

import java.util.Date;

/**
 * Created by TIMUR on 23.04.2016.
 */
@ChangeLog(order = "001")
public class InitChangeSet {
    @ChangeSet(order = "001", id = "ChangeSet-users", author = "jhipster")
    public void initUsers(DB db) {
        /*DBCollection mycollection = db.getCollection("jhi_authority");
        BasicDBObject doc1 = new BasicDBObject().append("_id", "ROLE_ADMIN");
        BasicDBObject doc2 = new BasicDBObject().append("_id", "ROLE_USER");
        mycollection.insert(doc1);
        mycollection.insert(doc2);
        DBCollection userCollection = db.getCollection("jhi_user");
        BasicDBObject doc3 = new BasicDBObject()
            .append("_id", "user-2")
            .append("login", "admin")
            .append("password", "$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC")
            .append("first_name", "admin")
            .append("last_name", "Administrator")
            .append("email", "admin@localhost")
            .append("activated", "true")
            .append("lang_key", "en")
            .append("created_by", "system")
            .append("created_date", new Date())
            .append("authorities", new BasicDBObject().append("_id", "ROLE_ADMIN")
                .append("_id", "ROLE_USER"));
        userCollection.insert(doc3);*/
    }
}

/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.mongo.test.tools;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.IMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.runtime.Network;
import org.bson.Document;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.http.credentials.password.PasswordEncoder;
import org.pac4j.http.credentials.password.SaltedSha512PasswordEncoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Simulates a MongoDB server.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
public class MongoServer implements TestsConstants {

    private MongodExecutable mongodExecutable;

    public void start(final int port) {
        MongodStarter starter = MongodStarter.getDefaultInstance();

        try {
            IMongodConfig mongodConfig = new MongodConfigBuilder()
                    .version(Version.Main.PRODUCTION)
                    .net(new Net(port, Network.localhostIsIPv6()))
                    .build();

            mongodExecutable = starter.prepare(mongodConfig);
            MongodProcess mongod = mongodExecutable.start();

            // populate
            final MongoClient mongo = new MongoClient("localhost", port);
            final MongoDatabase db = mongo.getDatabase("users");
            db.createCollection("users");
            final MongoCollection<Document> collection = db.getCollection("users");
            final PasswordEncoder encoder = new SaltedSha512PasswordEncoder(SALT);
            final String password = encoder.encode(PASSWORD);
            Map<String, Object> properties1 = new HashMap<>();
            properties1.put(USERNAME, GOOD_USERNAME);
            properties1.put(PASSWORD, password);
            properties1.put(FIRSTNAME, FIRSTNAME_VALUE);
            collection.insertOne(new Document(properties1));
            Map<String, Object> properties2 = new HashMap<>();
            properties2.put(USERNAME, MULTIPLE_USERNAME);
            properties2.put(PASSWORD, password);
            collection.insertOne(new Document(properties2));
            Map<String, Object> properties3 = new HashMap<>();
            properties3.put(USERNAME, MULTIPLE_USERNAME);
            properties3.put(PASSWORD, password);
            collection.insertOne(new Document(properties3));

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        if (mongodExecutable != null) {
            mongodExecutable.stop();
        }
    }
}

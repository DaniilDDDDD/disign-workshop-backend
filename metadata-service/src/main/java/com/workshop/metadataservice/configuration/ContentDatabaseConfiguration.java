package com.workshop.metadataservice.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static java.util.Collections.singletonList;


@Configuration
@EnableMongoRepositories(
        basePackages = {"com.workshop.metadataservice.repository.content"},
        mongoTemplateRef = "contentMongoTemplate"
)
@EnableConfigurationProperties
public class ContentDatabaseConfiguration {


    @Bean(name = "contentDatabaseProperties")
    @ConfigurationProperties(prefix = "mongodb.content")
    public MongoProperties contentProperties() {
        return new MongoProperties();
    }


    @Bean(name = "contentMongoClient")
    public MongoClient mongoClient(
            @Qualifier("contentDatabaseProperties") MongoProperties mongoProperties
    ) {

        MongoCredential credential = MongoCredential.createCredential(
                mongoProperties.getUsername(),
                mongoProperties.getAuthenticationDatabase(),
                mongoProperties.getPassword());

        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder
                        .hosts(singletonList(new ServerAddress(
                                mongoProperties.getHost(), mongoProperties.getPort()))))
                .credential(credential)
                .build());
    }


    @Bean(name = "contentMongoDBFactory")
    public MongoDatabaseFactory mongoDatabaseFactory(
            @Qualifier("contentMongoClient") MongoClient mongoClient,
            @Qualifier("contentDatabaseProperties") MongoProperties mongoProperties) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getDatabase());
    }


    @Bean(name = "contentMongoTemplate")
    public MongoTemplate mongoTemplate(
            @Qualifier("contentMongoDBFactory") MongoDatabaseFactory mongoDatabaseFactory) {
        return new MongoTemplate(mongoDatabaseFactory);
    }
}

package com.workshop.metadataservice.configuration;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.workshop.metadataservice.repository.metadata.CommentRepository;
import com.workshop.metadataservice.repository.metadata.LikeRepository;
import com.workshop.metadataservice.repository.metadata.ReviewRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static java.util.Collections.singletonList;


@Configuration
@EnableMongoRepositories(
        basePackages = {"com.workshop.metadataservice.repository.metadata"},
        mongoTemplateRef = "metadataMongoTemplate"
)
@EnableConfigurationProperties
public class MetadataDatabaseConfiguration {


    @Bean(name = "metadataDatabaseProperties")
    @ConfigurationProperties(prefix = "mongodb.metadata")
    @Primary
    public MongoProperties metadataProperties() {
        return new MongoProperties();
    }


    @Bean(name = "metadataMongoClient")
    public MongoClient mongoClient(
            @Qualifier("metadataDatabaseProperties") MongoProperties mongoProperties
    ) {

        MongoCredential credential = MongoCredential.createCredential(
                mongoProperties.getUsername(),
                mongoProperties.getAuthenticationDatabase(),
                mongoProperties.getPassword());

        return MongoClients.create(MongoClientSettings.builder()
                .applyToClusterSettings(builder -> builder
                        .hosts(singletonList(new ServerAddress(mongoProperties.getHost(), mongoProperties.getPort()))))
                .credential(credential)
                .build());
    }


    @Primary
    @Bean(name = "metadataMongoDBFactory")
    public MongoDatabaseFactory mongoDatabaseFactory(
            @Qualifier("metadataMongoClient") MongoClient mongoClient,
            @Qualifier("metadataDatabaseProperties") MongoProperties mongoProperties) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, mongoProperties.getDatabase());
    }
}

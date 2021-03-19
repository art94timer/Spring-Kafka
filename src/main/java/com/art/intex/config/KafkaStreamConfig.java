package com.art.intex.config;


import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;


@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

    @Value("${schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${stream.topic.name}")
    private String streamTopic;

    @Value("${person.topic.name}")
    private String personTopic;

    @Value("${change.value.field}")
    private String changeValueField;

    @Bean
    public SchemaRegistryClient schemaRegistryClient() {
        return new CachedSchemaRegistryClient(schemaRegistryUrl, 100);
    }


    @Bean
    public KStream<String, GenericRecord> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, GenericRecord> stream = streamsBuilder
                .stream(streamTopic);
        stream.mapValues((k, v) -> {
            String password = v.get(changeValueField).toString();
            password = password.replaceAll(".", "*");
            v.put(changeValueField, password);
            return v;
        });
        stream.to(personTopic);
        return stream;
    }
}

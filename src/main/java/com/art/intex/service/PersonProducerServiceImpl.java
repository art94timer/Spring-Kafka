package com.art.intex.service;

import com.art.intex.converter.FromObjectToJsonConverter;
import com.art.intex.service.interfaces.PersonProducerService;
import io.confluent.kafka.schemaregistry.client.SchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;
import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tech.allegro.schema.json2avro.converter.JsonAvroConverter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PersonProducerServiceImpl implements PersonProducerService {

    private final KafkaTemplate<String, GenericRecord> kafkaTemplate;

    private final JsonAvroConverter converter = new JsonAvroConverter();

    @Value("${stream.topic.name}")
    private String streamTopic;

    private final SchemaRegistryClient schemaRegistryClient;

    public void transferToStream(Object person) {
        Schema.Parser parser = new Schema.Parser();
        Schema latestSchema = parser.parse(getLatestSchema());
        GenericRecord genericRecord = buildRecord(person, latestSchema);
        kafkaTemplate.send(streamTopic, genericRecord);
    }

//    private GenericRecord buildRecord(Object person, Schema latestSchema) {
//        try {
//            String json = FromObjectToJsonConverter.convert(person);
//            Decoder decoder = new DecoderFactory().jsonDecoder(latestSchema, json);
//            DatumReader<GenericData.Record> reader =
//                    new GenericDatumReader<>(latestSchema);
//            return reader.read(null, decoder);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private GenericRecord buildRecord(Object person, Schema latestSchema) {
        String json = FromObjectToJsonConverter.convert(person);
        return converter.convertToGenericDataRecord(json.getBytes(), latestSchema);
    }


    private String getLatestSchema() {
        try {
            return schemaRegistryClient.getLatestSchemaMetadata(streamTopic.concat("-value")).getSchema();
        } catch (IOException | RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}

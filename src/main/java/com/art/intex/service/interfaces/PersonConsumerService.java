package com.art.intex.service.interfaces;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface PersonConsumerService {

   void listenUserTopic(ConsumerRecord<String, GenericRecord> record);
}

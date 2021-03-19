package com.art.intex.service;

import com.art.intex.service.interfaces.PersonConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PersonConsumerServiceImpl implements PersonConsumerService {

    private final static String TEMPLATE = "Person with login: %s and age: %s. His password is: %s";

    @KafkaListener(topics = "${person.topic.name}", groupId = "${group.name}")
    public void listenUserTopic(ConsumerRecord<String, GenericRecord> record) {
        String login = record.value().get("login").toString();
        String age = record.value().get("age").toString();
        String password = record.value().get("password").toString();
        log.info(String.format(TEMPLATE, login, age, password));
    }


}






package com.ithows.util.kafka;

import com.ithows.AppConfig;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

public class Producer {
    
    private static final String BOOTSTRAP_SERVER_KEY = "kafka_produce_bootstrap_server";
    
    private static final Logger logger = Logger.getLogger(Producer.class.getName());
    private static KafkaProducer<String, String> kafkaProducer;

    static {
        initProducer();
    }
    
    private static void initProducer() {
        String server = AppConfig.getConf(BOOTSTRAP_SERVER_KEY);
        
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, server);                        // 카프카 클러스터
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);         
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        
        kafkaProducer = new KafkaProducer<>(properties);
    }
    
    
    /**
     * 메세지를 보냄
     * @param topic         토픽
     * @param message       메세지
     */
    public static void send(String topic, String message) {
        Objects.requireNonNull(topic, "topic is null");
        Objects.requireNonNull(message, "message is null");
        
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        
        send(kafkaProducer, record);
    }
    
    
    /**
     * 메세지를 보냄
     * @param properties    설정 정보
     * @param topic         토픽
     * @param message       메세지
     */
    public static void send(Properties properties, String topic, String message) {
        Objects.requireNonNull(properties, "properties is null");
        Objects.requireNonNull(topic, "topic is null");
        Objects.requireNonNull(message, "message is null");
        
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, message);
        
        send(producer, record);
    }
    
    
    /**
     * 메세지를 보냄
     * @param producer      카프카 프로듀서
     * @param record        메세지
     */
    private static void send(KafkaProducer<String, String> producer, ProducerRecord<String, String> record) {
        Objects.requireNonNull(producer, "producer is null");
        Objects.requireNonNull(record, "record is null");
        
        producer.send(record, (RecordMetadata rm, Exception e) -> {
            if (e == null) {
                logger.log(Level.INFO, "send message to {0}", rm.topic());
            }
        });
        
        producer.flush();
    }
}

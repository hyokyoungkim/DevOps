package com.ithows.util.kafka;

import com.ithows.AppConfig;
import java.time.Duration;
import java.util.Arrays;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class Consumer {
    
    private static final String BOOTSTRAP_SERVER_KEY = "kafka_consume_bootstrap_server";
    private static final String GROUP_ID_KEY = "kafka_consume_group_id";
    private static final String OFFSET_RESET_KEY = "kafka_consume_offset_reset";
    
    private static final Logger logger = Logger.getLogger(Consumer.class.getName());
    private static KafkaConsumer<String, String> kafkaConsumer;
    
    static {
        initConsumer();
    }
    
    private static void initConsumer() {
        String server = AppConfig.getConf(BOOTSTRAP_SERVER_KEY);
        String groupId = AppConfig.getConf(GROUP_ID_KEY);
        String offsetConfig = AppConfig.getConf(OFFSET_RESET_KEY);
        
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, server);                            // 카프카 클러스터
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);   
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);                                    // 컨슈머 그룹 식별자
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetConfig);                      // 초기 오프셋 설정
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);                             // 주기적으로 오프셋 커밋
        
        kafkaConsumer = new KafkaConsumer<>(properties);
    }
    
    
    /**
     * 메세지를 받음
     * @param topic         토픽
     */
    public static void receive(String topic) {
        Objects.requireNonNull(topic, "topic is null");
        
        kafkaConsumer.subscribe(Arrays.asList(topic));
        
        receive(kafkaConsumer);
    }
    
    
    /**
     * 메시지를 받음
     * @param topics        토픽
     */
    public static void receive(String[] topics) {
        Objects.requireNonNull(topics, "topics is null");
        
        kafkaConsumer.subscribe(Arrays.asList(topics));
        
        receive(kafkaConsumer);
    }
    
    
    /**
     * 메세지를 받음
     * @param properties        설정 정보
     * @param topic             토픽
     */
    public static void receive(Properties properties, String topic) {
        Objects.requireNonNull(properties, "properties is null");
        Objects.requireNonNull(topic, "topic is null");
        
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topic));
        
        receive(kafkaConsumer);
    }
    
    
    /**
     * 메세지를 받음
     * @param properties        설정 정보
     * @param topics            토픽
     */
    public static void receive(Properties properties, String[] topics) {
        Objects.requireNonNull(properties, "properties is null");
        Objects.requireNonNull(topics, "topics is null");
        
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        kafkaConsumer.subscribe(Arrays.asList(topics));
        
        receive(kafkaConsumer);
    }
    
    
    /**
     * 메세지를 받음
     * @param consumer      카프카 컨슈머
     */
    private static void receive(KafkaConsumer<String, String> consumer) {
        Objects.requireNonNull(consumer, "consumer is null");
        
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(10));  // Duration 값 동안 기다림
        
            for (ConsumerRecord<String, String> record : records) {
                logger.log(Level.INFO, "receive value \"{0}\" from {1}", new String[]{record.value(), record.topic()});
            }
        }
    }
}

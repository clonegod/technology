package clonegod.kafka.tests;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.TimeUnit;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import clonegod.kafka.consumer.Receiver;
import clonegod.kafka.producer.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringKafkaApplicationTest {

  private static final String HELLOWORLD_TOPIC = "helloworld.t";

  // 启动1个内嵌的 Kafka Server 用于测试
  @ClassRule
  public static KafkaEmbedded embeddedKafka =
      new KafkaEmbedded(1, true, HELLOWORLD_TOPIC);

  @Autowired
  private Receiver receiver;

  @Autowired
  private Sender sender;

  @Test
  public void testReceive() throws Exception {
    sender.send(HELLOWORLD_TOPIC, "Hello Spring Kafka!");

    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
    assertThat(receiver.getLatch().getCount()).isEqualTo(0);
  }
  
}
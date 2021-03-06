package dataguru.redis.sample02;

import static org.junit.Assert.*;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.Assert;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.exceptions.InvalidURIException;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class JedisPoolTest  {
  private static HostAndPort hnp = HostAndPortUtil.getRedisServers().get(0);
  
  public static final String HOST = "192.168.1.103";
  
  @Test
  public void checkConnections() {
    //JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000);
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000);
    Jedis jedis = pool.getResource();
   // jedis.auth("foobared");
    jedis.set("foo", "bar");
    assertEquals("bar", jedis.get("foo"));
    jedis.close();
    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test
  public void checkCloseableConnections() throws Exception {
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000);
    Jedis jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.set("foo", "bar");
    assertEquals("bar", jedis.get("foo"));
    jedis.close();
    pool.close();
    assertTrue(pool.isClosed());
  }

  @Test
  public void checkConnectionWithDefaultPort() {
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort());
    Jedis jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.set("foo", "bar");
    assertEquals("bar", jedis.get("foo"));
    jedis.close();
    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test
  public void checkJedisIsReusedWhenReturned() {

    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort());
    Jedis jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.set("foo", "0");
    jedis.close();

    jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.incr("foo");
    jedis.close();
    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test
  public void checkPoolRepairedWhenJedisIsBroken() {
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort());
    Jedis jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.quit();
    jedis.close();

    jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.incr("foo");
    jedis.close();
    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test(expected = JedisConnectionException.class)
  public void checkPoolOverflow() {
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxTotal(1);
    config.setBlockWhenExhausted(false);
   // config.
    JedisPool pool = new JedisPool(config, hnp.getHost(), hnp.getPort());
    Jedis jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.set("foo", "0");

    Jedis newJedis = pool.getResource();
    newJedis.auth("foobared");
    newJedis.incr("foo");
  }

  @Test
  public void securePool() {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setTestOnBorrow(true);
    JedisPool pool = new JedisPool(config, hnp.getHost(), hnp.getPort(), 2000, "foobared");
    Jedis jedis = pool.getResource();
    jedis.set("foo", "bar");
    jedis.close();
    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test
  public void nonDefaultDatabase() {
    JedisPool pool0 = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000,
        "foobared");
    Jedis jedis0 = pool0.getResource();
    jedis0.set("foo", "bar");
    assertEquals("bar", jedis0.get("foo"));
    jedis0.close();
    pool0.destroy();
    assertTrue(pool0.isClosed());

    JedisPool pool1 = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000,
        "foobared", 1);
    Jedis jedis1 = pool1.getResource();
    assertNull(jedis1.get("foo"));
    jedis1.close();
    pool1.destroy();
    assertTrue(pool1.isClosed());
  }

  @Test
  public void startWithUrlString() {
    Jedis j = new Jedis(HOST, 6380);
    j.auth("foobared");
    j.select(2);
    j.set("foo", "bar");
    JedisPool pool = new JedisPool("redis://:foobared@localhost:6380/2");
    Jedis jedis = pool.getResource();
    assertEquals("PONG", jedis.ping());
    assertEquals("bar", jedis.get("foo"));
  }

  @Test
  public void startWithUrl() throws URISyntaxException {
    Jedis j = new Jedis(HOST, 6380);
    j.auth("foobared");
    j.select(2);
    j.set("foo", "bar");
    JedisPool pool = new JedisPool(new URI("redis://:foobared@localhost:6380/2"));
    Jedis jedis = pool.getResource();
    assertEquals("PONG", jedis.ping());
    assertEquals("bar", jedis.get("foo"));
  }

  @Test(expected = InvalidURIException.class)
  public void shouldThrowInvalidURIExceptionForInvalidURI() throws URISyntaxException {
    JedisPool pool = new JedisPool(new URI("localhost:6380"));
  }

  @Test
  public void allowUrlWithNoDBAndNoPassword() throws URISyntaxException {
    new JedisPool("redis://localhost:6380");
    new JedisPool(new URI("redis://localhost:6380"));
  }

  @Test
  public void selectDatabaseOnActivation() {
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000,
        "foobared");

    Jedis jedis0 = pool.getResource();
    assertEquals(""+0, ""+jedis0.getDB());

    jedis0.select(1);
    assertEquals(""+1, ""+jedis0.getDB());

    jedis0.close();

    Jedis jedis1 = pool.getResource();
    assertTrue("Jedis instance was not reused", jedis1 == jedis0);
    assertEquals(""+0, ""+jedis1.getDB());

    jedis1.close();
    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test
  public void customClientName() {
    JedisPool pool0 = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000,
        "foobared", 0, "my_shiny_client_name");

    Jedis jedis = pool0.getResource();

    assertEquals("my_shiny_client_name", jedis.clientGetname());

    jedis.close();
    pool0.destroy();
    assertTrue(pool0.isClosed());
  }



  @Test
  public void returnResourceShouldResetState() {
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxTotal(1);
    config.setBlockWhenExhausted(false);
    JedisPool pool = new JedisPool(config, hnp.getHost(), hnp.getPort(), 2000, "foobared");

    Jedis jedis = pool.getResource();
    try {
      jedis.set("hello", "jedis");
      Transaction t = jedis.multi();
      t.set("hello", "world");
    } finally {
      jedis.close();
    }

    Jedis jedis2 = pool.getResource();
    try {
      assertTrue(jedis == jedis2);
      assertEquals("jedis", jedis2.get("hello"));
    } finally {
      jedis2.close();
    }

    pool.destroy();
    assertTrue(pool.isClosed());
  }

  @Test
  public void checkResourceIsCloseable() {
    GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxTotal(1);
    config.setBlockWhenExhausted(false);
    JedisPool pool = new JedisPool(config, hnp.getHost(), hnp.getPort(), 2000, "foobared");

    Jedis jedis = pool.getResource();
    try {
      jedis.set("hello", "jedis");
    } finally {
      jedis.close();
    }

    Jedis jedis2 = pool.getResource();
    try {
      assertEquals(jedis, jedis2);
    } finally {
      jedis2.close();
    }
  }

  @Test
  public void getNumActiveIsNegativeWhenPoolIsClosed() {
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000,
        "foobared", 0, "my_shiny_client_name");

    pool.destroy();
    assertTrue(pool.getNumActive() < 0);
  }

  @Test
  public void getNumActiveReturnsTheCorrectNumber() {
    JedisPool pool = new JedisPool(new JedisPoolConfig(), hnp.getHost(), hnp.getPort(), 2000);
    Jedis jedis = pool.getResource();
    jedis.auth("foobared");
    jedis.set("foo", "bar");
    assertEquals("bar", jedis.get("foo"));

    assertEquals(1, pool.getNumActive());

    Jedis jedis2 = pool.getResource();
    jedis.auth("foobared");
    jedis.set("foo", "bar");

    assertEquals(2, pool.getNumActive());

    jedis.close();
    assertEquals(1, pool.getNumActive());

    jedis2.close();

    assertEquals(0, pool.getNumActive());

    pool.destroy();
  }
}

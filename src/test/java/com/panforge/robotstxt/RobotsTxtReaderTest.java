/*
 * Copyright 2016 Piotr Andzel.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.panforge.robotstxt;

import java.io.InputStream;
import java.util.List;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 * Testing check algorithm.
 */
public class RobotsTxtReaderTest {

  private static RobotsTxt bots;

  public RobotsTxtReaderTest() {
  }

  @BeforeClass
  public static void initTests() throws Exception {
    try (InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("robots.txt")) {
      RobotsTxtReader reader = new RobotsTxtReader(MatchingStrategy.DEFAULT, WinningStrategy.DEFAULT);
      bots = reader.readRobotsTxt(inputStream);
    }
  }

  @Test
  public void testUnmatched() throws Exception {
    String user_agent = "Any";
    
    assertTrue("/unmatched/foo.txt", bots.query(user_agent, "/unmatched/foo.txt"));
  }

  @Test
  public void testAnyGroup() throws Exception {
    String user_agent = "Any";
    
    assertTrue("/root", bots.query(user_agent, "/root"));
    assertFalse("/root/re.txt", bots.query(user_agent, "/root/re.txt"));
    assertFalse("/root/private/re.txt", bots.query(user_agent, "/root/private/re.txt"));
    
    assertTrue("/root/data/re.txt", bots.query(user_agent, "/root/data/re.txt"));
    assertTrue("/root/data/public/re.txt", bots.query(user_agent, "/root/data/public/re.txt"));
  }

  @Test
  public void testBanedGroup() throws Exception {
    String user_agent = "Baned";
    
    assertFalse("/root/data/re.txt", bots.query(user_agent, "/root/data/re.txt"));
  }

  @Test
  public void testSuperUserGroup() throws Exception {
    String user_agent = "Superuser";
    
    assertTrue("/root/re.txt", bots.query(user_agent, "/root/re.txt"));
  }

  @Test
  public void testAllowedGroup() throws Exception {
    String user_agent = "Allowed";
    
    assertTrue("/root/re.txt", bots.query(user_agent, "/root/re.txt"));
  }

  @Test
  public void testWildGroup() throws Exception {
    String user_agent = "Wild";
    
    assertFalse("/wild/data.txt", bots.query(user_agent, "/wild/data.txt"));
    assertFalse("/wilder/data.txt", bots.query(user_agent, "/wilder/data.txt"));
    assertFalse("/my.gif", bots.query(user_agent, "/my.gif"));
    assertFalse("/root/my.gif", bots.query(user_agent, "/root/my.gif"));
    
    assertTrue("/wildest/data.txt", bots.query(user_agent, "/wildest/data.txt"));
    assertTrue("/root/my.gif/pictures", bots.query(user_agent, "/root/my.gif/pictures"));
  }

  @Test
  public void testGoo1() throws Exception {
    String user_agent = "Goo1";
    
    assertFalse("/fish", bots.query(user_agent, "/fish"));
    assertFalse("/fish.html", bots.query(user_agent, "/fish.html"));
    assertFalse("/fish/salmon.html", bots.query(user_agent, "/fish/salmon.html"));
    assertFalse("/fishheads", bots.query(user_agent, "/fishheads"));
    assertFalse("/fishheads/yummy.html", bots.query(user_agent, "/fishheads/yummy.html"));
    assertFalse("/fish.php?id=anything", bots.query(user_agent, "/fish.php?id=anything"));

    assertTrue("/Fish.asp", bots.query(user_agent, "/Fish.asp"));
    assertTrue("/catfish", bots.query(user_agent, "/catfish"));
    assertTrue("/?id=fish", bots.query(user_agent, "/?id=fish"));
  }

  @Test
  public void testGoo2() throws Exception {
    String user_agent = "Goo2";
    
    assertFalse("/fish", bots.query(user_agent, "/fish"));
    assertFalse("/fish.html", bots.query(user_agent, "/fish.html"));
    assertFalse("/fish/salmon.html", bots.query(user_agent, "/fish/salmon.html"));
    assertFalse("/fishheads", bots.query(user_agent, "/fishheads"));
    assertFalse("/fishheads/yummy.html", bots.query(user_agent, "/fishheads/yummy.html"));
    assertFalse("/fish.php?id=anything", bots.query(user_agent, "/fish.php?id=anything"));

    assertTrue("/Fish.asp", bots.query(user_agent, "/Fish.asp"));
    assertTrue("/catfish", bots.query(user_agent, "/catfish"));
    assertTrue("/?id=fish", bots.query(user_agent, "/?id=fish"));
  }

  @Test
  public void testGoo3() throws Exception {
    String user_agent = "Goo3";
    
    assertFalse("/fish/", bots.query(user_agent, "/fish/"));
    assertFalse("/fish/?id=anything", bots.query(user_agent, "/fish/?id=anything"));
    assertFalse("/fish/salmon.html", bots.query(user_agent, "/fish/salmon.html"));

    assertTrue("/fish", bots.query(user_agent, "/fish"));
    assertTrue("/fish", bots.query(user_agent, "/fish.html"));
    assertTrue("/?id=fish", bots.query(user_agent, "/?id=fish"));
  }

  @Test
  public void testGoo4() throws Exception {
    String user_agent = "Goo4";
    
    assertFalse("/filename.php", bots.query(user_agent, "/filename.php"));
    assertFalse("/folder/filename.php", bots.query(user_agent, "/folder/filename.php"));
    assertFalse("/folder/filename.php?parameter", bots.query(user_agent, "/folder/filename.php?parameter"));
    assertFalse("/folder/any.php.file.html", bots.query(user_agent, "/folder/any.php.file.html"));
    assertFalse("/folder/any.php.file.html/filename.php", bots.query(user_agent, "/folder/any.php.file.html/filename.php"));
    
    assertTrue("/windows.PHP", bots.query(user_agent, "/windows.PHP"));
  }

  @Test
  public void testGoo5() throws Exception {
    String user_agent = "Goo5";
    
    assertFalse("/filename.php", bots.query(user_agent, "/filename.php"));
    assertFalse("/folder/filename.php", bots.query(user_agent, "/folder/filename.php"));
    
    assertTrue("/filename.php?parameter", bots.query(user_agent, "/filename.php?parameter"));
    assertTrue("/filename.php/", bots.query(user_agent, "/filename.php/"));
    assertTrue("/folder/filename.php?parameter", bots.query(user_agent, "/folder/filename.php?parameter"));
    assertTrue("/filename.php/", bots.query(user_agent, "/filename.php/"));
    assertTrue("/filename.php5", bots.query(user_agent, "/filename.php5"));
    assertTrue("/windows.PHP", bots.query(user_agent, "/windows.PHP"));
  }

  @Test
  public void testGoo6() throws Exception {
    String user_agent = "Goo6";
    
    assertFalse("/fish.php", bots.query(user_agent, "/fish.php"));
    assertFalse("/fishheads/catfish.php?parameters", bots.query(user_agent, "/fishheads/catfish.php?parameters"));
    
    assertTrue("/Fish.PHP", bots.query(user_agent, "/Fish.PHP"));
  }
  
  @Test
  public void testGetDisallowList() throws Exception {
    String user_agent = null;
    
    List<String> disallowList = bots.getDisallowList(user_agent);
    
    assertTrue("disallowList.size()==1",disallowList.size()==1);
    assertTrue("disallowList contains /root/",disallowList.contains("/root/"));
  }
}

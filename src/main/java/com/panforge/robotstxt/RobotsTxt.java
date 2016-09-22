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

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Represents access policy from a single "robots.txt" file.
 * <p>
 * Use {@link RobotsTxt#read(java.net.URL)} to read and parse robots.txt.
 */
public interface RobotsTxt {

  /**
   * Checks access to the given HTTP path.
   * @param userAgent user agent to be used evaluate authorization
   * @param path path to access
   * @return <code>true</code> if there is an access to the requested path
   */
  boolean checkAccess(String userAgent, String path);

  /**
   * Gets crawl delay.
   * @return crawl delay in seconds or <code>0</code> if no delay declared
   */
  Integer getCrawlDelay();

  /**
   * Gets host.
   * @return host or <code>null</code> if no host declared
   */
  String getHost();

  /**
   * Gets site maps.
   * @return list of site map URL's.
   */
  List<String> getSitemaps();
  
  /**
   * Reads robots.txt available at the URL.
   * @param url URL of the robots.txt
   * @return parsed robots.txt object
   * @throws URISyntaxException if invalid URL
   * @throws IOException if file unaccessible.
   */
  static RobotsTxt read(URL url) throws URISyntaxException, IOException {
    HttpGet get = new HttpGet(url.toURI());
    try (CloseableHttpClient http = HttpClients.createDefault(); CloseableHttpResponse response = http.execute(get); InputStream input = response.getEntity().getContent()) {
      RobotsTxtReader reader = new RobotsTxtReader();
      return reader.readRobotsTxt(input);
    }
  }
}

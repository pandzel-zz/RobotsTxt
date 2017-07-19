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
import java.util.List;

/**
 * Represents access policy from a single "robots.txt" file.
 * <p>
 * Use {@link RobotsTxt#read(java.io.InputStream)} to read and parse robots.txt.
 */
public interface RobotsTxt {

  /**
   * Checks access to the given HTTP path.
   * @param userAgent user agent to be used evaluate authorization
   * @param path path to access
   * @return <code>true</code> if there is an access to the requested path
   */
  boolean query(String userAgent, String path);

  /**
   * Gets crawl delay.
   * @return crawl delay in seconds or <code>0</code> if no delay declared
   * @deprecated 
   */
  @Deprecated
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
   * Gets a list of disallowed resources.
   * @param userAgent user agent
   * @return list of disallowed resources
   */
  List<String> getDisallowList(String userAgent);
  
  /**
   * Reads robots.txt available at the URL.
   * @param input stream of content
   * @return parsed robots.txt object
   * @throws IOException if unable to read content.
   */
  static RobotsTxt read(InputStream input) throws IOException {
      RobotsTxtReader reader = new RobotsTxtReader();
      return reader.readRobotsTxt(input);
  }
}

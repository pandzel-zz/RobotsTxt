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

import java.util.List;

/**
 * Grant.
 */
public interface Grant {
  
  /**
   * Checks if there is an access granted.
   * @return <code>true</code> if access granted
   */
  boolean hasAccess();
  
  /**
   * Gets clause being engaged.
   * @return clause
   */
  String getClause();
  
  /**
   * Gets user agents from the group.
   * @return user agents
   */
  List<String> getUserAgents();
  
  /**
   * Gets crawl delay from the group.
   * @return crawl delay or <code>null</code> if no crawl delay defined
   */
  Integer getCrawlDelay();
}

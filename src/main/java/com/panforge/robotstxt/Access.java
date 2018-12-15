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
 * Access.
 */
class Access implements Grant {
  private final Group group;
  private final String source;
  private final String clause;
  private final boolean accessAllowed;

  /**
   * Creates instance of the access.
   * @param group group
   * @param source source of the information
   * @param clause access path
   * @param accessAllowed access to the path
   */
  public Access(Group group, String source, String clause, boolean accessAllowed) {
    this.group = group;
    this.source = source;
    this.clause = clause;
    this.accessAllowed = accessAllowed;
  }

  @Override
  public String getClause() {
    return clause;
  }
  
  @Override
  public boolean hasAccess() {
    return accessAllowed;
  }

  @Override
  public Integer getCrawlDelay() {
    return group.getCrawlDelay();
  }

  @Override
  public List<String> getUserAgents() {
    return group.getUserAgents();
  }
  
  /**
   * Checks if path matches access path
   * @param path path to check
   * @param matchingStrategy matcher
   * @return <code>true</code> if path matches access path
   */
  public boolean matches(String path, MatchingStrategy matchingStrategy) {
    return path!=null && matchingStrategy.matches(clause, path);
  }
  
  @Override
  public String toString() {
    return source;
  }
  
}

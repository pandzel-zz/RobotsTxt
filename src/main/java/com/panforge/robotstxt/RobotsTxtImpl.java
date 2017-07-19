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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Robots TXT implementation.
 */
class RobotsTxtImpl implements RobotsTxt {

  private Group defaultSection;
  private final List<Group> groups = new ArrayList<>();

  private Integer crawlDelay;
  private String host;
  private final List<String> sitemaps = new ArrayList<>();

  private final MatchingStrategy matchingStrategy;
  private final WinningStrategy winningStrategy;

  /**
   * Creates instance of the RobotsTxt implementation
   *
   * @param userAgent user agent
   * @param winningStrategy winning strategy
   */
  public RobotsTxtImpl(MatchingStrategy matchingStrategy, WinningStrategy winningStrategy) {
    this.matchingStrategy = matchingStrategy;
    this.winningStrategy = winningStrategy;
  }

  @Override
  public String getHost() {
    return host;
  }

  /**
   * Sets host.
   *
   * @param host host name
   */
  public void setHost(String host) {
    this.host = host;
  }

  @Override
  public List<String> getSitemaps() {
    return sitemaps;
  }

  /**
   * Sets crawl delay.
   *
   * @param crawlDelay crawl delay.
   * @deprecated 
   */
  @Deprecated
  public void setCrawlDelay(Integer crawlDelay) {
    this.crawlDelay = crawlDelay;
  }

  @Override
  public Integer getCrawlDelay() {
    return crawlDelay;
  }

  @Override
  public List<String> getDisallowList(String userAgent) {
    Group sec = findSectionByAgent(groups, userAgent);
    if (sec == null) {
      sec = defaultSection;
    }
    return sec != null
            ? sec.getAccessList().listAll().stream()
                    .filter(acc -> !acc.hasAccess())
                    .map(acc -> acc.getPath())
                    .collect(Collectors.toList())
            : Collections.emptyList();
  }

  @Override
  public boolean query(String userAgent, String path) {
    List<Access> select = select(userAgent, path);
    Access winner = winningStrategy.selectWinner(select);
    return winner != null ? winner.hasAccess() : true;
  }

  /**
   * Adds section.
   *
   * @param section section
   */
  public void addGroup(Group section) {
    if (section != null) {
      if (section.isAnyAgent()) {
        if (this.defaultSection == null) {
          this.defaultSection = section;
        } else {
          this.defaultSection.getAccessList().importAccess(section.getAccessList());
        }
      } else {
        Group exact = findExactSection(section);
        if (exact == null) {
          groups.add(section);
        } else {
          exact.getAccessList().importAccess(section.getAccessList());
        }
      }
    }
  }

  @Override
  public String toString() {
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);

    if (defaultSection != null) {
      pw.println(defaultSection);
    }

    groups.forEach(group -> {
      pw.println(group);
    });

    if (host != null) {
      pw.format("Host: %s", host).println();
    }

    sitemaps.forEach(sitemap -> pw.format("Sitemap: %s", sitemap).println());

    pw.flush();

    return sw.toString();
  }

  /**
   * Finds exact section.
   *
   * @param section section to find exact
   * @return exact section or {@code null} if no exact section found
   */
  private Group findExactSection(Group section) {
    for (Group s : groups) {
      if (s.isExact(section)) {
        return s;
      }
    }
    return null;
  }

  private List<Access> select(String userAgent, String path) {
    String relativePath = assureRelative(path);

    if (relativePath != null && !"/robots.txt".equalsIgnoreCase(relativePath)) {
      ArrayList<Access> selected = new ArrayList<>();

      Group sec = findSectionByAgent(groups, userAgent);
      if (sec != null) {
        selected.addAll(sec.select(userAgent, relativePath, matchingStrategy));
      } else if (defaultSection != null) {
        selected.addAll(defaultSection.select(userAgent, relativePath, matchingStrategy));
      }
      return selected;
    } else {
      return Collections.EMPTY_LIST;
    }
  }

  private Group findSectionByAgent(List<Group> sections, String userAgent) {
    for (Group sec : sections) {
      if (sec.matchUserAgent(userAgent)) {
        return sec;
      }
    }
    return null;
  }

  private String assureRelative(String path) {
    try {
      URI uri = new URI(path);
      if (uri.isAbsolute()) {
        URL url = uri.toURL();
        path = String.format("/%s%s%s", url.getPath(), url.getQuery() != null ? "#" + url.getQuery() : "", url.getRef() != null ? "#" + url.getRef() : "").replaceAll("/+", "/");
      }
      return path;
    } catch (Exception ex) {
      return path;
    }
  }
}

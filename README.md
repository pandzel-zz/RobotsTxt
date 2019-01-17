[![Build Status](https://travis-ci.org/pandzel/RobotsTxt.png?branch=master)](https://travis-ci.org/pandzel/RobotsTxt)
[![Maven Central](https://img.shields.io/maven-central/v/com.panforge/robots.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.panforge%22%20AND%20a:%22robots%22)

# RobotsTxt
Java library to read, parse and query robots.txt file.

## Instructions

Building the source code:

* Run 'mvn clean install'

Using in your own project:

* Add dependency to the pom.xml

```xml
  <dependencies>
  ...
    <dependency>
        <groupId>com.panforge</groupId>
        <artifactId>robots</artifactId>
        <version>1.4.0</version>
    </dependency>
  ...
  </dependencies>
```

* Load robots.txt

```java
import java.io.InputStream;
import com.panforge.robotstxt.RobotsTxt;

...

try (InputStream robotsTxtStream = <obtain input stream with robots.txt content>;) {
  RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);
}
```

for example:

```java
try (InputStream robotsTxtStream = new URL("https://github.com/robots.txt").openStream()) {
  RobotsTxt robotsTxt = RobotsTxt.read(robotsTxtStream);
}
```

> Please, note that the code snippet above is just an example meant how to feed parser with the stream of data. It is highly recommended
to use a roboust a HTTP client implementation (like [Apache Commons HTTP Client](https://hc.apache.org/httpcomponents-client-ga/)) for better handling various aspects of HTTP protocol communication instead of rudimentary Java URL() class. 

> It is recomended to use companion module: [RobotsTxt-HTTP](https://github.com/pandzel/RobotsTxt-HTTP).

* Check if robotsTxt allows for access to the resource:

```java
boolean hasAccess = robotsTxt.query(<User-agent name>,<HTTP path to the resource>);
```

for example:

```java
boolean hasAccess = robotsTxt.query(null,"/humans.txt");
```

* Ask for more information:

```java
Grant grant = robotsTxt.ask(null,"/humans.txt");
if (grant == null || grant.hasAccess()) {
  // do something
}
if (grant != null && grant.getCrawlDelay()!=null) {
  // wait till next time
}
```

## Requirements

* Java JDK 1.8 or higher

## Licensing
Copyright 2016 Piotr Andzel

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

A copy of the license is available in the repository's [LICENSE](LICENSE.txt) file.

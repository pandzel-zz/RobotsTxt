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

import static com.panforge.robotstxt.URLDecoder.decode;
import static com.panforge.robotstxt.WildcardsCompiler.compile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matching strategy.
 * <p>
 * It determines how and if path is matching a pattern.
 * @see WinningStrategy
 */
interface MatchingStrategy {
  /**
   * Matches given path with a pattern.
   * @param pattern pattern
   * @param pathToTest path to test
   * @return <code>true</code> if match
   */
  boolean matches(String pattern, String pathToTest);
  
  /**
   * This strategy recognizes (*) and ($) as wildcards.
   */
  MatchingStrategy DEFAULT = (pattern,pathToTest)->{
    if (pathToTest==null) return false;
    if (pattern==null || pattern.isEmpty()) return true;
    
    String relativePath = decode(pathToTest);
    /*
    if (pattern.endsWith("/") && !relativePath.endsWith("/")) {
      relativePath += "/";
    }
    */
    Pattern pt = compile(pattern);
    Matcher matcher = pt.matcher(relativePath);
    return matcher.find() && matcher.start()==0;
  };
}

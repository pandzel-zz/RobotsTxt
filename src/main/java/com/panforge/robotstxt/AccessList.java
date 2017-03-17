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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Access list.
 */
class AccessList {
  private final List<Access> accessList = new ArrayList<Access>();

  /**
   * Adds access to the list.
   * @param access access
   */
  public void addAccess(Access access) {
    accessList.add(access);
  }
  
  /**
   * Imports entire access list from another instance.
   * @param ref another instance
   */
  public void importAccess(AccessList ref) {
    accessList.addAll(ref.accessList);
  }
  
  @Override
  public String toString() {
    return accessList.stream().map(Object::toString).collect(Collectors.joining("\n"));
  }
  
  /**
   * Select any access matching input path.
   * @param relativePath path to test
   * @param matchingStrategy matcher
   * @return list of matching elements
   */
  public List<Access> select(String relativePath, MatchingStrategy matchingStrategy) {
    ArrayList<Access> allMatching = new ArrayList<Access>();
    
    if (relativePath!=null) {
      for (Access acc: accessList) {
        if (acc.matches(relativePath, matchingStrategy)) {
          allMatching.add(acc);
        }
      }
    }
    
    return allMatching;
  }
  
  /**
   * Lists all accesses.
   * @return list of all accesses
   */
  public List<Access> listAll() {
    return accessList;
  }
}

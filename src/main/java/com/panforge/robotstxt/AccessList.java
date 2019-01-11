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

import com.panforge.robotstxt.exception.MatchingTimeoutException;
import com.panforge.robotstxt.exception.SelectionException;

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
   * @throws SelectionException when we are unable to select with certainty.
   * i.e. when we have successfully determined that most of the access clauses don't match the path but,
   * matching for some for the access clauses timed out
   * (we were not able to determine in a specified time frame - possible regex DOS attack.), so
   * we throw a selection exception to inform the caller that the selection process had hiccups
   * as some of the access clauses could not evaluated.
   * if all the access clauses are evaluated successfully and still no selection is available, then we do-not throw selectionException,
   * as all clauses were evaluated.
   * if some of the access clauses are evaluated successfully and some timeout, but selection is available, then we do-not throw selectionException,
   * as a selection is available.
   */
  public List<Access> select(String relativePath, MatchingStrategy matchingStrategy) throws SelectionException {
    ArrayList<Access> allMatching = new ArrayList<Access>();

    Exception latestMatchingTimeoutException = null;
    String latestTimedOutAccessClause = null;
    if (relativePath!=null) {
      for (Access acc: accessList) {
        try {
          if (acc.matches(relativePath, matchingStrategy)) {
            allMatching.add(acc);
          }
        } catch (MatchingTimeoutException e) {
          latestMatchingTimeoutException = e;
          latestTimedOutAccessClause = acc.getClause();
        }
      }
      //if timeout occurs in matching but some other matches have succeeded, then go ahead with what you have got. else .. throw exception
      if(allMatching.size() == 0 && latestMatchingTimeoutException != null){
        //since no match was possible at all & we detected timeout atleast once i.e. unable to determine a match. lets tell the caller about this.
        throw new SelectionException("All Access clauses could not be evaluated & no access paths! We could not decide for some access paths because the matching timed out. Latest Timed Out Access Clause: " + latestTimedOutAccessClause, latestMatchingTimeoutException);
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

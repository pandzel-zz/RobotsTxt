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
 * Winning strategy.
 * <p>
 * Determines how a winner will be selected.
 * @see MatchingStrategy
 */
interface WinningStrategy {
  
  /**
   * Selects a single winner amongst the candidates.
   * @param candidates list of candidates
   * @return a winner or {@code null} if no winner (for example: because empty list of candidates)
   */
  Access selectWinner(List<Access> candidates);
  
  WinningStrategy DEFAULT  = (candidates)->{
    Access winningDisallow = candidates.stream().filter(acc->acc.hasAccess()==false).sorted((l,r)->r.getPath().length()-l.getPath().length()).findFirst().orElse(null);
    Access winningAllow = candidates.stream().filter(acc->acc.hasAccess()==true).sorted((l,r)->r.getPath().length()-l.getPath().length()).findFirst().orElse(null);

    if (winningAllow!=null && winningAllow.getPath().length()>=(winningDisallow!=null? winningDisallow.getPath().length(): 0)) {
      return winningAllow;
    }

    if (winningDisallow!=null) {
      return winningDisallow;
    }
    
    return null;
  };
}

/* Licensed under the Apache License, Version 2.0 (the "License");
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

package org.activiti5.standalone.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.impl.util.DefaultClockImpl;
import org.activiti.engine.runtime.Clock;
import org.activiti5.engine.impl.calendar.DurationBusinessCalendar;
import org.activiti5.engine.impl.test.PvmTestCase;

/**
 * @author Tom Baeyens
 */
public class DurationBusinessCalendarTest extends PvmTestCase {

  public void testSimpleDuration() throws Exception {
    Clock testingClock = new DefaultClockImpl();
    DurationBusinessCalendar businessCalendar = new DurationBusinessCalendar(testingClock);

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy MM dd - HH:mm");
    Date now = simpleDateFormat.parse("2010 06 11 - 17:23");
    testingClock.setCurrentTime(now);

    Date duedate = businessCalendar.resolveDuedate("P2DT5H70M");

    Date expectedDuedate = simpleDateFormat.parse("2010 06 13 - 23:33");

    assertEquals(expectedDuedate, duedate);
  }

}

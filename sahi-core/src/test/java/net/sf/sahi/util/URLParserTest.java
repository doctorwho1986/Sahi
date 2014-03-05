package net.sf.sahi.util;

import net.sf.sahi.command.Command;
import net.sf.sahi.config.Configuration;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Sahi - Web Automation and Test Tool
 * <p/>
 * Copyright  2006  V Narayan Raman
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
public class URLParserTest{
  final String uri = "/_s_/dyn/Log_highlight/sahi_demo_include.sah?n=2";

  @Before
  public void setUp() {
    Configuration.init();
  }

  @Test
  public void xtestScriptFileNamefromURI() {
    assertEquals( Configuration.getUserDataDir() + "/scripts/sahi_demo_include.sah", URLParser.scriptFileNamefromURI(uri, "/Log_highlight/"));
  }

  @Test
  public void xtestScriptFileNamefromURI2() {
    final String uri2 = "/_s_/dyn/scripts/sahi_demo_include.sah?n=2";
    assertEquals(Configuration.getUserDataDir() + "/scripts/sahi_demo_include.sah", URLParser.scriptFileNamefromURI(uri2, "/scripts/"));
  }

  @Test
  public void testLogFileNamefromURI() {
    assertEquals("", URLParser.logFileNamefromURI("/_s_/" + Command.LOG_VIEW + "/"));
    assertEquals("", URLParser.logFileNamefromURI("/_s_/" + Command.LOG_VIEW));
    assertEquals("", URLParser.logFileNamefromURI("/_s_/" + Command.LOG_VIEW + "////////"));
  }

  @Test
  public void testFileNameFromURIIgnoresQueryString() {
    assertTrue(URLParser.fileNamefromURI("/_s_/spr/a/b/c/d.eee?fff").replace('\\', '/').endsWith("spr/a/b/c/d.eee"));
  }

  @Test
  public void testGetRelativeLogFile() {
    assertEquals("a/b/c", URLParser.getRelativeLogFile("/_s_/dyn/" + Command.LOG_VIEW + "/a/b/c"));
    assertEquals("a/b/c", URLParser.getRelativeLogFile("/_s_//////////dyn/" + Command.LOG_VIEW + "/a/b/c"));
  }

  @Test
  public void testGetCommandFromUri() {
    assertEquals("Player_currentParsedScript", URLParser.getCommandFromUri("http://www.google.co.in/_s_/dyn/Player_currentParsedScript", "/dyn/"));
    assertEquals("Player_currentParsedScript", URLParser.getCommandFromUri("http://www.google.co.in/_s_/dyn/Player_currentParsedScript?a=b", "/dyn/"));
    assertEquals("Player_currentParsedScript", URLParser.getCommandFromUri("http://www.google.co.in/_s_/dyn/Player_currentParsedScript/xa/b", "/dyn/"));
    assertEquals("Player_currentParsedScript", URLParser.getCommandFromUri("http://www.google.co.in/_s_/dyn/Player_currentParsedScript/", "/dyn/"));
  }

}

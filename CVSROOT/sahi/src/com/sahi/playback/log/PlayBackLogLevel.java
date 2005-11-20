package com.sahi.playback.log;

import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * User: nraman
 * Date: Jun 23, 2005
 * Time: 1:29:48 AM
 */
public class PlayBackLogLevel extends Level {
	private static final long serialVersionUID = 7518716444395222643L;
	public static final Level ERROR = new PlayBackLogLevel("ERROR", Level.SEVERE.intValue()+15);    
    public static final Level FAILURE = new PlayBackLogLevel("FAILURE", Level.SEVERE.intValue()+10);
    public static final Level SUCCESS = new PlayBackLogLevel("SUCCESS", Level.SEVERE.intValue()+5);
	public static final Level INFO2 = new PlayBackLogLevel("INFO", Level.SEVERE.intValue()+5);;
    public PlayBackLogLevel(String name, int value) {
        super(name, value);
    }
}
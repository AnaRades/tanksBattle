package tanks.battle.engine;

import static tanks.battle.utils.Constants.LINE_TERMINATOR;

public class BattleLog {

  private StringBuilder log;
  private StringBuilder latestLog;

  public BattleLog() {
    log = new StringBuilder();
    latestLog = new StringBuilder();
  }

  public void logEvent(String event) {
      latestLog.append(event).append(LINE_TERMINATOR);
      log.append(latestLog).append(LINE_TERMINATOR);
  }

  public String getLog() {
    return log.toString();
  }

  public String getLatestLog() {
    String latest = latestLog.toString();
    latestLog = new StringBuilder();
    return latest;
  }

  public String getLatestLogWithoutClear() {
    return latestLog.toString();
  }
}
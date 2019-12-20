package tanks.battle.models.battle;

public class BattleObserver {

  private StringBuilder log;
  private StringBuilder latestLog;

  public BattleObserver() {
    log = new StringBuilder();
    latestLog = new StringBuilder();
  }

  public void logEvent(String event) {
      latestLog.append(event).append(";");
      log.append(latestLog).append(";");
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

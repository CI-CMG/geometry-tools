package edu.colorado.cires.geometrytools.trackline;

public class ColumnMapping {

  private final String longitudeColumn;
  private final String latitudeColumn;
  private final String timeColumn;
  private final TimeFormat timeFormat;
  private final Delimiter delimiter;

  public ColumnMapping(String longitudeColumn, String latitudeColumn, String timeColumn, TimeFormat timeFormat, Delimiter delimiter) {
    this.longitudeColumn = longitudeColumn;
    this.latitudeColumn = latitudeColumn;
    this.timeColumn = timeColumn;
    this.timeFormat = timeFormat;
    this.delimiter = delimiter;
  }

  public String getLatitudeColumn() {
    return latitudeColumn;
  }

  public String getLongitudeColumn() {
    return longitudeColumn;
  }

  public String getTimeColumn() {
    return timeColumn;
  }

  public TimeFormat getTimeFormat() {
    return timeFormat;
  }

  public Delimiter getDelimiter() {
    return delimiter;
  }
}

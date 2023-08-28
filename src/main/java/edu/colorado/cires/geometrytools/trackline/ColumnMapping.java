package edu.colorado.cires.geometrytools.trackline;

import java.util.Objects;

public class ColumnMapping {

  private final String longitudeColumn;
  private final String latitudeColumn;
  private final String timeColumn;
  private final TimeFormat timeFormat;
  private final Delimiter delimiter;

  /**
   * Creates a new ColumnMapping object to store user preferences for geometry generation
   *
   * @param longitudeColumn The column name storing longitude data
   * @param latitudeColumn The column name storing latitude data
   * @param timeColumn The column name storing time data
   * @param timeFormat The format of the time data
   * @param delimiter The delimiter used in the data file
   */
  public ColumnMapping(String longitudeColumn, String latitudeColumn, String timeColumn, TimeFormat timeFormat, Delimiter delimiter) {
    this.longitudeColumn = longitudeColumn;
    this.latitudeColumn = latitudeColumn;
    this.timeColumn = timeColumn;
    this.timeFormat = timeFormat;
    this.delimiter = delimiter;
  }

  /**
   *
   * @return The column name storing longitude data
   */
  public String getLatitudeColumn() {
    return latitudeColumn;
  }

  /**
   *
   * @return The column name storing latitude data
   */
  public String getLongitudeColumn() {
    return longitudeColumn;
  }

  /**
   *
   * @return The column name storing time data
   */
  public String getTimeColumn() {
    return timeColumn;
  }

  /**
   *
   * @return The format of the time data
   */
  public TimeFormat getTimeFormat() {
    return timeFormat;
  }

  /**
   *
   * @return The delimiter used in the data file
   */
  public Delimiter getDelimiter() {
    return delimiter;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ColumnMapping that = (ColumnMapping) o;
    return Objects.equals(longitudeColumn, that.longitudeColumn) && Objects.equals(latitudeColumn, that.latitudeColumn)
        && Objects.equals(timeColumn, that.timeColumn) && timeFormat == that.timeFormat && delimiter == that.delimiter;
  }

  @Override
  public int hashCode() {
    return Objects.hash(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter);
  }

  @Override
  public String toString() {
    return "ColumnMapping{" +
        "longitudeColumn='" + longitudeColumn + '\'' +
        ", latitudeColumn='" + latitudeColumn + '\'' +
        ", timeColumn='" + timeColumn + '\'' +
        ", timeFormat=" + timeFormat +
        ", delimiter=" + delimiter +
        '}';
  }
}

package edu.colorado.cires.geometrytools.trackline;

import edu.colorado.cires.cmg.tracklinegen.DataRow;
import java.time.Instant;
import java.util.Objects;

public class DataPoint implements DataRow, Comparable<DataPoint> {

  private final double lon;
  private final double lat;
  private final Instant timestamp;

  public DataPoint(double lon, double lat, Instant timestamp) {
    this.lon = lon;
    this.lat = lat;
    this.timestamp = timestamp;
  }

  public Double getLat() {
    return lat;
  }

  public Double getLon() {
    return lon;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataPoint dataPoint = (DataPoint) o;
    return Double.compare(dataPoint.lat, lat) == 0 && Double.compare(dataPoint.lon, lon) == 0 && Objects.equals(timestamp,
        dataPoint.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(lat, lon, timestamp);
  }

  @Override
  public String toString() {
    return "DataPoint{" +
        "lat=" + lat +
        ", lon=" + lon +
        ", timestamp=" + timestamp +
        '}';
  }

  @Override
  public int compareTo(DataPoint o) {
    if (!timestamp.equals(o.timestamp)) {
      return timestamp.compareTo(o.timestamp);
    }
    if(lat != o.lat) {
      return Double.compare(lat, o.lat);
    }
    return Double.compare(lon, o.lon);
  }
}
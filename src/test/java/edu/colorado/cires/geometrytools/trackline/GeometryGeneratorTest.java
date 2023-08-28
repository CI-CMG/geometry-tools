package edu.colorado.cires.geometrytools.trackline;

import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.colorado.cires.geometrytools.OutputType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

public class GeometryGeneratorTest {

  @Test
  public void testWkt() throws Exception {
    Path input = Paths.get("src/test/resources/data.xyz");
    Path output = Paths.get("target/output/data.wkt");
    FileUtils.deleteQuietly(output.toFile());
    OutputType outputType = OutputType.wkt;
    double simplificationTolerance = 0.001;

    long splitGeometryMs = 900000L;
    int precision = 5;
    double maxAllowedSpeedKnts = 60;
    String latitudeColumn = "LATITUDE";
    String longitudeColumn = "LONGITUDE";
    String timeColumn = "TIME_EPOCH_MILLIS";
    TimeFormat timeFormat = TimeFormat.epoch_millis;
    Delimiter delimiter = Delimiter.comma;

    GeometryGenerator geometryGenerator = new GeometryGenerator(
        simplificationTolerance,
        splitGeometryMs,
        precision,
        maxAllowedSpeedKnts,
        new ColumnMapping(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter));
    geometryGenerator.generateGeometryFile(input, output, outputType);

    assertTrue(Files.exists(output));
  }

  @Test
  public void testGeoJson() throws Exception {
    Path input = Paths.get("src/test/resources/data.xyz");
    Path output = Paths.get("target/output/data.json");
    FileUtils.deleteQuietly(output.toFile());
    OutputType outputType = OutputType.geojson;
    double simplificationTolerance = 0.001;

    long splitGeometryMs = 900000L;
    int precision = 5;
    double maxAllowedSpeedKnts = 60;
    String latitudeColumn = "LATITUDE";
    String longitudeColumn = "LONGITUDE";
    String timeColumn = "TIME_EPOCH_MILLIS";
    TimeFormat timeFormat = TimeFormat.epoch_millis;
    Delimiter delimiter = Delimiter.comma;

    GeometryGenerator geometryGenerator = new GeometryGenerator(
        simplificationTolerance,
        splitGeometryMs,
        precision,
        maxAllowedSpeedKnts,
        new ColumnMapping(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter));
    geometryGenerator.generateGeometryFile(input, output, outputType);

    assertTrue(Files.exists(output));
  }

  @Test
  public void testIsoTime() throws Exception {
    Path input = Paths.get("src/test/resources/data2.xyz");
    Path output = Paths.get("target/output/data.wkt");
    FileUtils.deleteQuietly(output.toFile());
    OutputType outputType = OutputType.wkt;
    double simplificationTolerance = 0.001;

    long splitGeometryMs = -1L;
    int precision = 5;
    double maxAllowedSpeedKnts = -1;
    String latitudeColumn = "LAT";
    String longitudeColumn = "LON";
    String timeColumn = "TIME";
    TimeFormat timeFormat = TimeFormat.iso_utc;
    Delimiter delimiter = Delimiter.comma;

    GeometryGenerator geometryGenerator = new GeometryGenerator(
        simplificationTolerance,
        splitGeometryMs,
        precision,
        maxAllowedSpeedKnts,
        new ColumnMapping(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter));
    geometryGenerator.generateGeometryFile(input, output, outputType);

    assertTrue(Files.exists(output));
  }

  @Test
  public void testTsv() throws Exception {
    Path input = Paths.get("src/test/resources/data3.tsv");
    Path output = Paths.get("target/output/data.wkt");
    FileUtils.deleteQuietly(output.toFile());
    OutputType outputType = OutputType.wkt;
    double simplificationTolerance = 0.001;

    long splitGeometryMs = -1L;
    int precision = 5;
    double maxAllowedSpeedKnts = -1;
    String latitudeColumn = "LAT";
    String longitudeColumn = "LON";
    String timeColumn = "TIME";
    TimeFormat timeFormat = TimeFormat.iso_utc;
    Delimiter delimiter = Delimiter.tab;

    GeometryGenerator geometryGenerator = new GeometryGenerator(
        simplificationTolerance,
        splitGeometryMs,
        precision,
        maxAllowedSpeedKnts,
        new ColumnMapping(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter));
    geometryGenerator.generateGeometryFile(input, output, outputType);

    assertTrue(Files.exists(output));
  }

  @Test
  public void testSecondsTime() throws Exception {
    Path input = Paths.get("src/test/resources/data4.xyz");
    Path output = Paths.get("target/output/data.wkt");
    FileUtils.deleteQuietly(output.toFile());
    OutputType outputType = OutputType.wkt;
    double simplificationTolerance = 0.001;

    long splitGeometryMs = 900000L;
    int precision = 5;
    double maxAllowedSpeedKnts = 60;
    String latitudeColumn = "LATITUDE";
    String longitudeColumn = "LONGITUDE";
    String timeColumn = "TIME_EPOCH_MILLIS";
    TimeFormat timeFormat = TimeFormat.epoch_seconds;
    Delimiter delimiter = Delimiter.comma;

    GeometryGenerator geometryGenerator = new GeometryGenerator(
        simplificationTolerance,
        splitGeometryMs,
        precision,
        maxAllowedSpeedKnts,
        new ColumnMapping(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter));
    geometryGenerator.generateGeometryFile(input, output, outputType);

    assertTrue(Files.exists(output));
  }

}
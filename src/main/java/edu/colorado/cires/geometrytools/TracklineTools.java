package edu.colorado.cires.geometrytools;

import edu.colorado.cires.geometrytools.trackline.ColumnMapping;
import edu.colorado.cires.geometrytools.trackline.Delimiter;
import edu.colorado.cires.geometrytools.trackline.GeometryGenerator;
import edu.colorado.cires.geometrytools.trackline.TimeFormat;
import java.nio.file.Path;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(
    name = "trackline",
    description = "trackline generation",
    mixinStandardHelpOptions = true
)
public class TracklineTools implements Runnable {

  @Option(names = {"-i", "--input"}, required = true, description = "The file containing the source data")
  private Path input;

  @Option(names = {"-o", "--output"}, required = true, description = "The file to write the output")
  private Path output;

  @Option(names = {"-ot", "--output-type"}, description = "The type of output data. Allowed values: geojson, wkt. Default: wkt", defaultValue = "wkt")
  private OutputType outputType = OutputType.wkt;

  @Option(
      names = {"-tol", "--simplification-tolerance"},
      description = "The DP simplification tolerance. Default: 0.001",
      defaultValue = "0.001"
  )
  private double simplificationTolerance = 0.001;

  @Option(names = "--split", negatable = true, description = "Enable splitting based on time gaps. Default: enabled", fallbackValue = "true")
  private boolean enableSplit = true;

  @Option(names = {"-split-ms",
      "--split-ms"}, description = "The minimum amount of time in milliseconds between points to create a split. Default: 900000", defaultValue = "900000")
  private long splitGeometryMs = 900000L;

  @Option(names = {"-p", "--precision"}, description = "The latitude and longitude precision to use. Default: 5", defaultValue = "5")
  private int precision = 5;

  @Option(names = "--speed", negatable = true, description = "Enable maximum speed check. Default: enabled", fallbackValue = "true")
  private boolean enableSpeedCheck = true;

  @Option(names = {"-speed-knts", "--speed-knts"}, description = "The maximum allowed speed in knots. Default: 60", defaultValue = "60")
  private double maxAllowedSpeedKnts = 60;

  @Option(names = {"-lat", "--latitude-column"}, description = "The latitude column. Default: LATITUDE", defaultValue = "LATITUDE")
  private String latitudeColumn = "LATITUDE";

  @Option(names = {"-lon", "--longitude-column"}, description = "The longitude column. Default: LONGITUDE", defaultValue = "LONGITUDE")
  private String longitudeColumn = "LONGITUDE";

  @Option(names = {"-time", "--time-column"}, description = "The time column. Default: TIME", defaultValue = "TIME")
  private String timeColumn = "TIME";

  @Option(names = {"-time-format", "--time-format"}, description = "The time format. Allowed: iso_utc, epoch_millis, epoch_seconds", defaultValue = "iso_utc")
  private TimeFormat timeFormat = TimeFormat.iso_utc;

  @Option(names = {"-del", "--delimiter"}, description = "The file delimiter. Default 'comma'. Allowed values: comma, tab", defaultValue = "comma")
  private Delimiter delimiter = Delimiter.comma;


  @Override
  public void run() {

    new GeometryGenerator(
        simplificationTolerance,
        enableSplit ? splitGeometryMs : -1,
        precision,
        enableSpeedCheck ? maxAllowedSpeedKnts : -1,
        new ColumnMapping(longitudeColumn, latitudeColumn, timeColumn, timeFormat, delimiter))
        .generateGeometryFile(input, output, outputType);
  }
}

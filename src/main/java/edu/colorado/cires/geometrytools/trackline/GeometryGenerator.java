package edu.colorado.cires.geometrytools.trackline;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import edu.colorado.cires.cmg.iostream.Pipe;
import edu.colorado.cires.cmg.tracklinegen.GeoJsonMultiLineProcessor;
import edu.colorado.cires.cmg.tracklinegen.GeometrySimplifier;
import edu.colorado.cires.cmg.tracklinegen.ValidationException;
import edu.colorado.cires.geometrytools.OutputType;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;
import org.apache.commons.io.output.NullOutputStream;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryGenerator {

  private static final int BATCH_SIZE = 5000;
  private static final long MAX_POINTS = -1;
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper()
      .setSerializationInclusion(Include.NON_EMPTY)
      .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
      .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
      .disable(MapperFeature.DEFAULT_VIEW_INCLUSION)
      .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
      .registerModule(new JavaTimeModule())
      .registerModule(DoubleSerializer.DOUBLE_SERIALIZER_MODULE);
  private final GeometryFactory GEOMETRY_FACTORY = new GeometryFactory(new PrecisionModel(), 4326);

  //  [Decimal / Places / Degrees	/ Object that can be recognized at scale / N/S or E/W at equator, E/W at 23N/S, E/W at 45N/S, E/W at 67N/S]
  //  0   1.0	        1° 00′ 0″	        country or large region                             111.32 km	  102.47 km	  78.71 km	43.496 km
  //  1	  0.1	        0° 06′ 0″         large city or district                              11.132 km	  10.247 km	  7.871 km	4.3496 km
  //  2	  0.01	      0° 00′ 36″        town or village                                     1.1132 km	  1.0247 km	  787.1 m	  434.96 m
  //  3	  0.001	      0° 00′ 3.6″       neighborhood, street                                111.32 m	  102.47 m	  78.71 m	  43.496 m
  //  4	  0.0001	    0° 00′ 0.36″      individual street, land parcel                      11.132 m	  10.247 m	  7.871 m	  4.3496 m
  //  5	  0.00001	    0° 00′ 0.036″     individual trees, door entrance	                    1.1132 m	  1.0247 m	  787.1 mm	434.96 mm
  //  6	  0.000001	  0° 00′ 0.0036″    individual humans                                   111.32 mm	  102.47 mm	  78.71 mm	43.496 mm
  //  7	  0.0000001	  0° 00′ 0.00036″   practical limit of commercial surveying	            11.132 mm	  10.247 mm	  7.871 mm	4.3496 mm


  private final double simplificationTolerance;
  private final long splitGeometryMs;
  private final int precision;
  private final double maxAllowedSpeedKnts;
  private final ColumnMapping columnMapping;

  public GeometryGenerator(
      double simplificationTolerance,
      long splitGeometryMs,
      int precision,
      double maxAllowedSpeedKnts,
      ColumnMapping columnMapping) {
    this.simplificationTolerance = simplificationTolerance;
    this.splitGeometryMs = splitGeometryMs;
    this.precision = precision;
    this.maxAllowedSpeedKnts = maxAllowedSpeedKnts;
    this.columnMapping = columnMapping;
  }

  private Consumer<OutputStream> phase1(InputStream csvInputStream) {
    return outputStream -> {
      Phase1Processor phase1 = new Phase1Processor(
          splitGeometryMs,
          new GeometrySimplifier(simplificationTolerance),
          BATCH_SIZE,
          precision,
          OBJECT_MAPPER,
          outputStream,
          csvInputStream,
          MAX_POINTS,
          GEOMETRY_FACTORY,
          columnMapping);
      try {
        phase1.process();
      } catch (IOException e) {
        throw new RuntimeException("An error occurred simplifying geometry", e);
      }
    };
  }

  private Consumer<InputStream> phase2(OutputStream geoJsonOutputStream, OutputStream wktOutputStream) {

    return inputStream -> {
      GeoJsonMultiLineProcessor phase2 = new GeoJsonMultiLineProcessor(OBJECT_MAPPER, precision, maxAllowedSpeedKnts);
      try {
        phase2.process(inputStream, geoJsonOutputStream, wktOutputStream);
      } catch (ValidationException e) {
        throw new RuntimeException("An error occurred simplifying geometry", e);
      }
    };
  }

  private void process(InputStream csvInputStream, OutputStream geoJsonOutputStream, OutputStream wktOutputStream) {
    Pipe.pipe(phase1(csvInputStream), phase2(geoJsonOutputStream, wktOutputStream));
  }

  public void generateGeometryFile(Path inputFile, Path outputFile, OutputType outputType) {
    if (outputFile.getParent() != null && !Files.exists(outputFile.getParent())) {
      try {
        Files.createDirectories(outputFile.getParent());
      } catch (IOException e) {
        throw new RuntimeException("Unable to create directory " + outputFile.getParent(), e);
      }
    }
    try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(inputFile));
        OutputStream outputStream = new BufferedOutputStream(Files.newOutputStream(outputFile))
    ) {
      switch (outputType) {
        case wkt:
          process(inputStream, NullOutputStream.INSTANCE, outputStream);
          break;
        case geojson:
          process(inputStream, outputStream, NullOutputStream.INSTANCE);
          break;
        default:
          throw new IllegalStateException("Unsupported output type " + outputType);
      }
    } catch (IOException e) {
      throw new RuntimeException("Unable to process file", e);
    }
  }
}

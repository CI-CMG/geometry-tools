package edu.colorado.cires.geometrytools.trackline;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cmg.tracklinegen.GeoJsonMultiLineWriter;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracklineContext implements Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(TracklineContext.class);

  private final JsonGenerator jsonGenerator;
  private final GeoJsonMultiLineWriter lineWriter;
  private final CSVParser csvParser;

  public TracklineContext(
      int precision,
      ObjectMapper objectMapper,
      OutputStream out,
      InputStream inputStream,
      ColumnMapping columnMapping) {
    try {
      jsonGenerator = objectMapper.getFactory().createGenerator(out);
    } catch (IOException e) {
      throw new RuntimeException("Unable to create JSON generator", e);
    }
    lineWriter = new GeoJsonMultiLineWriter(jsonGenerator, precision);
    try {
      csvParser = new CSVParser(
          new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)),
          getFormat(columnMapping));
    } catch (IOException e) {
      throw new RuntimeException("Unable to open reader", e);
    }
  }

  private static CSVFormat getFormat(ColumnMapping columnMapping) {
    switch (columnMapping.getDelimiter()) {
      case comma:
        return CSVFormat.DEFAULT;
      case tab:
        return CSVFormat.TDF;
      default:
        throw new IllegalStateException("Unsupported delimiter: " + columnMapping.getDelimiter());
    }
  }

  public GeoJsonMultiLineWriter getLineWriter() {
    return lineWriter;
  }

  public CSVParser getCsvParser() {
    return csvParser;
  }

  @Override
  public void close() throws IOException {
    List<Exception> listExceptions = new ArrayList<>();
    try {
      csvParser.close();
    } catch (Exception e) {
      LOGGER.warn("Unable to close reader", e);
      listExceptions.add(e);
    }
    try {
      jsonGenerator.close();
    } catch (Exception e) {
      LOGGER.warn("Unable to close JSON generator", e);
      listExceptions.add(e);
    }

    if(!listExceptions.isEmpty()) {
      throw new IllegalStateException("Trackline context problem", listExceptions.get(0));
    }
  }
}

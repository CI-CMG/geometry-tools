package edu.colorado.cires.geometrytools.trackline;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.colorado.cires.cmg.tracklinegen.GeometrySimplifier;
import edu.colorado.cires.cmg.tracklinegen.TracklineProcessor;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.locationtech.jts.geom.GeometryFactory;

public class Phase1Processor extends TracklineProcessor<TracklineContext, DataPoint, RowListener> {

  private final long msSplit;
  private final GeometrySimplifier geometrySimplifier;
  private final int batchSize;
  private final int precision;
  private final ObjectMapper objectMapper;
  private final OutputStream out;
  private final InputStream in;
  private final long maxPoints;
  private final GeometryFactory geometryFactory;
  private final ColumnMapping columnMapping;

  public Phase1Processor(
      long msSplit,
      GeometrySimplifier geometrySimplifier,
      int batchSize,
      int precision,
      ObjectMapper objectMapper,
      OutputStream out,
      InputStream in,
      long maxPoints,
      GeometryFactory geometryFactory, ColumnMapping columnMapping) {
    this.msSplit = msSplit;
    this.geometrySimplifier = geometrySimplifier;
    this.batchSize = batchSize;
    this.precision = precision;
    this.objectMapper = objectMapper;
    this.out = out;
    this.in = in;
    this.maxPoints = maxPoints;
    this.geometryFactory = geometryFactory;
    this.columnMapping = columnMapping;
  }

  @Override
  protected Iterator<DataPoint> getRows(TracklineContext context) {
    return new CsvReaderIterator(columnMapping, context.getCsvParser());
  }

  @Override
  protected List<RowListener> createRowListeners(TracklineContext context) {
    return Collections.singletonList(
        new RowListener(msSplit, geometrySimplifier, context.getLineWriter(), batchSize, maxPoints, geometryFactory, precision));
  }

  @Override
  protected TracklineContext createProcessingContext() {
    return new TracklineContext(precision, objectMapper, out, in, columnMapping);
  }
}

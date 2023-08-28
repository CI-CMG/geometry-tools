package edu.colorado.cires.geometrytools.trackline;

import edu.colorado.cires.cmg.tracklinegen.BaseRowListener;
import edu.colorado.cires.cmg.tracklinegen.GeoJsonMultiLineWriter;
import edu.colorado.cires.cmg.tracklinegen.GeometrySimplifier;
import org.locationtech.jts.geom.GeometryFactory;

public class RowListener extends BaseRowListener<DataPoint> {

  public RowListener(
      long msSplit,
      GeometrySimplifier geometrySimplifier,
      GeoJsonMultiLineWriter lineWriter,
      int batchSize,
      long maxPoints,
      GeometryFactory geometryFactory,
      int precision
  ) {
    super(msSplit, geometrySimplifier, lineWriter, batchSize, x -> true, maxPoints, geometryFactory, precision);
  }
}

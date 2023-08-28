package edu.colorado.cires.geometrytools.trackline;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;


public class CsvReaderIterator implements Iterator<DataPoint> {

  private static final int ORDERED_BUFFER_SIZE = 2000;
  private static final int LOW_WATER_MARK = ORDERED_BUFFER_SIZE / 2;

  private final ColumnMapping columnMapping;

  private final Iterator<CSVRecord> iterator;
  private final Map<String,Integer> headerMap;
  private DataPoint lastRow;

  private final Deque<DataPoint> orderedBuffer = new ArrayDeque<>(ORDERED_BUFFER_SIZE);
  private boolean doneReading = false;

  public CsvReaderIterator(ColumnMapping columnMapping, CSVParser csvParser) {
    this.columnMapping = columnMapping;
    iterator = csvParser.iterator();
    CSVRecord headerRow = iterator.next();
    headerMap = new HashMap<>();
    for (int i = 0; i < headerRow.size(); i++) {
      headerMap.put(headerRow.get(i).trim(), i);
    }
  }

  private void readBuffer() {
    if (!doneReading) {
      int size = ORDERED_BUFFER_SIZE - orderedBuffer.size();
      List<DataPoint> buffer = new ArrayList<>(size);
      int i = 0;
      while (i < size) {
        CSVRecord row = readNextLine();
        if (row == null) {
          doneReading = true;
          break;
        }
        buffer.add(parseRow(row));
        i++;
      }
      orderedBuffer.addAll(buffer);
      Collections.sort(buffer);
    }
  }

  private Instant parseTime(CSVRecord record) {
    switch (columnMapping.getTimeFormat()) {
      case iso_utc:
        return Instant.parse(record.get(headerMap.get(columnMapping.getTimeColumn())));
      case epoch_millis:
        return Instant.ofEpochMilli(Long.parseLong(record.get(headerMap.get(columnMapping.getTimeColumn()))));
      case epoch_seconds:
        return Instant.ofEpochSecond(Long.parseLong(record.get(headerMap.get(columnMapping.getTimeColumn()))));
      default:
        throw new IllegalStateException("Unsupported time format " + columnMapping.getTimeFormat());
    }
  }

  private DataPoint parseRow(CSVRecord record) {
    return new DataPoint(
        Double.parseDouble(record.get(headerMap.get(columnMapping.getLongitudeColumn()))),
        Double.parseDouble(record.get(headerMap.get(columnMapping.getLatitudeColumn()))),
        parseTime(record)
    );
  }

  private CSVRecord readNextLine() {
    if (iterator.hasNext()) {
      return iterator.next();
    }
    return null;
  }

  @Override
  public boolean hasNext() {
    if (!doneReading && orderedBuffer.size() < LOW_WATER_MARK) {
      readBuffer();
    }
    return !orderedBuffer.isEmpty();
  }

  @Override
  public DataPoint next() {
    if (hasNext()) {
      DataPoint row = orderedBuffer.pop();
      if (lastRow != null) {
        Instant lastTime = lastRow.getTimestamp();
        Instant thisTime = row.getTimestamp();
        if (thisTime.isBefore(lastTime)) {
          throw new IllegalStateException("Timestamps are out of order: " + lastTime + " : " + thisTime);
        }
      }
      lastRow = row;
      return row;
    }
    throw new IllegalStateException("End of file");
  }
}

package edu.colorado.cires.geometrytools;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Spec;

@Command(
    name = "geometry-tools",
    description = "Tools used to process geometries",
    mixinStandardHelpOptions = true,
    versionProvider = VersionProvider.class,
    subcommands = {
        TracklineTools.class
    }
)
public class GeometryTools implements Runnable {

  @Spec
  private CommandSpec spec;

  public static void main(String[] args) {
    System.exit(new CommandLine(new GeometryTools()).execute(args));
  }

  @Override
  public void run() {
    spec.commandLine().usage(System.out);
  }
}

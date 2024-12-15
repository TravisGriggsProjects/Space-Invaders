package controller;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {

  public static final String LOGGER_NAME = "SpaceInvadersLog";
  // Note it is important for this to be here rather than a local var
  // of the constructor, otherwise the logger gets garbage collected
  private Logger logger;

  public MyLogger() throws IOException {


    logger = Logger.getLogger(LOGGER_NAME);
    FileHandler fh;



    // This block configure the logger with handler and formatter
    fh = new FileHandler("SpaceInvadersV2.log", true);
    logger.addHandler(fh);
    logger.setLevel(Level.ALL);
    SimpleFormatter formatter = new SimpleFormatter();
    fh.setFormatter(formatter);


  }
}

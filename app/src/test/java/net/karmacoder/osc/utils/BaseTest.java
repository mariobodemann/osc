package net.karmacoder.osc.utils;


import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class BaseTest {
  protected String loadFile(String fileName) {
    try {
      final ClassLoader classLoader = getClass().getClassLoader();
      final URL url = classLoader.getResource(fileName);
      if (url != null) {
        final String file = url.getFile();
        return FileUtils.readFileToString(new File(file));
      } else {
        throw new IOException("File not present!");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Could not find resource \"" + fileName + "\".", e);
    }
  }
}

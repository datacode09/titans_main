package titans.nam.grib;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Marquez
 */
public class GribFile {

  public final File grib;
  public final File gribIdx;
  public final ZonedDateTime datetimeref;
  public final int fcststep;

  public GribFile(ZonedDateTime datetimeref, int fcststep, File grib, File gribIdx) {
    this.fcststep = fcststep;
    this.grib = grib;
    this.gribIdx = gribIdx;
    this.datetimeref = datetimeref;
  }

  /**
   *
   * @return
   */
  String getBaseFileName() {
    return this.grib.getName();
  }

  /**
   *
   * @return
   */
  boolean exists() {
    boolean result = this.grib.exists();
    return result;
  }

  /**
   *
   * @return
   */
  OutputStream getOutputStream() {
    FileOutputStream result;
    try {
      result = new FileOutputStream(this.grib);
    } catch (FileNotFoundException ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }
  
  /**
   * 
   */
  public void delete() {
    try {
      this.grib.delete();
      this.gribIdx.delete();
    } catch (Exception ex) {
      Logger.getAnonymousLogger().log(Level.WARNING,// 
        String.format("Deleting grib file '%s'", this.grib), ex);
    }
  }
  
  /**
   * 
   * @return 
   */
  public boolean notExists() {
    return !this.grib.exists();
  }
  
  /**
   * 
   * @return 
   */
  @Override
  public int hashCode() {
    int hash = 7;
    hash = 41 * hash + Objects.hashCode(this.datetimeref);
    hash = 41 * hash + this.fcststep;
    return hash;
  }
  
  /**
   * 
   * @param obj
   * @return 
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final GribFile other = (GribFile) obj;
    if (this.fcststep != other.fcststep) {
      return false;
    }
    if (!Objects.equals(this.datetimeref, other.datetimeref)) {
      return false;
    }
    return true;
  }
  
  

  @Override
  public String toString() {
    return "GribFile{" + "grib=" + grib + ", datetimeref=" + datetimeref + ", fcststep=" + fcststep + '}';
  }

}

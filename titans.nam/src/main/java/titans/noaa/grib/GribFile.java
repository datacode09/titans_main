package titans.noaa.grib;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.mutable.MutableObject;
import titans.noaa.core.NoaaVariable;

/**
 *
 * @author Ricardo Marquez
 */
public class GribFile {

  public final ZonedDateTime datetimeref;
  public final int fcststep;
  public final NoaaVariable var;
  public final File grib;
  public final File gribIdx;

  public GribFile(ZonedDateTime datetimeref, int fcststep, NoaaVariable var, File grib, File gribIdx) {
    this.datetimeref = datetimeref;
    this.fcststep = fcststep;
    this.var = var;
    this.grib = grib;
    this.gribIdx = gribIdx;

  }

  /**
   *
   * @return
   */
  public String getBaseFileName() {
    return this.grib.getName();
  }

  /**
   *
   * @return
   */
  public boolean exists() {
    boolean result = this.grib.exists();
    return result;
  }

  /**
   *
   * @return
   */
  public OutputStream getOutputStream() {
    FileOutputStream result;
    try {
      MutableObject obj = new MutableObject();
      result = new FileOutputStream(this.grib) {
        @Override
        public void close() throws IOException {
          super.close();
          FileLock lock = (FileLock) obj.getValue();
          if (lock != null && lock.isValid()) {
            lock.close();
          }
        }
      };
      obj.setValue(result.getChannel().lock());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    return result;
  }

  /**
   *
   * @return
   */
  public boolean isNotLocked() {
    return !this.isLocked();
  }

  /**
   *
   * @return
   */
  public boolean isLocked() {
    try {
      if (this.notExists()) {
        return false;
      }
      FileOutputStream outputStream = new FileOutputStream(this.grib);
      FileChannel channel = outputStream.getChannel();
      FileLock lock;
      try {
        lock = channel.tryLock();
      } catch (OverlappingFileLockException ex) {
        return true;
      }
      if (lock == null) {
        return true;
      } else {
        lock.release();
        return false;
      }
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
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

  /**
   *
   * @return
   */
  @Override
  public String toString() {
    return "GribFile{" + "grib=" + grib + ", datetimeref=" + datetimeref + ", fcststep=" + fcststep + '}';
  }

  /**
   *
   * @param subfolderId
   * @return
   */
  public GribFile setSubPath(int subfolderId) {
    File newgrib = new File(String.format("%s\\%04d", grib.getParent(), subfolderId), grib.getName());
    File newgribIdx = new File(String.format("%s\\%04d", gribIdx.getParent(), subfolderId), gribIdx.getName());
    GribFile result = new GribFile(datetimeref, fcststep, var, newgrib, newgribIdx);
    return result;
  }
  
  /**
   * 
   */
  public void createFile() {
    if (!this.grib.getParentFile().exists()) {
      this.grib.getParentFile().mkdirs();
    }
    if (!this.grib.exists()) {
      try {
        this.grib.createNewFile();
      } catch (IOException ex) {
        throw new RuntimeException(ex); 
      }
    }
  }
}

package folderMonitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirectoryProperties {

	private long folderSize;
	private String folderName;
	private int noOfFiles;
	private long lastModified;
	private long totalDiskSpace;
	private long diskFreeSpace;
	private long usableFreeSpace;
	private List<String> dirFiles;
	
	private Map<String, Object> storeProperties = new HashMap<String, Object>();

	public DirectoryProperties(File directory) throws NoSuchFileException{
		
		this.folderName = directory.getName();
		this.lastModified = directory.lastModified();
		this.totalDiskSpace = directory.getTotalSpace();
		this.diskFreeSpace = directory.getFreeSpace();
		this.usableFreeSpace = directory.getUsableSpace();
		
		this.folderSize = getDirectorySizeJava(directory.toPath());
		this.dirFiles = listAllFilesInDirectory(directory.toPath());
		this.noOfFiles = dirFiles.size();
		
		storeProperties.put("LastModified", lastModified);
		storeProperties.put("FolderSize", folderSize);
		storeProperties.put("NoOfFiles", noOfFiles);
		
		
	}
	
	  long getDirectorySizeJava(Path path) {
	      long sizeInBytes = 0;
	      try (Stream<Path> walk = Files.walk(path)) {
	    	  sizeInBytes = walk.filter(Files::isRegularFile).mapToLong(p -> {
	                      try {
	                          return Files.size(p);
	                      } catch (IOException e) {
	                          System.out.printf("Failed to get size of %s%n%s", p, e);
	                          return 0L;
	                      }
	                  }).sum();

	      } catch (IOException e) {
	          System.out.printf("IO errors %s", e);
	      }

	      return sizeInBytes;

	  }
	  
	  List<String> listAllFilesInDirectory(Path path) {
		  List<String> result = null;
		  
		  try (Stream<Path> walk = Files.walk(path)) {

		        result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
		        //result.forEach(System.out::println);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		  
		  return result;
	  }

	public long getFolderSize() {
		return folderSize;
	}

	public String getFolderName() {
		return folderName;
	}

	public int getNoOfFiles() {
		return noOfFiles;
	}

	public long getLastModified() {
		return lastModified;
	}

	public long getTotalDiskSpace() {
		return totalDiskSpace;
	}

	public long getDiskFreeSpace() {
		return diskFreeSpace;
	}

	public long getUsableFreeSpace() {
		return usableFreeSpace;
	}

	public List<String> getDirFiles() {
		return dirFiles;
	}
	
	
	public Map<String, Object> getStoreProperties() {
		return storeProperties;
	}
	  

}

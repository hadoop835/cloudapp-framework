package io.cloudapp.util;

import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Map;

public class FileUtilsTest {
    
    public static final String FILE_PATH = "filePath";
    private static final Logger log =
            LoggerFactory.getLogger(FileUtilsTest.class);
    
    @After
    public void tearDown() {
        // Add additional tear down code here
        File file = new File(FILE_PATH);
        if(file.exists() && file.delete()) {
            log.info("File deleted successfully");
        } else {
            log.info("File not exists or file deletion failed");
        }
    }
    
    @Test
    public void testReadPropertiesFromFile() {
        // Setup
        // Run the test
        Map<?, ?> result = FileUtils.readPropertiesFromFile(FILE_PATH);
        
        assert result.isEmpty();
        
        File file = new File(FILE_PATH);
        
        try(FileWriter fw = new FileWriter(file);
            BufferedWriter writer = new BufferedWriter(fw)) {
            
            String content = "key=value";
            
            writer.write(content);
            
        } catch (Exception e) {
            log.error("Exception occurred while writing to file", e);
        }
        result = FileUtils.readPropertiesFromFile(FILE_PATH);
        // Verify the results
        
        assert !result.isEmpty();
    }
    
}

package cz.kramolis.exercises.paymenttracker;

import org.junit.Test;

import java.nio.file.Path;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests {@link Main}.
 */
public class MainTest {

    @Test
    public void testFindInputFile_fileExists() {
        String filename = this.getClass().getClassLoader().getResource("input1.txt").getFile();
        Path path = Main.findInputFile(filename);

        assertThat(path, notNullValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindInputFile_fileDoesNotExist() {
        String filename = "does not exist";
        Main.findInputFile(filename);
    }

}

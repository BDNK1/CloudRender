package tech.theraven.cloudrender;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class CloudRenderApplicationTests {

    @SneakyThrows
    @Test
    void pythonTest() {

        String blendPath = "DoughNutTurn.blend";
        String pyPath = "number.py";

        ClassLoader classLoader = getClass().getClassLoader();
        File blendFile = new File(classLoader.getResource(blendPath).getFile());
        File pyFile = new File(classLoader.getResource(pyPath).getFile());
        String absoluteBlendPath = blendFile.getAbsolutePath();
        String absolutePyPath = pyFile.getAbsolutePath();

        ProcessBuilder processBuilder = new ProcessBuilder("blender", "-b", absoluteBlendPath, "--python", absolutePyPath);
        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();
        Integer results = readProcessOutput(process.getInputStream());
        System.out.println(results);
        assertThat("Results should not be empty", results, is(not("")));

        int exitCode = process.waitFor();
        assertEquals("No errors should be detected", 0, exitCode);

    }

    @SneakyThrows
    private Integer readProcessOutput(InputStream inputStream) {
        var str = new BufferedReader(new InputStreamReader(inputStream)).readLine();

        return Integer.valueOf(str);
    }

}

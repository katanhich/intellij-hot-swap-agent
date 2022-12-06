package com.phuoc.hotswapagent.patcher;

import com.intellij.execution.Executor;
import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.runners.JavaProgramPatcher;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Phuoc Cao
 */
public class HotSwapPatcher extends JavaProgramPatcher {

    @Override
    public void patchJavaParameters(Executor executor, RunProfile configuration, JavaParameters javaParameters) {
        createPropertyFile(javaParameters);
        addVMOptions(javaParameters);
    }

    private static void addVMOptions(JavaParameters javaParameters) {
        ParametersList list = javaParameters.getVMParametersList();
        list.add("-XX:+AllowEnhancedClassRedefinition");
        list.add("-XX:HotswapAgent=fatjar");
    }

    private static void createPropertyFile(JavaParameters javaParameters) {
        try {
            String config = """
                autoHotswap=true
                LOGGER=WARNING
                    """;

            String classpath = javaParameters.getClassPath().getPathList().get(0);
            Path path = Path.of(classpath, "hotswap-agent.properties");

            FileUtils.writeStringToFile(path.toFile(), config, StandardCharsets.UTF_8);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}

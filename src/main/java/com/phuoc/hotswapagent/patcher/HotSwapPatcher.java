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
import java.util.List;

/**
 * @author Phuoc Cao
 */
public class HotSwapPatcher extends JavaProgramPatcher {

    @Override
    public void patchJavaParameters(Executor executor, RunProfile configuration, JavaParameters javaParameters) {
        if (isMaven(configuration.getName()) || isUnitTest(javaParameters)) {
            return;
        }

        createPropertyFile(javaParameters);
        addVMOptions(javaParameters);
    }

    private boolean isMaven(String cmd) {
        return cmd.contains("[") && cmd.contains("]");
    }

    private boolean isUnitTest(JavaParameters javaParameters) {
        List<String> list = javaParameters.getVMParametersList().getParameters();
        if (list.size() > 1 && list.get(0).equals("-ea")) {
            return true;
        }

        list = javaParameters.getProgramParametersList().getParameters();
        if (list.size() > 2 && list.get(1).contains("junit")) {
            return true;
        }

        return javaParameters.getMainClass().contains("JUnitStarter");
    }

    private void addVMOptions(JavaParameters javaParameters) {
        ParametersList list = javaParameters.getVMParametersList();
        list.add("-XX:+AllowEnhancedClassRedefinition");
        list.add("-XX:HotswapAgent=fatjar");
    }

    private void createPropertyFile(JavaParameters javaParameters) {
        try {
            String config = """
                autoHotswap=true
                LOGGER=WARNING
                    """;

            String classpath = javaParameters.getClassPath().getPathList().get(0);
            Path path = Path.of(classpath, "hotswap-agent.properties");

            FileUtils.writeStringToFile(path.toFile(), config, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

package com.phuoc.hotswapagent.runner;


import com.intellij.execution.configurations.JavaParameters;
import com.intellij.execution.configurations.ModuleRunProfile;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.impl.DefaultJavaProgramRunner;
import com.intellij.execution.jar.JarApplicationConfiguration;
import com.intellij.execution.remote.RemoteConfiguration;
import com.intellij.execution.runners.JavaProgramPatcher;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import static com.phuoc.hotswapagent.util.Resources.RUN_WITH_VISUAL_VM;

/**
 * @author Phuoc Cao
 */
public class RunHotSwapRunner extends DefaultJavaProgramRunner {

    @Override
    public String getRunnerId() {
        return RUN_WITH_VISUAL_VM;
    }

    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return executorId.equals(RUN_WITH_VISUAL_VM) && (profile instanceof ModuleRunProfile || profile instanceof JarApplicationConfiguration) && !(profile instanceof RemoteConfiguration);
    }

    @Override
    public void patch(@NotNull JavaParameters javaParameters, @Nullable RunnerSettings settings, @NotNull RunProfile runProfile, boolean beforeExecution) {
        createPropertyFile(javaParameters);
        addVMOptions(javaParameters, runProfile);
    }

    private static void addVMOptions(@NotNull JavaParameters javaParameters, @NotNull RunProfile runProfile) {
        ParametersList list = javaParameters.getVMParametersList();
        list.add("-XX:+AllowEnhancedClassRedefinition");
        list.add("-XX:HotswapAgent=fatjar");
        JavaProgramPatcher.runCustomPatchers(javaParameters, DefaultRunExecutor.getRunExecutorInstance(), runProfile);
    }

    private static void createPropertyFile(JavaParameters javaParameters) {
        try {
            String config = """
                autoHotswap=true
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

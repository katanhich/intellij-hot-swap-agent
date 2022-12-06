package com.phuoc.hotswapagent.executor;

import com.intellij.execution.executors.DefaultRunExecutor;
import com.phuoc.hotswapagent.util.Resources;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

import static com.phuoc.hotswapagent.util.Resources.RUN_WITH_VISUAL_VM;
import static com.phuoc.hotswapagent.util.Resources.RUN_WITH_VISUAL_VM1;

/**
 * @author Phuoc Cao
 */
public class RunHotSwapExecutor extends DefaultRunExecutor {

    @NotNull
    public String getToolWindowId() {
        return getId();
    }

    public Icon getToolWindowIcon() {
        return Resources.RUN_13;
    }

    @NotNull
    public Icon getIcon() {
        return Resources.RUN;
    }

    public Icon getDisabledIcon() {
        return null;
    }

    public String getDescription() {
        return RUN_WITH_VISUAL_VM;
    }

    @NotNull
    public String getActionName() {
        return RUN_WITH_VISUAL_VM1;
    }

    @NotNull
    public String getId() {
        return RUN_WITH_VISUAL_VM;
    }

    @NotNull
    public String getStartActionText() {
        return RUN_WITH_VISUAL_VM;
    }

    public String getContextActionId() {
        return getId() + " context-action-does-not-exist";
    }

    public String getHelpId() {
        return null;
    }
}

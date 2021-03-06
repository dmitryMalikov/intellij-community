/*
 * Copyright 2000-2016 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.intellij.ide.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindowId;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

public class CommanderViewActionGroup extends DefaultActionGroup  implements DumbAware {
  public CommanderViewActionGroup() {
    super();
  }

  public void update(@NotNull AnActionEvent e){
    Presentation presentation = e.getPresentation();
    Project project = e.getProject();
    if (project == null) {
      presentation.setVisible(false);
      return;
    }
    String id = ToolWindowManager.getInstance(project).getActiveToolWindowId();
    boolean isCommanderActive = ToolWindowId.COMMANDER.equals(id);
    presentation.setVisible(isCommanderActive);
  }
}
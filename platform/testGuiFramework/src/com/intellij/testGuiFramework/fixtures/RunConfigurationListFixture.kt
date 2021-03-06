// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.testGuiFramework.fixtures

import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl
import com.intellij.testGuiFramework.framework.Timeouts
import com.intellij.testGuiFramework.impl.popupMenu
import com.intellij.ui.popup.PopupFactoryImpl
import org.fest.swing.core.Robot
import org.fest.swing.exception.ComponentLookupException
import org.fest.swing.fixture.JButtonFixture
import org.fest.swing.timing.Pause
import javax.swing.JButton
import javax.swing.JList

/**
 * @author Artem.Gainanov
 */
class RunConfigurationListFixture(val myRobot: Robot, val myIde: IdeFrameFixture) {

  private val EDIT_CONFIGURATIONS: String = "Edit Configurations..."

  /**
   * Returns a list of available run configurations
   * except for "Edit configuration" and "save configuration"
   */
  fun getRunConfigurationList(): List<String> {
    showPopup()
    val list = myRobot.finder()
      .find(myIde.target()) { it is JList<*> } as JList<*>
    val returnList: MutableList<String> = mutableListOf()
    for (i in 0 until list.model.size) {
      returnList.add(i, list.model.getElementAt(i).toString())
    }
    //Close popup
    showPopup()
    return returnList.filter { it != EDIT_CONFIGURATIONS && !it.contains("Save ") }
  }


  /**
   * Opens up run configuration popup and chooses given RC
   * @param name
   */
  fun configuration(name: String): RunActionFixture {
    showPopup()
    myIde.popupMenu(name, Timeouts.minutes02).clickSearchedItem()
    return RunActionFixture()
  }

  /**
   * Trying to click on Edit Configurations attempt times.
   */
  fun editConfigurations(attempts: Int = 5) {
    try {
      JButtonFixture(myRobot, addConfigurationButton()).click()
    }catch(e: ComponentLookupException) {
      for (i in 0 until attempts) {
        showPopup()
        Pause.pause(1000)
        if (getEditConfigurationsState()) {
          break
        }
        //Close popup
        showPopup()
      }
      myIde.popupMenu(EDIT_CONFIGURATIONS, Timeouts.minutes02).clickSearchedItem()
    }


  }

  /**
   * Click on Add Configuration button.
   * Available if no configurations are available in the RC list
   */
  fun addConfiguration() {
    JButtonFixture(myRobot, addConfigurationButton()).click()
  }

  inner class RunActionFixture {
    fun run() {
      ActionButtonFixture.findByText("Run", myRobot, myIde.target()).click()
    }

    fun debug() {
      ActionButtonFixture.findByText("Debug", myRobot, myIde.target()).click()
    }

    fun runWithCoverage() {
      ActionButtonFixture.findByText("Run with Coverage", myRobot, myIde.target()).click()
    }

    fun stop() {
      ActionButtonFixture.findByText("Stop", myRobot, myIde.target()).click()

    }
  }

  private fun showPopup() {
    JButtonFixture(myRobot, getRunConfigurationListButton()).click()
  }

  private fun addConfigurationButton(): JButton {
    return myRobot.finder()
      .find(myIde.target()) {
        it is JButton
        && it.text == "Add Configuration..."
        && it.isShowing
      } as JButton
  }

  private fun getRunConfigurationListButton(): JButton {
    return myRobot.finder()
      .find(myIde.target()) {
        it is JButton
        && it.parent.parent is ActionToolbarImpl
        && it.text != ""
        && it.isShowing
      } as JButton
  }

  private fun getEditConfigurationsState(): Boolean {
    val list = myRobot.finder()
      .find(myIde.target()) { it is JList<*> } as JList<*>
    val actionItem = list.model.getElementAt(0) as PopupFactoryImpl.ActionItem
    return actionItem.isEnabled
  }

}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gravityshock

import com.badlogic.gdx.backends.jogl.JoglApplication;

object MainDesktop {
  /**
   * @param args the command line arguments
   */
  def main(args: Array[String]): Unit = {
    new JoglApplication(Main, "Hello World", 480, 320, false);
  }
}

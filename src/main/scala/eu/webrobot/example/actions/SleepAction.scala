package eu.webrobot.example.actions

import eu.webrobot.plugin.sdk.{ActionSpec, WAction, WActionArgs}

/** Browser action that pauses execution for a specified number of milliseconds.
 *
 *  NOTE: Hot-loading of actions requires ETL restart after deploying the JAR.
 *
 *  Pipeline YAML:
 *  {{{
 *  traces:
 *    - action: sleep
 *      params:
 *        ms: 1000
 *  }}}
 */
class SleepAction extends WAction {

  override def name: String = "sleep"

  override def build(args: WActionArgs): ActionSpec =
    ActionSpec(actionType = "sleep", params = Map("ms" -> args.int("ms", 500)))
}

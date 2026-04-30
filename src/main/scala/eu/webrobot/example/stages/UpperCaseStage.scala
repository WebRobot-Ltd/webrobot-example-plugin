package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WTransformStage}

/** Converts a text field to upper case.
 *
 *  Pipeline YAML usage:
 *  {{{
 *  - id: upper_case
 *    args:
 *      - fieldName: "title"
 *  }}}
 */
class UpperCaseStage extends WTransformStage {

  override def name: String = "upper_case"

  override def transform(row: WRow, args: WArgs): WRow = {
    val field = args.string(0, "text")
    row.str(field).fold(row)(v => row.set(field, v.toUpperCase))
  }
}

package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WFilterStage}

/** Discards rows where the specified field is null, empty, or blank.
 *
 *  Pipeline YAML:
 *  {{{
 *  - id: filter_non_empty
 *    args:
 *      - fieldName: "price"
 *  }}}
 */
class FilterNonEmptyStage extends WFilterStage {

  override def name: String = "filter_non_empty"

  override def include(row: WRow, args: WArgs): Boolean = {
    val field = args.string(0, "value")
    row.str(field).exists(_.trim.nonEmpty)
  }
}

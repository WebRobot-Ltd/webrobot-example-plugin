package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WAggregateStage}

/** Groups rows by a key field and sums a numeric field.
 *
 *  Pipeline YAML:
 *  {{{
 *  - id: sum_by_key
 *    args:
 *      - keyField:   "category"
 *      - valueField: "price"
 *  }}}
 */
class SumByKeyStage extends WAggregateStage {

  override def name: String = "sum_by_key"

  override def groupBy(row: WRow): String =
    row.str("category").getOrElse("")

  override def combine(left: WRow, right: WRow, args: WArgs): WRow = {
    val valueField = args.string(1, "amount")
    val sum = left.double(valueField).getOrElse(0.0) +
              right.double(valueField).getOrElse(0.0)
    left.set(valueField, sum)
  }
}

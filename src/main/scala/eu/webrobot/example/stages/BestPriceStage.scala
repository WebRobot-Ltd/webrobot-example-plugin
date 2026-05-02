package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WGroupStage, WebroStageContext}

/**
 * Example WGroupStage: selects the row with the minimum (or maximum) price within a group.
 * Groups rows by a key field (e.g. EAN), then picks the best price across all sites.
 *
 * Pipeline YAML:
 * {{{
 * - stage: best_price
 *   args:
 *     - group_field: "ean"
 *     - price_field: "price"
 *     - mode: "min"          # min | max | avg
 * }}}
 */
class BestPriceStage extends WGroupStage {

  override def name: String = "best_price"

  override def groupBy(row: WRow): String =
    row.str("ean").getOrElse("__no_group__")

  override def aggregate(rows: Iterable[WRow], args: WArgs, ctx: WebroStageContext): WRow = {
    val groupField = args.string(0, "ean")
    val priceField = args.string(1, "price")
    val mode       = args.string(2, "min")

    val rowList = rows.toList
    if (rowList.isEmpty) return WRow.empty

    val withPrice = rowList.flatMap(r => r.double(priceField).map(p => (p, r)))

    if (withPrice.isEmpty) {
      ctx.warn(s"[$name] group '${rowList.head.str(groupField).getOrElse("?")}' has no valid $priceField")
      return rowList.head
    }

    val selected = mode match {
      case "max" => withPrice.maxBy(_._1)._2
      case "avg" =>
        val avg = withPrice.map(_._1).sum / withPrice.size
        rowList.head.set(s"${priceField}_avg", avg)
      case _     => withPrice.minBy(_._1)._2  // default: min
    }

    selected
      .set(s"${priceField}_${mode}", withPrice.map(_._1).min)
      .set(s"${priceField}_max",     withPrice.map(_._1).max)
      .set("site_count",             rowList.size)
  }
}

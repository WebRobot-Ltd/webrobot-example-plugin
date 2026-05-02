package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WSinkStage, WebroStageContext}

/**
 * Example WSinkStage: persists a field value to a DB table, passes the row through.
 *
 * Pipeline YAML:
 * {{{
 * - stage: db_save_example
 *   args:
 *     - id_field: "product_id"
 *     - value_field: "extracted_price"
 *     - table: "price_results"
 * }}}
 */
class DbSaveStage extends WSinkStage {

  override def name: String = "db_save_example"

  override def consume(row: WRow, args: WArgs, ctx: WebroStageContext): WRow = {
    val idField    = args.string(0, "id")
    val valueField = args.string(1, "value")
    val table      = args.string(2, "results")

    val id    = row.str(idField).getOrElse("")
    val value = row.str(valueField).getOrElse("")

    if (id.nonEmpty && value.nonEmpty) {
      ctx.execute(
        s"INSERT INTO $table (id, value, saved_at) VALUES (?, ?, NOW()) ON CONFLICT (id) DO UPDATE SET value = EXCLUDED.value, saved_at = NOW()",
        Seq(id, value)
      )
    } else {
      ctx.warn(s"[$name] Skipping row: missing $idField or $valueField")
    }

    row.set("_saved", true)
  }
}

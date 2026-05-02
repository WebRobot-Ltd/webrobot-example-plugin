package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WSourceStage, WebroStageContext}

/**
 * Example WSourceStage: loads rows from a DB table filtered by a status column.
 *
 * Pipeline YAML:
 * {{{
 * - stage: db_load_example
 *   args:
 *     - table: "my_items"
 *     - status_filter: "pending"
 *     - limit: 1000
 * }}}
 */
class DbLoadStage extends WSourceStage {

  override def name: String = "db_load_example"

  override def produce(args: WArgs, ctx: WebroStageContext): Iterator[WRow] = {
    val table  = args.string(0, "items")
    val status = args.string(1, "active")
    val limit  = args.int(2, 1000)

    ctx.log(s"[$name] Loading up to $limit rows from $table where status=$status")

    ctx.query(
      s"SELECT * FROM $table WHERE status = ? LIMIT ?",
      Seq(status, limit)
    )
  }
}

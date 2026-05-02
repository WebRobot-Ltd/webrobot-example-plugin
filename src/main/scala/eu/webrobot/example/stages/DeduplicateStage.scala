package eu.webrobot.example.stages

import eu.webrobot.plugin.sdk.{WArgs, WRow, WPartitionStage, WebroStageContext}

/**
 * Example WPartitionStage: removes duplicate rows within each partition by a key field.
 * For global deduplication use a WGroupStage instead.
 *
 * Pipeline YAML:
 * {{{
 * - stage: deduplicate_partition
 *   args:
 *     - key_field: "url"
 * }}}
 */
class DeduplicateStage extends WPartitionStage {

  override def name: String = "deduplicate_partition"

  override def transformPartition(
    rows: Iterator[WRow],
    args: WArgs,
    ctx: WebroStageContext
  ): Iterator[WRow] = {
    val keyField = args.string(0, "id")
    val seen     = scala.collection.mutable.HashSet.empty[String]
    var kept     = 0
    var dropped  = 0

    val result = rows.filter { row =>
      val key = row.str(keyField).getOrElse("")
      if (key.isEmpty || seen.add(key)) { kept += 1; true }
      else { dropped += 1; false }
    }.toList // materialize to allow logging after

    ctx.log(s"[$name] partition dedup on '$keyField': kept=$kept dropped=$dropped")
    result.iterator
  }
}

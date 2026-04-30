package eu.webrobot.example.resolvers

import eu.webrobot.plugin.sdk.WResolver

/** Extracts a price (decimal number) from raw element text.
 *
 *  DSL usage in pipeline YAML:
 *  {{{
 *  resolvers:
 *    - name: price_resolver
 *      selector: "span.price"
 *  }}}
 */
class PriceResolver extends WResolver {

  override def name: String = "price_resolver"

  private val pattern = """([0-9]+(?:[.,][0-9]{1,2})?)""".r

  override def extract(text: String): Option[String] =
    pattern.findFirstIn(text).map(_.replace(',', '.'))
}

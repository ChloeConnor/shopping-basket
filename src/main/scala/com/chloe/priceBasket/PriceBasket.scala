package com.chloe.priceBasket

object PriceBasket extends App {

  def discount(goodInput: String,
               mapOfDiscounts: Map[String, Double],
               goods: Map[String, Double]): Double =
    if (mapOfDiscounts.contains(goodInput)) {
      goods(goodInput) * (1 - mapOfDiscounts(goodInput))
    } else {
      goods(goodInput)
    }

  def priceBasket(items: Set[String]): Unit = {

    val goods =
      Map("soup" -> 0.65, "bread" -> 0.8, "milk" -> 1.3, "apple" -> 1.0)

    val discountsMap = Map("apple" -> 0.1)

    val totalWithoutDiscount: Double = items.map(item => goods(item)).sum

    val totalWithDiscount: Double =
      items.map(item => discount(item, discountsMap, goods)).sum
  }

  override def main(args: Array[String]): Unit = {}
}

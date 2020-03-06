package com.chloe.priceBasket

object PriceBasket extends App {

  def discount(goodInput: String,
               goodDiscount: String,
               percentageOff: Double,
               goods: Map[String, Double]): Double = {
    goodInput match {
      case goodDiscount =>
        goods(goodDiscount) * (1 - percentageOff)
      case _ =>
        goods(goodInput)
    }
  }

  def priceBasket(items: Set[String]): Unit = {

    val goods =
      Map("soup" -> 0.65, "bread" -> 0.8, "milk" -> 1.3, "apple" -> 1.0)

    val totalWithoutDiscount: Double = items.map(item => goods(item)).sum

    val totalWithDiscount: Double =
      items.map(item => discount(item, "apple", 0.1, goods)).sum
  }

  override def main(args: Array[String]): Unit = {}
}

package com.chloe.priceBasket

object PriceBasket extends App {

  val goods = Map("soup" -> 0.65, "bread" -> 0.8, "milk" -> 1.3, "apple" -> 1.0)

  def discount(good: String, percentageOff: Double) = goods(good) * (percentageOff)

  def offer(numberBought: Int, itemBought: String, discountOn: String, discountApplied: Double): Unit = {
    goods(itemBought)
  }

  override def main(args: Array[String]): Unit = {

    val totalWithoutDiscount: Double = ???

    val discountApples: Double = discount("apple", 0.1)

    val discountOffer: Double = ???

    val totalWithDiscountApplied = totalWithoutDiscount - discountApples - discountOffer

  }
}

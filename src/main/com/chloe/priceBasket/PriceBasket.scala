package com.chloe.priceBasket

import com.chloe.priceBasket.Good

object PriceBasket extends App {

  def main(args: Array[String]): Unit = {

    val soup = Good("soup", 0.65)
    val bread = Good("bread", 0.8)
    val milk = Good("milk", 1.3)
    val apples = Good("apples", 1.0)

    def discount(good: Good, percentageOff: Double) = {
      good.price
    }
  }
}

package com.chloe.priceBasket

import com.chloe.priceBasket.UnconditionalDiscount.applyDiscount
import com.chloe.priceBasket.dataTypes.Discount.{Condition, ConditionalDiscount, Discount}
import com.chloe.priceBasket.dataTypes.Good

object PriceBasket extends App {

  val appleDis = Discount("apple", 0.1)

  val conditionalDiscounts: Set[ConditionalDiscount] = Set(
    ConditionalDiscount("Loaf", 0.5, Condition(Set("apple", "apple")))
  )
  val unconditionalDiscounts: Set[Discount] = Set(appleDis)

  def priceBasket(items: Set[String]): Double = {

    val pricesMap =
      Map("soup" -> 0.65, "bread" -> 0.8, "milk" -> 1.3, "apple" -> 1.0)

    val basket: Set[Good]  = items.map(item => Good(item, pricesMap(item), pricesMap(item)))

    val goodsWithDiscount: Set[Good] = basket.map(good => applyDiscount(good, unconditionalDiscounts))

    goodsWithDiscount.map(d => d.discountedPrice).sum
  }

  override def main(args: Array[String]): Unit = {}
}

package com.chloe.priceBasket

import com.chloe.priceBasket.ApplyDiscounts.applyDiscount
import com.chloe.priceBasket.ApplyDiscounts.applyConditionalDiscount
import com.chloe.priceBasket.dataTypes.Discount.{
  Condition,
  ConditionalDiscount,
  Discount
}
import com.chloe.priceBasket.dataTypes.Good

object PriceBasket extends App {

  val appleDis = Discount("apple", 0.1)

  val conditionalDiscounts: List[ConditionalDiscount] = List(
    ConditionalDiscount("bread", 0.5, Condition(List("soup", "soup")))
  )
  val unconditionalDiscounts: List[Discount] = List(appleDis)

  def priceBasket(items: List[String]): Double = {

    val pricesMap =
      Map("soup" -> 0.65, "bread" -> 0.8, "milk" -> 1.3, "apple" -> 1.0)

    val basket: List[Good] =
      items.map(item => Good(item, pricesMap(item), pricesMap(item)))

    val goodsWithDiscount: List[Good] =
      applyConditionalDiscount(basket.map(good => applyDiscount(good, unconditionalDiscounts)), conditionalDiscounts)

    goodsWithDiscount.map(d => d.discountedPrice).sum
  }

  override def main(args: Array[String]): Unit = {}
}

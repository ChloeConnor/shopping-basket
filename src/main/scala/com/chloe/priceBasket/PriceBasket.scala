package com.chloe.priceBasket

import com.chloe.priceBasket.ApplyDiscounts.applyDiscount
import com.chloe.priceBasket.ApplyDiscounts.applyConditionalDiscount
import com.chloe.priceBasket.dataTypes.Discount.{
  Condition,
  ConditionalDiscount,
  Discount
}
import com.chloe.priceBasket.dataTypes.Good
import scala.math.BigDecimal.RoundingMode

object PriceBasket extends App {

  def getTotalWithoutDiscount(goods: List[Good]) =
    BigDecimal(goods.map(d => d.price).sum)
      .setScale(2, RoundingMode.HALF_EVEN)

  def getTotalWithDiscount(goods: List[Good]) =
    BigDecimal(goods.map(d => d.discountedPrice).sum)
      .setScale(2, RoundingMode.HALF_EVEN)

  def calculateDiscountedGoods(items: List[String],
                               pricesMap: Map[String, Double],
                               conditionalDiscounts: List[ConditionalDiscount],
                               discounts: List[Discount]): List[Good] = {

    val initialBasket: List[Good] =
      items.map(item => Good(item, pricesMap(item), pricesMap(item)))

    val subtotal = getTotalWithoutDiscount(initialBasket)

    println(s"Subtotal: £$subtotal")

    val allDiscounts = discounts ::: applyConditionalDiscount(
      initialBasket,
      conditionalDiscounts
    )
    initialBasket.map(good => applyDiscount(good, allDiscounts))
  }

  override def main(args: Array[String]): Unit = {


    val pricesMap =
      Map("Soup" -> 0.65, "Bread" -> 0.8, "Milk" -> 1.3, "Apples" -> 1.0)

    val twoSoupDiscountBread: ConditionalDiscount =
      ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

    val discountOnApples: Discount = Discount("Apples", 0.1)

    val basketCalculated = calculateDiscountedGoods(
      args.toList,
      pricesMap,
      List(twoSoupDiscountBread),
      List(discountOnApples)
    )

    if (getTotalWithDiscount(basketCalculated) == getTotalWithoutDiscount(
          basketCalculated)) {
      println("(No offers available)")
    }

    println(s"Total price: £${getTotalWithDiscount(basketCalculated)}")
  }
}

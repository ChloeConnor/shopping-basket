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
import com.chloe.priceBasket.utils.ReadFile._

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

    if (args.length == 0) {
      println("Please specify at least one item")
    }
    val pricesMap = readCSVToMap("src/main/resources/prices.csv")
    val conditionalDiscounts = readCSVToConditionalDiscount(
      "src/main/resources/conditional_discounts.csv")
    val discounts = readCSVToDiscount("src/main/resources/discounts.csv")

    val basketCalculated = calculateDiscountedGoods(
      args.toList,
      pricesMap,
      conditionalDiscounts,
      discounts
    )

    if (getTotalWithDiscount(basketCalculated) == getTotalWithoutDiscount(
          basketCalculated
        )) {
      println("(No offers available)")
    }

    println(s"Total price: £${getTotalWithDiscount(basketCalculated)}")
  }
}

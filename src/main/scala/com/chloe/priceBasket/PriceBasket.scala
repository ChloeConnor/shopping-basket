package com.chloe.priceBasket

import com.chloe.priceBasket.utils.ReadFile._
import com.chloe.priceBasket.discounts.CalculateDiscountedGoods._

object PriceBasket extends App {

  override def main(args: Array[String]): Unit = {

    if (args.length == 0) println("Please specify at least one item")

    val pricesMap = readCSVToMap("src/main/resources/prices.csv")
    val conditionalDiscounts = readCSVToConditionalDiscount(
      "src/main/resources/conditional_discounts.csv"
    )
    val discounts = readCSVToDiscount("src/main/resources/discounts.csv")

    val basketCalculated = calculateDiscountedGoods(
      args.toList,
      pricesMap,
      conditionalDiscounts,
      discounts
    )

    if (getTotalWithDiscount(basketCalculated) == getTotalWithoutDiscount(
          basketCalculated
        )) println("(No offers available)")

    println(s"Total price: £${getTotalWithDiscount(basketCalculated)}")
  }
}

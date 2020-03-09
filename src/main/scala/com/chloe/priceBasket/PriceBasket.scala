package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.{Basket, Good}
import com.chloe.priceBasket.utils.ReadFile._
import com.chloe.priceBasket.discounts.CalculateDiscountedGoods._
import com.chloe.priceBasket.utils.Logging._

object PriceBasket extends App {

  override def main(args: Array[String]): Unit = {

    if (args.length == 0) println("Please specify at least one item")
    val pricesMap = readCSVToMap("src/main/resources/prices.csv")
    val basket = Basket.apply(args.toList, pricesMap)

    val conditionalDiscounts = readCSVToConditionalDiscount(
      "src/main/resources/conditional_discounts.csv"
    )

    val discounts =
      readCSVToDiscount(
        "src/main/resources/discounts.csv",
        basket.countQuantityOfEach
      )

    val basketCalculated = calculateDiscountedGoods(
      basket,
      pricesMap,
      conditionalDiscounts,
      discounts
    )

    outputNoOffers(basketCalculated)

    outputTotalBasketCost(basketCalculated, total = true)
  }
}

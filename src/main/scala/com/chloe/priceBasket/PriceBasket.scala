package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.utils.ReadFile._
import com.chloe.priceBasket.discounts.CalculateDiscountedGoods._

object PriceBasket extends App {

  def createBasket(items: List[String],
                   pricesMap: Map[String, Double]): List[Good] =
    items.map(item => Good(item, pricesMap(item), pricesMap(item)))

  def countItemsInBasket(basket: List[Good]): Map[String, Int] =
    basket.map(g => g.name).groupBy(identity).mapValues(_.size)


  def logNoOffers(basket: List[Good]) = if (getTotal(basket, discounted = false) == getTotal(
    basket,
    discounted = true
  )) println("(No offers available)")

  override def main(args: Array[String]): Unit = {

    if (args.length == 0) println("Please specify at least one item")
    val pricesMap = readCSVToMap("src/main/resources/prices.csv")
    val basket = createBasket(args.toList, pricesMap)

    val conditionalDiscounts = readCSVToConditionalDiscount(
      "src/main/resources/conditional_discounts.csv"
    )
    val countOfItems = countItemsInBasket(basket)

    val discounts =
      readCSVToDiscount("src/main/resources/discounts.csv", countOfItems)

    val basketCalculated = calculateDiscountedGoods(
      basket,
      pricesMap,
      conditionalDiscounts,
      discounts
    )

    logNoOffers(basketCalculated)

    println(s"Total price: £${getTotal(basketCalculated, discounted = true)}")
  }
}

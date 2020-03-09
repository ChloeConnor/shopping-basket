package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.{ConditionalDiscount, Discount}
import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.discounts.ApplyDiscounts._
import com.chloe.priceBasket.discounts.ConditionalDiscounts.convertConditionalDiscountsToDiscounts
import scala.math.BigDecimal.RoundingMode

object CalculateDiscountedGoods {

  def getTotalWithoutDiscount(goods: List[Good]): BigDecimal =
    BigDecimal(goods.map(d => d.price).sum)
      .setScale(2, RoundingMode.HALF_EVEN)

  def getTotalWithDiscount(goods: List[Good]): BigDecimal =
    BigDecimal(goods.map(d => d.discountedPrice).sum)
      .setScale(2, RoundingMode.HALF_EVEN)

  def discountsGenerate(discountsFiltered: List[Discount],
                        countOfItems: Map[String, Int]): List[Discount] =
    for {
      dis <- discountsFiltered
      _ <- 0 until countOfItems(dis.item)
    } yield {
      Discount(dis.item, dis.discount, dis.numberOfTimesToApply)
    }

  def generateCorrectNumberOfDiscounts(
      discounts: List[Discount],
      mapOfPrices: Map[String, Double],
      countOfItems: Map[String, Int]
  ): List[Discount] = {

    val maxNumberToBeApplied: List[Discount] = discounts
      .map(
        discount =>
          if (discount.numberOfTimesToApply > countOfItems(discount.item)) {
            discount.copy(
              numberOfTimesToApply = countOfItems(discount.item))
          } else {
            discount
        }
      )

    val allDiscounts = for {
      dis <- maxNumberToBeApplied
      _ <- 0 until dis.numberOfTimesToApply
    } yield {
      Discount(dis.item, dis.discount, dis.numberOfTimesToApply)
    }
    allDiscounts.foreach(dis => logDiscount(dis, mapOfPrices(dis.item)))

    allDiscounts
  }

  def calculateDiscountedGoods(initialBasket: List[Good],
                               pricesMap: Map[String, Double],
                               conditionalDiscounts: List[ConditionalDiscount],
                               discounts: List[Discount]): List[Good] = {

    println(s"Subtotal: £${getTotalWithoutDiscount(initialBasket)}")

    val allDiscounts = (discounts ::: convertConditionalDiscountsToDiscounts(
      initialBasket,
      conditionalDiscounts
    )) filter (d => initialBasket.map(g => g.name).contains(d.item))

    applyDiscountsToGoods(
      initialBasket,
      generateCorrectNumberOfDiscounts(
        allDiscounts,
        pricesMap,
        initialBasket.map(g => g.name).groupBy(identity).mapValues(_.size)
      )
    )
  }

}

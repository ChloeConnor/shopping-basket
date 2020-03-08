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

  def splitDiscounts(discounts: List[Discount],
                     mapOfPrices: Map[String, Double],
                     countOfItems: Map[String, Int]): List[Discount] = {

    val discountsApplyAsManyTimes: List[Discount] =
      discounts.filter(dis => dis.numberOfTimesToApply.isEmpty)

    val maxNumber = discounts
      .filter(dis => dis.numberOfTimesToApply.isDefined)
      .map(m =>
        if (m.numberOfTimesToApply.getOrElse(0) > countOfItems(m.item)) {
          m.copy(numberOfTimesToApply = Some(countOfItems(m.item)))
        } else {
          m
      })

    val discountsApplySetNumberOfTimes = for {
      dis <- maxNumber
      _ <- 0 until dis.numberOfTimesToApply.getOrElse(0)
      if dis.numberOfTimesToApply.isDefined
    } yield {
      Discount(dis.item, dis.discount, dis.numberOfTimesToApply)
    }

    val allDiscounts = discountsApplyAsManyTimes ::: discountsApplySetNumberOfTimes
    allDiscounts.foreach(
      dis => logDiscount(dis, mapOfPrices(dis.item))
    )

    allDiscounts
  }

  def calculateDiscountedGoods(items: List[String],
                               pricesMap: Map[String, Double],
                               conditionalDiscounts: List[ConditionalDiscount],
                               discounts: List[Discount]): List[Good] = {

    val initialBasket: List[Good] =
      items.map(item => Good(item, pricesMap(item), pricesMap(item)))

    println(s"Subtotal: £${getTotalWithoutDiscount(initialBasket)}")

    val allDiscounts = (discounts ::: convertConditionalDiscountsToDiscounts(
      initialBasket,
      conditionalDiscounts
    )) filter (d => initialBasket.map(g => g.name).contains(d.item))

    applyDiscountsToGoods(
      initialBasket,
      splitDiscounts(
        allDiscounts,
        pricesMap,
        initialBasket.map(g => g.name).groupBy(identity).mapValues(_.size))
    )
  }

}

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
                     mapOfPrices: Map[String, Double]): List[Discount] = {

    val discountsApplyAsManyTimes: List[Discount] =
      discounts.filter(dis => dis.numberOfTimesToApply.isEmpty)

    val discountsApplySetNumberOfTimes = for {
      dis <- discounts.filter(dis => dis.numberOfTimesToApply.isDefined)
      _ <- 0 until dis.numberOfTimesToApply.getOrElse(0)
      if dis.numberOfTimesToApply.isDefined
    } yield {
      logDiscount(dis, mapOfPrices(dis.item))
      Discount(dis.item, dis.discount, dis.numberOfTimesToApply)
    }

    discountsApplyAsManyTimes ::: discountsApplySetNumberOfTimes
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

    applyDiscountsToGoods(initialBasket,
                          splitDiscounts(allDiscounts, pricesMap))
  }

}

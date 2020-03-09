package com.chloe.priceBasket.utils

import com.chloe.priceBasket.dataTypes.Discount.Discount
import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.discounts.CalculateDiscountedGoods.getTotal

import scala.math.BigDecimal.RoundingMode

object Logging {
  def logNoOffers(basket: List[Good]): Unit =
    if (getTotal(basket, discounted = false) == getTotal(
          basket,
          discounted = true
        )) println("(No offers available)")

  def logDiscount(discount: Discount, oldPrice: Double): Unit = {
    val savings = oldPrice - (oldPrice * (1 - discount.discount))

    println(
      discount.item + " " + (discount.discount * 100) + "% off: " + (BigDecimal(
        savings
      ) * 100)
        .setScale(0, RoundingMode.HALF_EVEN) + "p"
    )
  }
}

package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount.{ConditionalDiscount, Discount}
import com.chloe.priceBasket.dataTypes.Good

import scala.math.BigDecimal.RoundingMode

object ApplyDiscounts {

  def applyDiscount(goodInput: Good, discounts: List[Discount]): Good = {

    val discountExists = discounts
      .exists(d => d.item == goodInput.name)

    if (discountExists) {
      val discount = discounts
        .find(d => d.item == goodInput.name)
        .get
        .discount

      val newPrice: Double = goodInput.price * (1 - discount)
      val diff = goodInput.price - newPrice
      println(
        goodInput.name + ": " + (discount * 100) + "% off. " + BigDecimal(diff)
          .setScale(2, RoundingMode.HALF_EVEN) + "p")

      goodInput.copy(discountedPrice = newPrice)
    } else {
      goodInput
    }
  }

  def applyConditionalDiscount(
      goodsInBasket: List[Good],
      discounts: List[ConditionalDiscount]
  ): List[Discount] = {

    val howManyOfEachGood =
      goodsInBasket.map(g => g.name).groupBy(identity).mapValues(_.size)

    val discountsFiltered = discounts.filter { d =>
      {
        val required =
          d.condition.goodsRequired.groupBy(identity).mapValues(_.size)
        required.toSet.subsetOf(howManyOfEachGood.toSet)
      }
    }
    discountsFiltered.map(d => Discount(d.item, d.discount))
  }
}

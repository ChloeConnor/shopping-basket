package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount.{ConditionalDiscount, Discount}
import com.chloe.priceBasket.dataTypes.Good

object ApplyDiscounts {

  def applyDiscount(goodInput: Good, discounts: List[Discount]): Good =
    Good(
      goodInput.name,
      goodInput.price,
      goodInput.price * (1 - discounts
        .find(d => d.item == goodInput.name)
        .getOrElse(Discount("", 0))
        .discount)
    )

  def applyConditionalDiscount(
    goodsInBasket: List[Good],
    discounts: List[ConditionalDiscount]
  ): List[Good] = {

    val howManyOfEachGood =
      goodsInBasket.map(g => g.name).groupBy(identity).mapValues(_.size)

    val applicableDiscounts: List[Discount] = discounts.map { d =>
      val required =
        d.condition.goodsRequired.groupBy(identity).mapValues(_.size)
      if (required.toSet.subsetOf(howManyOfEachGood.toSet)) {
        Discount(d.item, d.discount)
      } else {
        Discount(d.item, 0.0)
      }
    }

    goodsInBasket.map(good => applyDiscount(good, applicableDiscounts))
  }
}

package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.{ConditionalDiscount, Discount}
import com.chloe.priceBasket.dataTypes.Good

object ConditionalDiscounts {

  private def groupGoodsWithQuantity(goods: List[Good]): Map[String, Int] =
    goods.map(g => g.name).groupBy(identity).mapValues(_.size)

  /**
    * Checks whether a conditional discount is applicable
    * based on items in the basket, and if so converts to a
    * discount
    */
  def convertConditionalDiscountsToDiscounts(
      goodsInBasket: List[Good],
      discounts: List[ConditionalDiscount]
  ): List[Discount] = {

    val howManyOfEachGood =
      groupGoodsWithQuantity(goodsInBasket)

    val discountsFiltered = discounts.filter { d =>
      {
        val required =
          d.condition.goodsRequired.groupBy(identity).mapValues(_.size)
        howManyOfEachGood
          .filter(a => required.contains(a._1))
          .values
          .head > required.values.head
      }
    }
    discountsFiltered.map(d => Discount(d.item, d.discount))
  }
}

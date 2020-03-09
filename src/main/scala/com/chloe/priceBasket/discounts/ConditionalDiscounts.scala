package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.{ConditionalDiscount, Discount}
import com.chloe.priceBasket.dataTypes.Good

object ConditionalDiscounts {

  private def groupGoodsWithQuantity(goods: List[Good]): Map[String, Int] =
    goods.map(g => g.name).groupBy(identity).mapValues(_.size)

  private def filterDiscounts(
    numberOfGoodsRequired: Map[String, Int],
    numberOfGoodsInBasket: Map[String, Int]
  ): Boolean = {
    numberOfGoodsRequired
      .filter(
        requiredGood =>
          requiredGood._2 <= numberOfGoodsInBasket
            .filter(
              numberOfGood => numberOfGoodsRequired.contains(numberOfGood._1)
            )
            .getOrElse(requiredGood._1, 0)
      )
      .equals(numberOfGoodsRequired)
  }

  /**
    * Checks whether a conditional discount is applicable
    * based on items in the basket, and if so converts to a
    * discount
    */
  def processConditionalDiscounts(
    goodsInBasket: List[Good],
    discounts: List[ConditionalDiscount]
  ): List[Discount] = {

    val numberOfGoodsInBasket: Map[String, Int] =
      groupGoodsWithQuantity(goodsInBasket)

    discounts
      .filter { conditionalDiscount =>
        {
          val numberOfGoodRequired: Map[String, Int] =
            conditionalDiscount.condition.countValues

          filterDiscounts(numberOfGoodRequired, numberOfGoodsInBasket)
        }
      }
      .map(d => {
        val numberRequired = d.condition.goodsRequired.size
        val numberInBasket =
          numberOfGoodsInBasket(d.condition.goodsRequired.head)
        Discount(d.item, d.discount, numberInBasket / numberRequired)
      })
  }
}

package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.Discount
import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.PriceBasket._

object ApplyDiscounts {

  def applyAllDiscounts(basket: List[Good],
                        discounts: List[Discount]): List[Good] = {
    discounts
      .filter(d => basket.map(g => g.name).contains(d.item))
      .map(dis => {
        val priceGood = basket
          .find(b => { b.name == dis.item && b.price == b.discountedPrice })
          .get
          .price
        Good(dis.item, priceGood, priceGood * (1 - dis.discount))
      })
  }

  def findNumberOfEachItemNotDiscounted(
    basket: List[Good],
    discountedGoods: List[Good]
  ): Map[String, Int] = {

    def howMany(item: String) =
      countItemsInBasket(discountedGoods).getOrElse(item, 0)

    countItemsInBasket(basket)
      .flatMap(h => Map(h._1 -> (h._2 - howMany(h._1))))
      .filter(m => m._2 > 0)
  }

  def getBasketWithDiscountsApplied(basket: List[Good],
                                    discounts: List[Discount]): List[Good] = {

    val discountedGoods = applyAllDiscounts(basket, discounts)

    val itemsNotDiscounted: Map[String, Int] =
      findNumberOfEachItemNotDiscounted(basket, discountedGoods)

    val goodsWithoutDiscountsApplied = (for {
      itemNotDiscounted <- itemsNotDiscounted
      priceGood = basket
        .find(item => item.name == itemNotDiscounted._1)
        .get
        .price
      _ <- 0 until itemNotDiscounted._2
    } yield Good(itemNotDiscounted._1, priceGood, priceGood)).toList

    discountedGoods ::: goodsWithoutDiscountsApplied
  }

}

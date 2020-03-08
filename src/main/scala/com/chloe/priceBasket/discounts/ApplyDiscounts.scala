package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.Discount
import com.chloe.priceBasket.dataTypes.Good
import scala.collection.immutable
import scala.math.BigDecimal.RoundingMode

object ApplyDiscounts {

  def logDiscount(discount: Discount, oldPrice: Double): Unit = {
    val savings = oldPrice - (oldPrice * (1 - discount.discount))

    println(
      discount.item + " " + (discount.discount * 100) + "% off: " + (BigDecimal(
        savings
      ) * 100)
        .setScale(0, RoundingMode.HALF_EVEN) + "p"
    )
  }

  def applyAllDiscounts(basket: List[Good],
                        discounts: List[Discount]): List[Good] =
    discounts
      .filter(d => basket.map(g => g.name).contains(d.item))
      .map(dis => {
        val priceGood = basket.find(b => b.name == dis.item).get.price
        Good(dis.item, priceGood, priceGood * (1 - dis.discount))
      })

  def findNumberOfEachItemNotDiscounted(
    basket: List[Good],
    discountedGoods: List[Good]
  ): Map[String, Int] =
    basket
      .groupBy(g => g.name)
      .mapValues(_.size)
      .flatMap(
        h =>
          Map(
            h._1 -> (h._2 - discountedGoods
              .groupBy(g => g.name)
              .mapValues(_.size)
              .getOrElse(h._1, 0))
        )
      )
      .filter(m => m._2 > 0)

  def applyDiscountsToGoods(basket: List[Good],
                            discounts: List[Discount]): List[Good] = {

    val discountedGoods = applyAllDiscounts(basket, discounts)

    val itemsNotDiscounted: Map[String, Int] =
      findNumberOfEachItemNotDiscounted(basket, discountedGoods)

    val goodsWithoutDiscountsApplied = (for {
      m <- itemsNotDiscounted
      priceGood = basket.find(b => b.name == m._1).get.price
      _ <- 0 until m._2
    } yield Good(m._1, priceGood, priceGood)).toList

    discountedGoods ::: goodsWithoutDiscountsApplied
  }

}

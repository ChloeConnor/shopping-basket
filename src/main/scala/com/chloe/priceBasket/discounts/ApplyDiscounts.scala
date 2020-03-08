package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.Discount
import com.chloe.priceBasket.dataTypes.Good
import scala.math.BigDecimal.RoundingMode

object ApplyDiscounts {

  def logDiscount(discount: Discount, oldPrice: Double): Unit = {
    val savings = oldPrice - (oldPrice * (1 - discount.discount))

    println(
      discount.item + " " + (discount.discount * 100) + "% off: " + (BigDecimal(
        savings) * 100)
        .setScale(0, RoundingMode.HALF_EVEN) + "p"
    )
  }

  def applyDiscountsToGoods(basket: List[Good],
                            discounts: List[Discount]): List[Good] = {

    val discountedGoods = discounts
      .filter(d => basket.map(g => g.name).contains(d.item))
      .map(dis => {
        val priceGood = basket.find(b => b.name == dis.item).get.price
        Good(dis.item, priceGood, priceGood * (1 - dis.discount))
      })

    val countInBasket = basket.groupBy(g => g.name).mapValues(_.size)

    val countOfDiscounts =
      discountedGoods.groupBy(g => g.name).mapValues(_.size)

    val itemsNotDiscounted: Map[String, Int] =
      countInBasket
        .flatMap(h => Map(h._1 -> (h._2 - countOfDiscounts.getOrElse(h._1, 0))))
        .filter(m => m._2 > 0)

    var some: List[Good] = List.empty[Good]

    itemsNotDiscounted.foreach(m => {
      val priceGood = basket.find(b => b.name == m._1).get.price

      for (_ <- 0 until m._2) {
        some = some ::: List(Good(m._1, priceGood, priceGood))
      }
    })

    discountedGoods ::: some
  }

}

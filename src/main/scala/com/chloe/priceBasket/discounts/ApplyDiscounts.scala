package com.chloe.priceBasket.discounts

import com.chloe.priceBasket.dataTypes.Discount.Discount
import com.chloe.priceBasket.dataTypes.Good
import scala.math.BigDecimal.RoundingMode

object ApplyDiscounts {

  def logDiscount(discount: Double, good: Good, newPrice: Double): Unit = {
    val savings = good.price - newPrice

    println(
      good.name + " " + (discount * 100) + "% off: " + (BigDecimal(savings) * 100)
        .setScale(0, RoundingMode.HALF_EVEN) + "p"
    )
  }

  def applyDiscountToGood(goodInput: Good, discounts: List[Discount]): Good = {

    println(discounts)

    if (discounts.exists(dis => dis.item == goodInput.name)) {

      val discount = discounts
        .find(d => d.item == goodInput.name)
        .get
        .discount

      val newPrice = goodInput.price * (1 - discount)

      logDiscount(discount, goodInput, newPrice)

      goodInput.copy(discountedPrice = newPrice)

    } else {
      goodInput
    }
  }

}

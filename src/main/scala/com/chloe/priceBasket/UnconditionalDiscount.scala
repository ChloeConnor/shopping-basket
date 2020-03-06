package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount.Discount
import com.chloe.priceBasket.dataTypes.Good

object UnconditionalDiscount {

  def applyDiscount(goodInput: Good, discounts: Set[Discount]): Good =
    Good(
      goodInput.name,
      goodInput.price,
      goodInput.price * (1 - discounts
        .find(d => d.item == goodInput.name)
        .getOrElse(Discount("", 0))
        .discount)
    )
}

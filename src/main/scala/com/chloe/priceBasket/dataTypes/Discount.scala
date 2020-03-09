package com.chloe.priceBasket.dataTypes

object Discount {

  trait DiscountGeneric {
    val item: String
    val discount: Double
  }

  case class ConditionalDiscount(item: String,
                                 discount: Double,
                                 condition: Condition)
      extends DiscountGeneric

  case class Discount(item: String, discount: Double, numberOfTimesToApply: Int)
      extends DiscountGeneric

  case class Condition(goodsRequired: List[String])
}

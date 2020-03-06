package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount._
import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.UnconditionalDiscount.applyDiscount
import org.scalatest.FlatSpec

class TestDiscount extends FlatSpec {

  "Discount" should "be applied correctly" in {

    val appleDis = Discount("apple", 0.1)

    val discounts: Set[Discount] = Set(appleDis)

    val actual = applyDiscount(Good("apple", 1.0, 1.0), discounts)
    val expected = 0.9
    assert(actual.discountedPrice === expected)
  }
}

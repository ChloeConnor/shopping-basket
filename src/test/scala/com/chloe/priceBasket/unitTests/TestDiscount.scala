package com.chloe.priceBasket.unitTests

import com.chloe.priceBasket.PriceBasket.discount
import org.scalatest.FlatSpec

class TestDiscount extends FlatSpec {

  "Discount" should "be applied correctly" in {
    val goods =
      Map("soup" -> 0.65, "bread" -> 0.8, "milk" -> 1.3, "apple" -> 1.0)

    val discountsMap = Map("apple" -> 0.1)

    val actual = discount("apple", discountsMap, goods)
    val expected = 0.9
    assert(actual === expected)
  }
}

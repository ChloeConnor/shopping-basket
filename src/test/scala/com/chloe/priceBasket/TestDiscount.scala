package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount._
import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.ApplyDiscounts.applyDiscount
import com.chloe.priceBasket.ApplyDiscounts.applyConditionalDiscount
import org.scalatest.FlatSpec

class TestDiscount extends FlatSpec {

  "Discounts" should "be applied correctly for unconditional" in {

    val discounts: List[Discount] = List(Discount("Apples", 0.1))

    val actual = applyDiscount(Good("Apples", 1.0, 1.0), discounts)
    val expected = 0.9
    assert(actual.discountedPrice === expected)
  }

  it should "be applied when conditional" in {

    val goodsInBasket = List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8)
    )

    val expectedGoods = List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.4)
    )

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples"))))
    val actual = applyConditionalDiscount(goodsInBasket, conditionalDiscounts)

    println(actual)
    assert(actual === expectedGoods)
  }

  it should "be applied when multiple conditional discounts" in {

    val goodsInBasket = List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8),
      Good("pear", 2.0, 2.0),
      Good("banana", 1.8, 1.8),
    )

    val expectedGoods = List(
      Good("Apple", 1.0, 1.0),
      Good("Apple", 1.0, 1.0),
      Good("Bread", 0.8, 0.4),
      Good("pear", 2.0, 1.6),
      Good("banana", 1.8, 1.8),
    )

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples"))),
        ConditionalDiscount("pear", 0.2, Condition(List("banana")))
      )
    val actual = applyConditionalDiscount(goodsInBasket, conditionalDiscounts)

    assert(actual === expectedGoods)
  }

}

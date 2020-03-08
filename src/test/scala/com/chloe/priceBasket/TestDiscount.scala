package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount._
import com.chloe.priceBasket.dataTypes.Good
import com.chloe.priceBasket.discounts.ApplyDiscounts.applyDiscountToGood
import com.chloe.priceBasket.discounts.ConditionalDiscounts.convertConditionalDiscountsToDiscounts
import org.scalatest.FlatSpec

import scala.io.{BufferedSource, Source}

class TestDiscount extends FlatSpec {

  "Discounts" should "be applied correctly for unconditional" in {

    val discounts: List[Discount] = List(Discount("Apples", 0.1))

    val actual = applyDiscountToGood(Good("Apples", 1.0, 1.0), discounts)
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
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples")))
      )

    val allDiscounts = convertConditionalDiscountsToDiscounts(
      goodsInBasket,
      conditionalDiscounts
    )
    val actual =
      goodsInBasket.map(good => applyDiscountToGood(good, allDiscounts))

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
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.4),
      Good("pear", 2.0, 1.6),
      Good("banana", 1.8, 1.8),
    )

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples"))),
        ConditionalDiscount("pear", 0.2, Condition(List("banana")))
      )

    val allDiscounts = convertConditionalDiscountsToDiscounts(
      goodsInBasket,
      conditionalDiscounts
    )
    val actual =
      goodsInBasket.map(good => applyDiscountToGood(good, allDiscounts))

    assert(actual === expectedGoods)
  }

  it should "be applied when multiple different goods are required" in {

    val goodsInBasket = List(
      Good("Apples", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Bread", 0.8, 0.8)
    )

    val expectedGoods = List(
      Good("Apples", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Bread", 0.8, 0.4)
    )

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Soup"))))

    val allDiscounts: List[Discount] =
      convertConditionalDiscountsToDiscounts(
        goodsInBasket,
        conditionalDiscounts
      )

    val actual =
      goodsInBasket.map(good => applyDiscountToGood(good, allDiscounts))

    assert(actual === expectedGoods)
  }

  it should "only apply conditional discount to item once" in {

    val goodsInBasket = List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8),
      Good("Bread", 0.8, 0.8)
    )

    val expectedGoods = List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.4),
      Good("Bread", 0.8, 0.4)
    )

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples")))
      )

    val allDiscounts: List[Discount] =
      convertConditionalDiscountsToDiscounts(
        goodsInBasket,
        conditionalDiscounts
      )

    println(allDiscounts)

    val actual =
      goodsInBasket.map(good => applyDiscountToGood(good, allDiscounts))

    assert(actual === expectedGoods)
  }

}

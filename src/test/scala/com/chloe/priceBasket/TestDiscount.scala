package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount._
import com.chloe.priceBasket.dataTypes.{Basket, Good}
import com.chloe.priceBasket.discounts.ApplyDiscounts._
import com.chloe.priceBasket.discounts.ConditionalDiscounts.processConditionalDiscounts
import org.scalatest.FlatSpec

class TestDiscount extends FlatSpec {

  it should "be applied when conditional" in {

    val goodsInBasket = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8)
    ))

    val expectedGoods = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.4)
    ))

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples")))
      )

    val allDiscounts =
      processConditionalDiscounts(goodsInBasket, conditionalDiscounts)
    val actual =
      getBasketWithDiscountsApplied(goodsInBasket, allDiscounts)

    assert(actual.equals(expectedGoods))
  }

  it should "be applied when multiple conditional discounts" in {

    val goodsInBasket = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8),
      Good("pear", 2.0, 2.0),
      Good("banana", 1.8, 1.8),
    ))

    val expectedGoods = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.4),
      Good("pear", 2.0, 1.6),
      Good("banana", 1.8, 1.8),
    ))

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples"))),
        ConditionalDiscount("pear", 0.2, Condition(List("banana")))
      )

    val allDiscounts =
      processConditionalDiscounts(goodsInBasket, conditionalDiscounts)
    val actual =
      getBasketWithDiscountsApplied(goodsInBasket, allDiscounts)

    assert(actual.equals(expectedGoods))
  }

  it should "be applied when multiple different goods are required for discount to apply" in {

    val goodsInBasket = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Bread", 0.8, 0.8)
    ))

    val expectedGoods = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Bread", 0.8, 0.4)
    ))

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Soup"))))

    val allDiscounts: List[Discount] =
      processConditionalDiscounts(goodsInBasket, conditionalDiscounts)

    val actual =
      getBasketWithDiscountsApplied(goodsInBasket, allDiscounts)

    assert(actual.equals(expectedGoods))
  }

  it should "only apply conditional discount to item once" in {

    val goodsInBasket = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8),
      Good("Bread", 0.8, 0.8)
    ))

    val expectedGoods = Basket(List(
      Good("Apples", 1.0, 1.0),
      Good("Apples", 1.0, 1.0),
      Good("Bread", 0.8, 0.8),
      Good("Bread", 0.8, 0.4)
    ))

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples")))
      )

    val allDiscounts: List[Discount] =
      processConditionalDiscounts(goodsInBasket, conditionalDiscounts)

    val applyNew =
      getBasketWithDiscountsApplied(goodsInBasket, allDiscounts)

    assert(expectedGoods.equals(applyNew))
  }

  it should "calculate normal price if no discounts applicable" in {

    val goodsInBasket = Basket(List(Good("Soup", 1.0, 1.0), Good("Apples", 1.0, 1.0)))

    val expectedGoods = Basket(List(Good("Soup", 1.0, 1.0), Good("Apples", 1.0, 1.0)))

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(
        ConditionalDiscount("Bread", 0.5, Condition(List("Apples", "Apples")))
      )

    val allDiscounts: List[Discount] =
      processConditionalDiscounts(goodsInBasket, conditionalDiscounts)

    val applyNew =
      getBasketWithDiscountsApplied(goodsInBasket, allDiscounts)

    assert(applyNew.equals(expectedGoods))

  }

  it should "generate the required number of discounts" in {

    val goodsInBasket = Basket(List(
      Good("Bread", 0.8, 0.8),
      Good("Bread", 0.8, 0.8),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0),
      Good("Soup", 1.0, 1.0)
    ))

    val conditionalDiscounts: List[ConditionalDiscount] =
      List(ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup"))))

    val allDiscounts: List[Discount] =
      processConditionalDiscounts(goodsInBasket, conditionalDiscounts)

    assert(allDiscounts.head.numberOfTimesToApply == 3)
  }
}

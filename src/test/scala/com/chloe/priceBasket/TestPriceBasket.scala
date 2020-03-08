package com.chloe.priceBasket

import com.chloe.priceBasket.dataTypes.Discount.{
  Condition,
  ConditionalDiscount,
  Discount
}
import com.chloe.priceBasket.discounts.CalculateDiscountedGoods.{
  calculateDiscountedGoods,
  getTotalWithDiscount,
  getTotalWithoutDiscount
}
import org.scalatest.FlatSpec

class TestPriceBasket extends FlatSpec {

  "Total price" should "be calculated correctly" in {
    val pricesMap =
      Map("Soup" -> 0.65, "Bread" -> 0.8, "Milk" -> 1.3, "Apples" -> 1.0)

    val twoSoupDiscountBread: ConditionalDiscount =
      ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

    val discountOnApples: Discount = Discount("Apples", 0.1)

    val basket = List("Apples", "Milk", "Bread")

    val goodsCalculated = calculateDiscountedGoods(
      basket,
      pricesMap,
      List(twoSoupDiscountBread),
      List(discountOnApples)
    )

    val totalWithDiscount = getTotalWithDiscount(goodsCalculated)
    val totalWithoutDiscount = getTotalWithoutDiscount(goodsCalculated)

    assert(totalWithoutDiscount == 3.10)
    assert(totalWithDiscount == 3.00)
  }

}

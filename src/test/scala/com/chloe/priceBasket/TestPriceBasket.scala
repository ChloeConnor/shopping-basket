package com.chloe.priceBasket

import org.scalatest.FlatSpec
import com.chloe.priceBasket.PriceBasket.calculateBasketTotal
import com.chloe.priceBasket.dataTypes.Discount.{
  Condition,
  ConditionalDiscount,
  Discount
}

import scala.math.BigDecimal.RoundingMode

class TestPriceBasket extends FlatSpec {

  "Total price" should "be calculated correctly" in {
    val pricesMap =
      Map("Soup" -> 0.65, "Bread" -> 0.8, "Milk" -> 1.3, "Apple" -> 1.0)

    val twoSoupDiscountBread: ConditionalDiscount =
      ConditionalDiscount("Bread", 0.5, Condition(List("Soup", "Soup")))

    val discountOnApples: Discount = Discount("Apple", 0.1)

    val basket = List("Apple", "Milk", "Bread")

    val goodsCalculated = calculateBasketTotal(
      basket,
      pricesMap,
      List(twoSoupDiscountBread),
      List(discountOnApples)
    )

    val totalWithoutDiscount = goodsCalculated.map(d => d.price).sum
    val totalWithDiscount = goodsCalculated.map(d => d.discountedPrice).sum

    assert(totalWithoutDiscount == (1.0 + 1.3 + 0.8))
    assert(totalWithDiscount == (0.9 + 1.3 + 0.8))
  }

}

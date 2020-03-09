package com.chloe.priceBasket.dataTypes

case class Good(name: String, price: Double, discountedPrice: Double) {

  def getCost(discounted: Boolean): Double =
    if (discounted) this.discountedPrice
    else this.price
}

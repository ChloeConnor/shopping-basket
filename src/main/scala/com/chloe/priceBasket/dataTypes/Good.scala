package com.chloe.priceBasket.dataTypes

case class Good(name: String, price: Double, discountedPrice: Double) {
  def discountAppliedAlready: Boolean = price - discountedPrice == 0
}

# shopping-basket

## How to run:
1. clone repo
2. from the root directory run 
`java -cp project/shopping-basket-assembly-0.1.jar com.chloe.priceBasket.PriceBasket [your items here]`

example:
`java -cp project/shopping-basket-assembly-0.1.jar com.chloe.priceBasket.PriceBasket Apples`

should return

```$xslt
Subtotal: £1.00
Apples 10.0% off: 10p
Total price: £0.90
```

## Requirements:
Write a program driven by unit tests that can price a basket of goods taking into account some special offers.
<p>
The goods that can be purchased, together with their normal prices are:
<p>

- Soup – 65p per tin
- Bread – 80p per loaf
- Milk – £1.30 per bottle
- Apples – £1.00 per bag
<p>
Current special offers

- Apples have a 10% discount off their normal price this week
- Buy 2 tins of Soup and get a loaf of Bread for half price
<p>
The program should accept a list of items in the basket and output the subtotal, the special offer discounts and the final price.
Input should be via the command line in the form PriceBasket item1 item2 item3 ...
For example

`PriceBasket Apples Milk Bread`

Output should be to the console, for example:
```
Subtotal: £3.10
Apples 10% off: 10p
Total price: £3.00
```
If no special offers are applicable the code should output:

```
Subtotal: £1.30
(No offers available)
Total price: £1.30
```

## Further considerations:

- if there is more than one conditional offer on the same item, the best offer won't necessarily be picked
- if there are two unconditional discounts on the same item both will be applied (but this case seems unlikely)
# Algo Trading Assessment -logic  #
The algorithm monitors the spread between bid and ask prices.
Places buy and sell orders to take advantage of the spread when conditions are favorable.
Cancels any orders that are not aligned with the best market prices to stay competitive and keep its orders close to optimal prices.


# Algo unitTest covers #
1. Verifies that the algorithm creates at least 3 buy orders when conditions are met
2. Check that at least 3 sell orders are created when conditions are met
3. Ensure that buy orders are cancelled if they do not meet the best bid price condition
4. Ensures that sell orders are cancelled if they do not meet the best ask price condition
5. Tests that no action is taken when the spread is below the specified threashold 


# Algo BackTesting covers #
1. Sending multiple markets data ticks
2. Verify the creations of buy and sell orders
3. Checking cancellations of orders
4. Asserting filled quantity and ensuring the state is updated accordingly


  # UI-Front-end Exercise #
  Components added are:
  1. MarketDepthData.tsx
  2. PriceCell.tsx
  3. Quantity.tsx
     
 ![image](https://github.com/user-attachments/assets/cc46ef80-3d7b-4f2a-af06-64c5710a0b2a)

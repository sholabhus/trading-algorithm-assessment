package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*This Algo is a market making strategy that
Monitors the bid-ask spread
Places buy and sell orders to profits from the spread when it's favorable
Cancel orders that are not aligned with the best market price to ensure it remains competitive and maintains orders to close to the optimal prices
 *
 */


public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);
    private static final String ANSI_RED = "\033[31m";  // Red color for cancellations
    private static final String ANSI_GREEN = "\033[32m";  // Green color for successful actions
    private static final String ANSI_RESET = "\033[0m";  // Reset to default color
    private static final String ANSI_BLUE = "\u001B[34m";

    private static final int MAX_TOTAL_ORDER_COUNT = 10;
    private static final int MAX_BUY_ORDER_COUNT = 5;
    private static final int MAX_SELL_ORDER_COUNT = 5;
    private static final long ASK_PRICE_THRESHOLD = 110;
    private static final long SPREAD_THRESHOLD = -8; // use spread threshold to make decision

    @Override
    public Action evaluate(SimpleAlgoState state) {

        var orderBookAsString = Util.orderBookToString(state);

        logger.info(ANSI_BLUE + "[MYALGO] The state of the order book is:\n" + orderBookAsString + ANSI_RESET);
        logger.info("########### CURRENT ORDERS: " + state.getActiveChildOrders().toString());

        var totalOrderCount = state.getChildOrders().size();

        // make sure we have an exit condition...
        if (totalOrderCount > MAX_TOTAL_ORDER_COUNT) {
            return NoAction.NoAction;
        }

        final BidLevel bidLevel = state.getBidAt(0);
        final long bestBidPrice = bidLevel.price;
        final long bidQuantity = bidLevel.quantity;

        final AskLevel askLevel = state.getAskAt(0);
        final long bestAskPrice = askLevel.price;
        final long askQuantity = askLevel.quantity;

        // calculate the spread
        final long spread = bestAskPrice - bestBidPrice;
        logger.info(String.format(ANSI_GREEN + "[MYALGO] Bid–Ask Spread: " + spread + ANSI_RESET));

        // Don't place or cancel orders if spread is too small
        if (spread < SPREAD_THRESHOLD) {
            logger.info(String.format(ANSI_GREEN + "[MYALGO] Bid–Ask Spread is too small." + spread + " No Action taken"));
            return NoAction.NoAction;
        }

        // separate child orders into BUY AND SELL
        long buyOrdersCount = state.getChildOrders().stream().filter(ChildOrder -> ChildOrder.getSide() == Side.BUY).count();
        long sellOrdersCount = state.getChildOrders().stream().filter(ChildOrder -> ChildOrder.getSide() == Side.SELL).count();

        // create new BUY order if fewer than 5 BUY orders exist
        if (buyOrdersCount < MAX_BUY_ORDER_COUNT) {
            return createBuyOrder(state, buyOrdersCount, bidQuantity, bestBidPrice);
        }

        // create new SELL order if fewer than  5 SELL orders exist
        if (sellOrdersCount < MAX_SELL_ORDER_COUNT) {
            return createSellOrder(state, sellOrdersCount, askQuantity, bestAskPrice);
        }

       // cancel orders that don't match the best price
        return cancelOrders(state, bestBidPrice, ASK_PRICE_THRESHOLD);
    }

    public Action createBuyOrder (SimpleAlgoState state, long buyOrdersCount, long bidQuantity, long bestBidPrice){
        logger.info("BUY ORDER COUNT: "  + buyOrdersCount );
        // ALL IN NEW METHOD CALLED createBuyOrder
        logger.info(String.format(ANSI_GREEN + "[MYALGO] Have " + state.getChildOrders().size() + " orders. ADD NEW BUY ORDER " + bidQuantity + " @ " + bestBidPrice + ANSI_RESET));
        // int initialOrderSize = state.getChildOrders().size();// capture current size of order before creating new one
        logger.info(state.getActiveChildOrders().toString());
        return new CreateChildOrder(Side.BUY, bidQuantity, bestBidPrice); // Create a new BUY order
    }

    public Action createSellOrder (SimpleAlgoState state, long sellOrdersCount, long askQuantity, long bestAskPrice){
        logger.info("SELL ORDER COUNT: "  + sellOrdersCount);
        // ALL IN NEW METHOD CALLED createBuyOrder
        logger.info(String.format(ANSI_GREEN + "[MYALGO] Have " + state.getChildOrders().size() + " orders. ADD NEW SELL ORDER " + askQuantity + " @ " + bestAskPrice + ANSI_RESET));
        //int initialOrderSize = state.getChildOrders().size();// capture current size of order before creating new one
        return new CreateChildOrder(Side.SELL, askQuantity, bestAskPrice); // Create a new child order
    }

    public Action cancelOrders(SimpleAlgoState state,long bestBidPrice, long askPriceThreshold){
        //Cancel the First BUY order with a price less than the BestBid or not matching the Bestbid.

        for (ChildOrder order : state.getActiveChildOrders()) {
            logger.info("Order ID: #" + order.getOrderId() + ", Side: " + order.getSide() + ", Price: " + order.getPrice() + ", Quantity " + order.getQuantity());

            //handle buy orders
            boolean orderIsBuy = order.getSide() == Side.BUY;
            boolean orderNotBestBidPrice = order.getPrice() != bestBidPrice;
            boolean orderLessThanBestBid = order.getPrice() < bestBidPrice;
            // if the order's price is not matching or is less than the BestBid, cancel it

            if (orderIsBuy && (orderNotBestBidPrice || orderLessThanBestBid)) {
                logger.info(String.format(ANSI_RED + "[MYALGO] Cancelling BUY order #%d with price %d and quantity %d. The current best price is %d." + ANSI_RESET, order.getOrderId(), order.getPrice(), order.getQuantity(), bestBidPrice));
                logger.info(state.getActiveChildOrders().toString());
                return new CancelChildOrder(order);

            }
             //handle sell orders
            boolean orderIsSell = order.getSide() == Side.SELL;
            boolean orderLessThanThreshold = order.getPrice() <= askPriceThreshold;

            // If the order's price is not matching the threshold or is less than BestAsk, cancel it
            if (orderIsSell && orderLessThanThreshold) {
                logger.info(String.format(ANSI_RED + " The Threshold is %d." + ANSI_RESET,  askPriceThreshold));
                logger.info(String.format(ANSI_RED + "[MYALGO] Cancelling SELL order #%d with price %d and quantity %d." + ANSI_RESET, order.getOrderId(), order.getPrice(), order.getQuantity()));
                logger.info(state.getActiveChildOrders().toString());
                return new CancelChildOrder(order);

            }
        }
        //If no actions are taken, return NoAction
        return NoAction.NoAction;

    }


}

















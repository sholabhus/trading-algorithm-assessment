package codingblackfemales.gettingstarted;

import codingblackfemales.action.Action;
import codingblackfemales.action.CancelChildOrder;
import codingblackfemales.action.CreateChildOrder;
import codingblackfemales.action.NoAction;
import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.orderbook.channel.MarketDataChannel;
import codingblackfemales.service.MarketDataService;
import codingblackfemales.service.OrderService;
import codingblackfemales.sotw.ChildOrder;
import codingblackfemales.sotw.OrderState;
import codingblackfemales.sotw.SimpleAlgoState;
import codingblackfemales.sotw.marketdata.AskLevel;
import codingblackfemales.sotw.marketdata.BidLevel;
import codingblackfemales.util.Util;
import messages.order.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.plaf.nimbus.State;

public class MyAlgoLogic implements AlgoLogic {

    private static final Logger logger = LoggerFactory.getLogger(MyAlgoLogic.class);

    //private static final long SELL_THRESHOLD = 500; // SELL when price goes above this

    @Override
    public Action evaluate(SimpleAlgoState state) {

        var orderBookAsString = Util.orderBookToString(state);

        logger.info("[MYALGO] The state of the order book is:\n" + orderBookAsString);


        final BidLevel bidLevel = state.getBidAt(0);
        final long price = bidLevel.price;
        final long quantity = bidLevel.quantity;

        final AskLevel askLevel = state.getAskAt(0);
        final long askprice = askLevel.price;
        final long askquantity = askLevel.quantity;
//        var totalOrderCount = state.getChildOrders().size();
//
//        if (totalOrderCount > 1) {
//            return NoAction.NoAction;
//        }

//        //if (state.getActiveChildOrders().) {
//            // Check if there are existing orders to cancel
//            ChildOrder orderToCancel = state.getChildOrders().get(0); // Cancel the first order
//            long orderId = orderToCancel.getOrderId();// get the first orderID
//            logger.info("Cancel Order with ID #" + orderId  + " . Current state; Cancelled");
//            return new CancelChildOrder(orderToCancel);
//
//     }

         if(state.getChildOrders().size() < 5) {
          // If less than 3 child orders, create a new order
            logger.info("[PASSIVEALGO] Have: " + state.getChildOrders().size() + " orders. ADD New Order " + quantity + " @ " + price);
           int initialOrderSize =state.getChildOrders().size();// capture current size of order before creating new one
            return new CreateChildOrder(Side.BUY, quantity, price); // Create a new child order
        }

// If no actions are taken, return NoAction
        return NoAction.NoAction;
    }
}















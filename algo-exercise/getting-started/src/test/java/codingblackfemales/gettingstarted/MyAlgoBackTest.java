package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.ChildOrder;
import messages.order.Side;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test plugs together all of the infrastructure, including the order book (which you can trade against)
 * and the market data feed.
 *
 * If your algo adds orders to the book, they will reflect in your market data coming back from the order book.
 *
 * If you cross the srpead (i.e. you BUY an order with a price which is == or > askPrice()) you will match, and receive
 * a fill back into your order from the order book (visible from the algo in the childOrders of the state object.
 *
 * If you cancel the order your child order will show the order status as cancelled in the childOrders of the state object.
 *
 */
public class MyAlgoBackTest extends AbstractAlgoBackTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        return new MyAlgoLogic();
    }

    @Test
    public void testBackTest() throws Exception {
        //create a sample market data tick....
        send(createTick());

        //ADD asserts when you have implemented your algo logic
        //assertEquals(container.getState().getChildOrders().size(), 6);

        //when: market data moves towards us
        send(createTick2());
        send(createTick3());
       send(createTick4());

        //then: get the state
        var state = container.getState();


        //verify create SellOrders
        long createSellOrders =state.getChildOrders().stream().filter(childOrder -> childOrder.getSide()==Side.SELL).count();
        assertEquals("At least a SELL Order is created", 5, createSellOrders );

         //Verify create BuyOrders
        long createBuyOrders =state.getChildOrders().stream().filter(childOrder -> childOrder.getSide()==Side.BUY).count();
        assertEquals("At least a BUY Order is created", 5, createBuyOrders );

        //Verify at least a SellOrder was cancelled
        long cancelSellOrder = state.getCancelledChildOrders().stream().filter(childOrder -> childOrder.getSide()== Side.SELL).count();
        assertEquals("At least one sell order should be cancelled", 5,cancelSellOrder);

        //Verify at least a BuyOrder was cancelled
        long cancelBuyOrder = state.getCancelledChildOrders().stream().filter(childOrder -> childOrder.getSide()== Side.BUY).count();
        assertEquals("At least one sell order should be cancelled", 2,cancelBuyOrder);

        //retrieve initial state and check order count
        long initialOrderCount = state.getChildOrders().size();
        assertTrue("initial order count should be greater than 0", initialOrderCount >0);



        //Check  filled quantity
       long filledQuantity = state.getChildOrders().stream().mapToLong(ChildOrder::getFilledQuantity).reduce(0L,Long::sum);
       //and: check that our algo state was updated to reflect our fills when the market data
        assertEquals("check Algo state was updated",3100, filledQuantity);




    }

}

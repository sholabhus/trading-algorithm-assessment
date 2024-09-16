package codingblackfemales.gettingstarted;

import codingblackfemales.algo.AlgoLogic;
import codingblackfemales.sotw.SimpleAlgoState;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;


/**
 * This test is designed to check your algo behavior in isolation of the order book.
 *
 * You can tick in market data messages by creating new versions of createTick() (ex. createTick2, createTickMore etc..)
 *
 * You should then add behaviour to your algo to respond to that market data by creating or cancelling child orders.
 *
 * When you are comfortable you algo does what you expect, then you can move on to creating the MyAlgoBackTest.
 *
 */
public class MyAlgoTest extends AbstractAlgoTest {

    @Override
    public AlgoLogic createAlgoLogic() {
        //this adds your algo logic to the container classes
        return new MyAlgoLogic();
    }


    @Test
    public void testDispatchThroughSequencer() throws Exception {

        //create a sample market data tick....
        send(createTick());
        send(createTick2());
        //Retrieve the state from the container
        SimpleAlgoState state = container.getState();

        //Get the number of buy orders
        long buyOrderCount = state.getBidLevels();

        //simple assert to check we had 3 orders created

        //assertEquals("Should create 3 orders",3, state.getBidLevels());
        // assertEquals("Should create 2 BUY orders", 2,buyOrderCount);

        assertEquals("Should cancel 1 order", 1, state.getCancelledChildOrders().size());
        assertFalse("The cancelled Buy order should no longer exists in the order book",state.getActiveChildOrders().contains(buyOrderCount));


    }
}







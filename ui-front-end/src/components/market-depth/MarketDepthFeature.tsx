import { useMarketDepthData } from "./useMarketDepthData";
import { schemas } from "../../data/algo-schemas";
import {PriceCell} from  "./PriceCell";
import { Quantity } from "./Quantity";

/**
 * TODO
 */
export const MarketDepthFeature = () => {
  const data = useMarketDepthData(schemas.prices);

  // Check if data is available
  if (!data) {
    return <div>Loading...</div>;
  }

  return (
    <><h2>Trading</h2><div style={{ width: '100%', overflowX: 'auto', display: 'flex' }}>
      <table style={{ width: '50%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th colSpan={1}></th>
            <th colSpan={2} style={{ textAlign: "center" }}>Bid</th>
            <th colSpan={2} style={{ textAlign: "center" }}>Ask</th>
          </tr>
          <tr>
            <th></th>
            <th>Quantity</th>
            <th>Price</th>
            <th>Price</th>
            <th>Quantity</th>
          </tr>
        </thead>
        <tbody>
          {data.map((row, index) => (
            <tr key={index}>
              {/* Bid side */}
              <td style={{ fontSize: "12px", textAlign: "center",paddingRight: "20px" }}> {row.level}</td>
              <td style={{ backgroundColor: "#007bff", color: "white", textAlign: "center" }}>
                <Quantity quantity={row.bidQuantity} color="blue" />
              </td>
              <td style={{ fontSize: "12px", textAlign: "center" }}>
                <PriceCell price={row.bid} />
              </td>

              {/* Ask side */}
              <td style={{ fontSize: "12px", textAlign: "center" }}>
                <PriceCell price={row.offer} />
              </td>
              <td style={{ backgroundColor: "#dc3545", color: "white", textAlign: "center" }}>
                <Quantity quantity={row.offerQuantity} color="red" />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div></>
  );
};

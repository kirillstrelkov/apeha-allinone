package apeha.allinone.calc;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CalculatorTest {
    private String durab = "76/89";
    private String price = "25.48";
    private String percentSell = "9999";
    private String percentBuy = "7997";

    @Test
    public void differentCalculationMethods() {
        String price = "27.00";
        String durab = "100/100";
        String priceOfSell = "27.00";
        String priceOfBuy = "24.54";
        String buingPercent = "9090";
        String sellingPercent = "10000";
        String extra = "0.00";
        BigDecimal profit = Calculator.calculateProfit(price, durab,
                priceOfSell, priceOfBuy, extra, false);
        BigDecimal profit2 = Calculator.calculateProfit(price, durab,
                priceOfSell, priceOfBuy, extra, true);
        BigDecimal profitPercent = Calculator.calculateProfitUsingPercents(
                price, durab, sellingPercent, buingPercent, extra, false);
        BigDecimal profitPercent2 = Calculator.calculateProfitUsingPercents(
                price, durab, sellingPercent, buingPercent, extra, true);
        assertEquals(profit, profitPercent);
        assertEquals(profit2, profitPercent2);
    }

    @Test
    public void durabilityTest() {
        String price = "324.93";
        String durab = "100/100";
        assertNotNull(Calculator.calculateRepairCosts(durab, price));
        durab = durab.concat("sfdsf");
        assertNull(Calculator.calculateRepairCosts(durab, price));
        durab = "1/100";
        assertNotNull(Calculator.calculateRepairCosts(durab, price));
        durab = "99/100";
        assertNotNull(Calculator.calculateRepairCosts(durab, price));
        durab = "101/100";
        assertNull(Calculator.calculateRepairCosts(durab, price));
        durab = "0/100";
        assertNull(Calculator.calculateRepairCosts(durab, price));
        durab = "1sd/100";
        assertNull(Calculator.calculateRepairCosts(durab, price));
        durab = "0.9/100";
        assertNull(Calculator.calculateRepairCosts(durab, price));
    }

    @Test
    public void checkBuyPrecent() {
        assertEquals("Buy price is incorrect", "20.38", Calculator
                .calculateBuyPrice(price, percentBuy).toString());
    }

    @Test
    public void checkSellPrecent() {
        assertEquals("Sell price is incorrect", "25.48", Calculator
                .calculateSellPrice(price, percentSell).toString());
    }

    @Test
    public void checkRepairCosts() {
        String durab = "77/100";
        String price = "15.10";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 0.23,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "27";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 0.46,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "45";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 0.92,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "105";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 2.3,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "180";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 4.14,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "360";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 8.28,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "1040";
        durab = "77/200";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 63.96,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "1060";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 65.19,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "1080";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 66.42,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "1300";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 79.95,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
        price = "1500";
        assertEquals(
                String.format(
                        "Incorrect repair costs for '%s' with durab '%s'",
                        price, durab), 92.25,
                Calculator.calculateRepairCosts(durab, price).doubleValue(),
                0.001);
    }

    @Test
    public void profitInBlues() {
        String price = "27";
        String durab = "100/100";
        String priceOfBuy = "24.55";
        String extra = "0";
        boolean townIsSeized = false;
        assertEquals("Incorrect income", "24.30",
                Calculator.calculateIncome(price, townIsSeized).toString());
        assertEquals("Incorrect costs", "27.01",
                Calculator.calculateCosts(priceOfBuy, townIsSeized).toString());
        assertEquals("Incorrect repair costs", "0.00", Calculator
                .calculateRepairCosts(durab, priceOfBuy).toString());
        assertEquals("Incorrect profit in blues", "0.00", Calculator
                .calcProfitInBlues(extra, townIsSeized).toString());
        assertEquals(
                "Incorrect profit",
                "-2.71",
                Calculator.calculateProfit(price, durab, price, priceOfBuy,
                        extra, townIsSeized).toString());

    }

    @Test
    public void differentPricesInput() {
        String[] arrayPricesBuySell = {"0.81", "32", "150.54", "155.765",
                "155.763", "180.0"};
        String[] expected = {"0.81", "32.00", "150.54", "155.77", "155.76",
                "180.00"};
        for (int i = 0; i < arrayPricesBuySell.length; i++) {
            assertEquals("Incorrect nubmer", expected[i], Calculator
                    .stringToBigDecimal(arrayPricesBuySell[i]).toString());
        }
    }

    @Test
    public void incomeCityIsSeized() {
        assertEquals(153.00, Calculator.calculateIncome("180.00", true)
                .doubleValue(), 0.001);
        assertEquals(162.00, Calculator.calculateIncome("180.00", false)
                .doubleValue(), 0.001);
    }

    @Test
    public void profitCityIsSeized() {
        String price = "27.00";
        String durab = "100/100";
        String priceOfSell = "27.00";
        String priceOfBuy = "24.53";
        String extra = "0.00";
        assertEquals(
                -5.26,
                Calculator.calculateProfit(price, durab, priceOfSell,
                        priceOfBuy, extra, true).doubleValue(), 0.001);
        assertEquals(
                -2.68,
                Calculator.calculateProfit(price, durab, priceOfSell,
                        priceOfBuy, extra, false).doubleValue(), 0.001);
    }

    @Test
    public void profitWithIncorrectDurability() {
        String price = "27.00";
        String durab = "100./100";
        String priceOfSell = "27.00";
        String priceOfBuy = "24.53";
        String extra = "0.00";
        assertNull(Calculator.calculateProfit(price, durab, priceOfSell,
                priceOfBuy, extra, true));
        assertNull(Calculator.calculateProfit(price, durab, priceOfSell,
                priceOfBuy, extra, false));
    }
}

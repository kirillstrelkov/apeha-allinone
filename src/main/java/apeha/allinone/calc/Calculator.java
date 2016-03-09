package apeha.allinone.calc;

import apeha.allinone.common.Utils;

import java.math.BigDecimal;

public class Calculator {
    final static int roundHalfUp = BigDecimal.ROUND_HALF_UP;

    public static BigDecimal calculateRepairCosts(String dur, String price) {
        int current = Utils.getInteger("^\\d+/", dur);
        int max = Utils.getInteger("/\\d+$", dur);

        if (current != -1 && max != -1 && current <= max && current > 0) {
            BigDecimal initsToFix = new BigDecimal(max - current);
            BigDecimal pricePerUnit = new BigDecimal(price).divide(
                    new BigDecimal("10")).divide(new BigDecimal(max), 2,
                    BigDecimal.ROUND_DOWN);
            BigDecimal priceToRepair = pricePerUnit.multiply(initsToFix);
            priceToRepair = priceToRepair.setScale(2, roundHalfUp);

            return priceToRepair;
        } else
            return null;
    }

    public static BigDecimal calculateBuyPrice(String price, String percentBuy) {
        BigDecimal bdPrice = new BigDecimal(price);

        BigDecimal bdPercentBuy = new BigDecimal(percentBuy).divide(
                new BigDecimal("10000"), 5, roundHalfUp);
        BigDecimal buyingCostsWithoutTax = bdPrice.multiply(bdPercentBuy)
                .setScale(2, roundHalfUp);

        return buyingCostsWithoutTax;
    }

    public static BigDecimal calculateSellPrice(String price, String percentSell) {
        BigDecimal bdPrice = new BigDecimal(price);

        BigDecimal bdPercentSell = new BigDecimal(percentSell)
                .divide(new BigDecimal(10000));
        BigDecimal sellingCostsWithoutTax = bdPrice.multiply(bdPercentSell)
                .setScale(2, roundHalfUp);

        return sellingCostsWithoutTax;
    }

    public static BigDecimal intToBigDecimal(int val) {
        return new BigDecimal(val).divide(new BigDecimal(100)).setScale(2,
                roundHalfUp);
    }

    public static BigDecimal calcProfitInBlues(String extraInBlues,
                                               boolean townIsSeized) {
        return calculateIncome(extraInBlues, townIsSeized);
    }

    public static BigDecimal calculateCosts(String priceBuy,
                                            boolean townIsSeized) {
        BigDecimal percent;

        if (townIsSeized)
            percent = new BigDecimal("1.15");
        else
            percent = new BigDecimal("1.10");

        return new BigDecimal(priceBuy).multiply(percent).setScale(2,
                roundHalfUp);
    }

    public static BigDecimal calculateIncome(String priceSell,
                                             boolean townIsSeized) {
        BigDecimal percent;

        if (townIsSeized)
            percent = new BigDecimal("0.85");
        else
            percent = new BigDecimal("0.90");

        return new BigDecimal(priceSell).multiply(percent).setScale(2,
                roundHalfUp);
    }

    public static BigDecimal stringToBigDecimal(String string) {
        return new BigDecimal(string).setScale(2, roundHalfUp);
    }

    public static BigDecimal calculateProfitUsingPercents(String price,
                                                          String durab, String sellingPercent, String buyingPercent,
                                                          String extra, boolean townIsSeized) {
        String priceOfSell = formatBigDecimal(calculateSellPrice(price,
                sellingPercent));
        String priceOfBuy = formatBigDecimal(calculateBuyPrice(price,
                buyingPercent));

        return calculateProfit(price, durab, priceOfSell, priceOfBuy, extra,
                townIsSeized);
    }

    public static BigDecimal calculateProfit(String price, String durab,
                                             String priceOfSell, String priceOfBuy, String extra,
                                             boolean townIsSeized) {
        BigDecimal calculateIncome = calculateIncome(priceOfSell, townIsSeized);
        BigDecimal calculateCosts = calculateCosts(priceOfBuy, townIsSeized);
        BigDecimal calculateRepairCosts = calculateRepairCosts(durab, price);

        if (calculateRepairCosts == null)
            return null;

        BigDecimal calcProfitInBlues = calcProfitInBlues(extra, townIsSeized);
        BigDecimal profit = calculateIncome.subtract(calculateCosts)
                .subtract(calculateRepairCosts).add(calcProfitInBlues);

        return profit.setScale(2, roundHalfUp);
    }

    public static String formatBigDecimal(BigDecimal value) {
        return value.setScale(2, roundHalfUp).toString();
    }
}

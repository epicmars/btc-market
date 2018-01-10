package com.fafabtc.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class NumberUtils {

    private static final String FORMAT_BLOCKCHAIN_QUANTITY = "#.#########";
    private static final String FORMAT_CURRENCY_QUANTITY = "#.########";

    private static DecimalFormat currencyFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.CHINA);
    private static DecimalFormat blockchainQuantityFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.CHINA);
    private static DecimalFormat percentFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.CHINA);

    static {
        currencyFormat.applyPattern(FORMAT_CURRENCY_QUANTITY);
        blockchainQuantityFormat.applyPattern(FORMAT_BLOCKCHAIN_QUANTITY);
        percentFormat.setMinimumFractionDigits(2);
    }

    public static Double parseDouble(String str) {
        Double result = null;
        try {
            result = Double.valueOf(str);
        } catch (Exception e) {
            Timber.e(e);
        }
        return result;
    }

    public static boolean isPositive(Double dNum) {
        return dNum != null && dNum > 0;
    }

    public static String formatCurrency(double currency) {
        return currencyFormat.format(currency);
    }

    public static String formatBlockchainQuantity(double blockchain) {
        try {
            return blockchainQuantityFormat.format(blockchain);
        } catch (ClassCastException e) {
            return null;
        }
    }

    public static String formatPercent(double percentage) {
        return percentFormat.format(percentage);
    }
}

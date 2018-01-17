package com.fafabtc.common.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import timber.log.Timber;

/**
 * Created by jastrelax on 2018/1/10.
 */

public class NumberUtils {

    private static final String FORMAT_VOLUME = "0";
    private static final String FORMAT_BALANCE_QUNITITY = "0.00";
    private static final String FORMAT_BLOCKCHAIN_QUANTITY = "#.#########";
    private static final String FORMAT_CURRENCY_QUANTITY = "#.########";

    private static DecimalFormat volumeFormat = (DecimalFormat) NumberFormat.getIntegerInstance(Locale.CHINA);
    private static DecimalFormat balanceFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.CHINA);
    private static DecimalFormat priceFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.CHINA);
    private static DecimalFormat blockchainQuantityFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.CHINA);
    private static DecimalFormat percentFormat = (DecimalFormat) NumberFormat.getPercentInstance(Locale.CHINA);

    static {
        volumeFormat.applyPattern(FORMAT_VOLUME);
        balanceFormat.applyPattern(FORMAT_BALANCE_QUNITITY);
        priceFormat.applyPattern(FORMAT_CURRENCY_QUANTITY);
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

    public static String formatBalance(double balance) {
        return balanceFormat.format(balance);
    }

    public static String formatPrice(double currency) {
        return priceFormat.format(currency);
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

    public static String formatVolumeCN(double volume) {
        if (volume < 1.0) {
            return balanceFormat.format(volume);
        }
        if (volume < 1E4) {
            return volumeFormat.format(volume);
        }
        if (volume < 1E8) {
            return volumeFormat.format(volume / 1E4) + "万";
        }
        return (long)(volume / 1E8) + "亿" +
                volumeFormat.format(volume % 1E8 / 1E4) + "万";
    }
}

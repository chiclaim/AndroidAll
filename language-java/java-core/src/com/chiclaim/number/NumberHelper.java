package com.chiclaim.number;

import java.math.BigDecimal;

public class NumberHelper {

    public static void fee2yuan(int fen, int scale) {
        BigDecimal feeYuan = new BigDecimal(fen)
                .divide(new BigDecimal(100), scale, BigDecimal.ROUND_HALF_UP);
        System.out.println(feeYuan.toString());
    }

    public static void main(String[] args) {
        fee2yuan(10, 1);
    }
}

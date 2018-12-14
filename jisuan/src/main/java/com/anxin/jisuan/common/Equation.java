package com.anxin.jisuan.common;

import java.math.BigDecimal;

import static org.apache.commons.math3.special.Erf.erf;

/**
 * 公式类；输入参数，给出返回值
 *
 * @author: ly
 * @date: 2018/12/7 13:54
 */
public class Equation {

    public double method(double x, double cc, double b, double betac, double zc,
                         double sig, double xc, double bx, double betax, double y, double z) {
        double sr2 = Math.sqrt(2.0);

//       erf = error functon
        double xa = (x - xc + bx) / (sr2 * betax);
        double xb = (x - xc - bx) / (sr2 * betax);
        double ya = (y + b) / (sr2 * betac);
        double yb = (y - b) / (sr2 * betac);
//       exp = exponential function
        double za = (z - zc) / (sr2 * sig);
        double zb = (z + zc) / (sr2 * sig);

        double result = cc * (erf(xa) - erf(xb)) * (erf(ya) - erf(yb)) * (Math.exp(-za * za) + Math.exp(-zb * zb));
        return result;
    }

    public static void main(String[] args) {
        Equation equation = new Equation();
//        double result = equation.method(4.35E+01, 1.67E-03, 4.11E+01, 3.13E+01, 2.26E-02, 1.46E+00,
//                5.27E+01, 5.10E+01, 4.16E-01, 109, 0);
        double result = equation.method(4.35E+01, 1.67E-03, 4.11E+01, 3.13E+01, 2.26E-02, 1.46E+00,
                43.5, 41.9, 0.343, 18, 0);
        System.out.println("结果： " + result);
    }

    //z取0，y按0.1的步长取；最后按x和y绘制地图
    public BigDecimal method(BigDecimal x, BigDecimal cc, BigDecimal b, BigDecimal betac, BigDecimal zc,
                             BigDecimal sig, BigDecimal xc, BigDecimal bx, BigDecimal betax, BigDecimal y, BigDecimal z) {
        BigDecimal sr2 = new BigDecimal(Double.toString(Math.sqrt(2.0)));
//        erf = error functon
        //x - xc
        BigDecimal xxc = x.subtract(xc);
        //x - xc + bx
        BigDecimal xxcbx1 = xxc.add(bx);
        //x - xc - bx
        BigDecimal xxcbx2 = xxc.subtract(bx);
        //sr2 * betax
        BigDecimal sr2betax = sr2.multiply(betax);
        //xa = (x - xc + bx) / (sr2 * betax);
        BigDecimal xa = xxcbx1.divide(sr2betax, 5, BigDecimal.ROUND_HALF_EVEN);
        //xb = (x - xc - bx) / (sr2 * betax);
        BigDecimal xb = xxcbx2.divide(sr2betax, 5, BigDecimal.ROUND_HALF_EVEN);

        //y + b
        BigDecimal yb1 = y.add(b);
        //y - b
        BigDecimal yb2 = y.subtract(b);
        //sr2 * betac
        BigDecimal sr2betac = sr2.multiply(betac);
        //ya = (y + b) / (sr2 * betac);
        BigDecimal ya = yb1.divide(sr2betac, 5, BigDecimal.ROUND_HALF_EVEN);
        //yb = (y - b) / (sr2 * betac);
        BigDecimal yb = yb2.divide(sr2betac, 5, BigDecimal.ROUND_HALF_EVEN);
        BigDecimal yayb = ya.subtract(yb);

//        exp = exponential function
        //z - zc
        BigDecimal zzc1 = z.subtract(zc);
        //z + zc
        BigDecimal zzc2 = z.add(zc);
        //sr2 * sig
        BigDecimal sr2sig = sr2.multiply(sig);
        //za = (z - zc) / (sr2 * sig);
        BigDecimal za = zzc1.divide(sr2sig, 5, BigDecimal.ROUND_HALF_EVEN);
        //zb = (z + zc) / (sr2 * sig);
        BigDecimal zb = zzc2.divide(sr2sig, 5, BigDecimal.ROUND_HALF_EVEN);

        //(xa) - (xb)
        BigDecimal xaxb = xa.subtract(xb);
        //(ya) - (yb)
//        BigDecimal yayb = ya.subtract(yb);
        //(-za * za)
        BigDecimal zaza = za.negate().multiply(za);
        //(-zb * zb)
        BigDecimal zbzb = zb.negate().multiply(zb);
        //((-za * za) + (-zb * zb))
        BigDecimal zazb = zaza.add(zbzb);

        // result = cc * ((xa) - (xb)) * ((ya) - (yb)) * ((-za * za) + (-zb * zb));
        BigDecimal result = cc.multiply(xaxb).multiply(yayb).multiply(zazb);
        return result;
    }
//    加：add（BigDecima）
//    减：subtract（BigDecimal）
//    乘：multiply（BigDecimal）
//    除：divide（BigDecimal）
//    乘方：pow（int）
//    取绝对值：abs（）
//    取反：negate（）
//    对比：compareTo（BigDecimal）

}

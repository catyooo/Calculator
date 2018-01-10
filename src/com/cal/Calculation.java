package com.cal;

import java.text.DecimalFormat;
import java.util.*;

/**
 * 智能计算器
 * <p>
 * <li>作用: 为了方便参与多人活动后付款方的AA制金额计算繁琐
 * <p>
 * Created by IntelliJ IDEA.
 * User: zhangchao
 * Date: 2017/12/21
 * Time: 9:43
 */
public class Calculation {

    /**
     * 警告标识
     */
    private static final String ERROR = "ERROR: ";

    /**
     * 整数每三位逗号分隔并保留两位小数
     */
    private static final DecimalFormat DF = new DecimalFormat("#,###.00");

    /**
     * Console扫描仪
     */
    private static final Scanner SCANNER = new Scanner(System.in);

    /**
     * 智能计算器昵称
     */
    private static final String NICKNAME = "SmartQBer\uD83D\uDE0A";     // SmartQBer😊

    /**
     * 主方法进入 Run/Debug
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        System.out.println("智能计算器" + NICKNAME + " ! 已为您成功启动 >>>");
        try {
            // 开始
            start();
        } catch (Exception e) {
            System.out.printf("%s检测到异常, %s已开启自我保护, 停止运行 <<<%n", ERROR, NICKNAME);
        }
    }

    /**
     * 开始
     */
    private static void start() {
        /*
         * 是否知道总金额
         */
        System.out.println("请输入是否知道总金额 ? >>>"
                + " ( Y: 知道, 直接开始计算。 "
                + "||  N: 不知道, 先输入每笔金额 !  "
                + "||  E: 多人多金额不同的付款 ! )");

        switch (SCANNER.next().toUpperCase()) {     // 获取输入类型
            // 按总金额计算
            case "Y":
                System.out.println("请输入总金额:");
                double totMoney = SCANNER.nextDouble();
                // 开始计算
                startCal(totMoney);
                break;
            // 按每笔相加计算出总金额
            case "N":
                isNotTotMoney();
                break;
            // 多人多金额不同的付款
            case "E":
                peoplesDiffMoney();
                break;
            default:
                System.out.println(ERROR + "请按提示, 重新输入 !");
                start();
                break;
        }
    }

    /**
     * 不知道总金额 (按每笔相加计算出总金额)
     */
    private static void isNotTotMoney() {
        System.out.println("请输入总笔数:");
        int tNum = SCANNER.nextInt();   // 获取输入笔数
        // 非0判断
        if (tNum != 0) {
            double[] eNumMon = new double[tNum];
            // 循环输入每笔金额
            for (int i = 0; i < eNumMon.length; i++) {
                System.out.println(new StringBuilder("输入第 (").append(i + 1).append(") 笔的金额 ( 共").append(tNum)
                        .append("笔 ) :"));
                double eMoney = SCANNER.nextDouble();
                eNumMon[i] = eMoney;
            }

            // 按笔数计算总金额
            double totMoney = 0.0;
            for (double eNM : eNumMon) totMoney += eNM;
            System.out.printf("--- 总金额 ( %s ) 元 ---%n", DF.format(totMoney));

            // 开始计算
            startCal(totMoney);
        } else {
            System.out.println(ERROR + "总笔数不可为0 !");
            isNotTotMoney();
        }
    }

    /**
     * 开始计算
     */
    private static void startCal(double totMoney) {
        // 非0判断
        if (totMoney != 0.0) {
            // 按人数计算
            calForPeopleNum(totMoney);
        } else {
            System.out.println(ERROR + "总金额不能为0 , 请重新输入 !");
            startCal(totMoney);
        }
    }

    /**
     * 按人数计算
     *
     * @param totMoney 总金额
     */
    private static void calForPeopleNum(double totMoney) {
        System.out.println("输入参与人数:");
        int peopleNum = SCANNER.nextInt();
        // 非0判断
        if (peopleNum != 0) {
            // 计算方式校验
            calTypeCheck(peopleNum, totMoney);
        } else {
            System.out.println(ERROR + "参与人数不可为0 , 请重新输入 !");
            calForPeopleNum(totMoney);
        }
    }

    /**
     * 计算方式校验
     *
     * @param peopleNum 人数
     * @param totMoney  总金额
     */
    private static void calTypeCheck(int peopleNum, double totMoney) {
        System.out.println("请输入参与人中是否有个人金额 ? >>> ( Y: 有。 ||  N: 没有, 平摊计算 ! )");

        switch (SCANNER.next().toUpperCase()) {     // 获取输入类型
            // 按有个人参与金额计算
            case "Y":
                isPerMonCal(peopleNum, totMoney);
                break;
            // 按没有个人参与金额计算
            case "N":
                double ePay = totMoney / peopleNum;
                System.out.printf("共%d人, 每人应付款: %s 元%n", peopleNum, DF.format(ePay));
                System.out.println(NICKNAME + "已为您完成计算 <<<");
                break;
            default:
                System.out.println(ERROR + "请按提示, 重新输入 !");
                calTypeCheck(peopleNum, totMoney);
                break;
        }
    }

    /**
     * 按有个人参与金额计算
     *
     * @param peopleNum 人数
     * @param totMoney  总金额
     */
    private static void isPerMonCal(int peopleNum, double totMoney) {

        // 获取每人输入金额
        double[] eMoneys = getEveryMoney(peopleNum);
        // 计算每位总金额
        double sum = 0.0;
        for (double eM : eMoneys) sum += eM;

        // 越值校验
        if (sum > totMoney) {
            System.out.printf("%s每位的相加金额 (%s) 不可大于总金额 (%s) ! 请重新开始 !%n", ERROR,
                    DF.format(sum), DF.format(totMoney));
            start();
            return;
        }

        /*
         * 计算公共金额
         */
        for (int i = 0; i < eMoneys.length; i++) {
            double comMoney = totMoney - sum;   // 公共金额
            double ePay = (comMoney / peopleNum) + eMoneys[i];   // 每位应付金额
            System.out.println(new StringBuilder("第 (").append(i + 1).append(") 位应付款: ").append(DF.format(ePay))
                    .append(" 元"));
        }
        System.out.println(NICKNAME + "已为您完成一波复杂的计算 <<<");
    }

    /**
     * 获取每人输入金额
     *
     * @param peopleNum 人数
     * @return double[]
     */
    private static double[] getEveryMoney(int peopleNum) {

        double[] eMoneys = new double[peopleNum];
        // 循环输入每位金额
        for (int i = 0; i < eMoneys.length; i++) {
            System.out.println(new StringBuilder("输入第 (").append(i + 1).append(") 位 ( 共").append(peopleNum)
                    .append("位 ) 的金额:"));
            double eMoney = SCANNER.nextDouble();
            eMoneys[i] = eMoney;
        }
        return eMoneys;
    }

    /**
     * 按多人多金额不同的付款计算
     */
    private static void peoplesDiffMoney() {
        System.out.println("人数:");
        int peopleNum = SCANNER.nextInt();

        // 第几位付款次数
        List<Map<Integer, Double>> mapList = new LinkedList<>();
        for (int i = 0; i < peopleNum; i++) {
            System.out.printf("第%d位付款次数 ( 共%d位 ):%n", i + 1, peopleNum);
            int payNum = SCANNER.nextInt();

            // 第几位第几次付款
            Map<Integer, Double> map = new HashMap<>();
            for (Integer j = 0; j < payNum; j++) {
                System.out.printf("第%d位, 第%d次付款的金额 ( 共%d次 ):%n", i + 1, j + 1, payNum);
                double money = SCANNER.nextDouble();
                map.put(j + 1, money);
            }
            mapList.add(i, map);
        }

        // 计算每位的金额总数
        Map<Integer, Double> doubleMap = new HashMap<>();
        for (Integer a = 0; a < mapList.size(); a++) {
            double mmp = 0.0;
            for (int k = 0; k < mapList.get(a).size(); k++) {
                mmp += mapList.get(a).get(k + 1);
            }
            doubleMap.put(a, mmp);
        }

        // 判断是否金额都相等
        if (doubleMap.size() > 1) {
            boolean flag = true;    // 相等
            for (Integer c = 0; c < doubleMap.size(); c++) {
                Integer d = c + 1;
                if (!doubleMap.get(c).equals(doubleMap.get(d))) {
                    flag = false;   // 不相等
                    break;
                }
                if (c + 1 + 1 == doubleMap.size()) {
                    break;
                }
            }

            // 不相等计算
            if (!flag) {
                // TODO

                // 相等计算
            } else {
                double tMon = 0.0;
                for (Integer f = 0; f < doubleMap.size(); f++) tMon += doubleMap.get(f);
                double m = tMon / peopleNum;    //计算总金额
                System.out.printf("共%s元, %d人, 每人付款%s元%n", DF.format(tMon), peopleNum, DF.format(m));
            }
        }
    }

}



package com.xiaxl.retrofit2_2;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xiaxueliang on 2017/4/21.
 */

public class Test {

    public static void main(String args[]) {
        // 创建一个捕鱼人
        final SellFisher sellFisher = new FisherMan();
        // 创建一个动态代理对象
        Object obj = Proxy.newProxyInstance(sellFisher.getClass().getClassLoader(), sellFisher.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 打印“InvocationHandler——> public abstract int com.xiaxl.retrofit2_2.Test$SellFisher.sellFish(int)”
                System.out.println("InvocationHandler——> " + method.toString());
                // 打印“InvocationHandler——> 1”
                if (args != null && args.length > 0) {
                    for (int i = 0; i < args.length; i++) {
                        System.out.println("InvocationHandler——> " + args[i]);
                    }
                }
                // 打印“InvocationHandler——> 执行捕鱼人的sellFish拿到鱼”
                System.out.println("InvocationHandler——> 执行捕鱼人的sellFish拿到鱼");
                // 打印"FisherMan——> sellFish"
                // 打印"FisherMan——> 鲨鱼:50"
                int concreteSellFisher = (int) method.invoke(sellFisher, args);// 相当于sell.sellFish(args);
                // 打印"InvocationHandler——> 代理加价sellFish"
                System.out.println("InvocationHandler——> 代理加价sellFish");
                return concreteSellFisher + 10;
            }
        });
        // 执行动态代理对象的sellFish方法
        ((SellFisher) obj).sellFish(1);
    }


    public static interface SellFisher {
        int sellFish(int type);
    }

    /**
     * 捕鱼人
     */
    public static class FisherMan implements SellFisher {

        public int sellFish(int type) {
            System.out.println("FisherMan——> sellFish");
            switch (type) {
                case 1:
                    System.out.println("FisherMan——> 鲨鱼:50");
                    return 50;
                default:
                    System.out.println("FisherMan——> 其他:10");
                    return 10;
            }
        }
    }
}
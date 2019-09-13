package com.conanco.practice.leetcode.Concurrent;

/* =====================================================================================
 * @ProjectName:    practice
 * @Package:        com.conanco.practice.leetcode.Concurrent
 * @ClassName:      ${TYPE_NAME}
 * @Description:
 *  现在有两种线程，氢 oxygen 和氧 hydrogen，你的目标是组织这两种线程来产生水分子。
    存在一个屏障（barrier）使得每个线程必须等候直到一个完整水分子能够被产生出来。
    氢和氧线程会被分别给予 releaseHydrogen 和 releaseOxygen 方法来允许它们突破屏障。
    这些线程应该三三成组突破屏障并能立即组合产生一个水分子。
    你必须保证产生一个水分子所需线程的结合必须发生在下一个水分子产生之前。
    换句话说:
    如果一个氧线程到达屏障时没有氢线程到达，它必须等候直到两个氢线程到达。
    如果一个氢线程到达屏障时没有其它线程到达，它必须等候直到一个氧线程和另一个氢线程到达。
    书写满足这些限制条件的氢、氧线程同步代码。
    示例 1:
    输入: "HOH"
    输出: "HHO"
    解释: "HOH" 和 "OHH" 依然都是有效解。
    示例 2:
    输入: "OOHHHH"
    输出: "HHOHHO"
    解释: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" 和 "OHHOHH" 依然都是有效解。
    限制条件:
    输入字符串的总长将会是 3n, 1 ≤ n ≤ 50；
    输入字符串中的 “H” 总数将会是 2n；
    输入字符串中的 “O” 总数将会是 n。
    在真实的面试中遇到过这道题？
    来源：力扣（LeetCode）
    链接：https://leetcode-cn.com/problems/building-h2o
    著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @Author:         conan.ckr (chenkeren), keren.chen@dianping.com
 * @Organization:   dianping
 * @CreateDate:     2019/9/13 16:28
 * @UpdateUser:     conan.ckr
 * @UpdateDate:     2019/9/13 16:28
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2019</p>
 * =====================================================================================
 */

import java.util.concurrent.CountDownLatch;

public class H2O {

    private volatile BatchCountDownLatch countDownLatch = new BatchCountDownLatch();

    public H2O() {
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        while (true) {
            BatchCountDownLatch batchCountDownLatch = countDownLatch;
            if (batchCountDownLatch.tryProduceHydrogen()) {
                // releaseHydrogen.run() outputs "H". Do not change or remove this line.
                releaseHydrogen.run();
                batchCountDownLatch.awaitAll();
                break;
            }
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        while (true) {
            BatchCountDownLatch batchCountDownLatch = countDownLatch;
            if (batchCountDownLatch.tryProduceOxygen()) {
                // releaseOxygen.run() outputs "O". Do not change or remove this line.
                releaseOxygen.run();
                batchCountDownLatch.awaitAll();
                break;
            }
        }
        countDownLatch = new BatchCountDownLatch();
    }

    class BatchCountDownLatch {

        private CountDownLatch hydrogenCountDownLatch = new CountDownLatch(2);

        private CountDownLatch oxygenCountDownLatch = new CountDownLatch(1);

        synchronized public boolean tryProduceHydrogen() {
            if (hydrogenCountDownLatch.getCount() == 0) {
                return false;
            } else {
                hydrogenCountDownLatch.countDown();
                return true;
            }
        }

        synchronized public boolean tryProduceOxygen() {
            if (oxygenCountDownLatch.getCount() == 0) {
                return false;
            } else {
                oxygenCountDownLatch.countDown();
                return true;
            }
        }

        public void awaitAll() throws InterruptedException {
            hydrogenCountDownLatch.await();
            oxygenCountDownLatch.await();
        }
    }
}

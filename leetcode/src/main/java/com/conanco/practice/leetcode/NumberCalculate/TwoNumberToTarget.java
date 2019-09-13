package com.conanco.practice.leetcode.NumberCalculate;

import java.util.HashMap;
import java.util.Map;

/* =====================================================================================
 * @ProjectName:    practice
 * @Package:        com.conanco.practice.leetcode.NumberCalculate
 * @ClassName:      ${TYPE_NAME}
 * @Description:    ${DESC}
 * @Author:         conan.ckr (chenkeren), keren.chen@dianping.com
 * @Organization:   dianping
 * @CreateDate:     2019/9/13 22:26
 * @UpdateUser:     conan.ckr
 * @UpdateDate:     2019/9/13 22:26
 * @UpdateRemark:   The modified content
 * @Version:        1.0
 * <p>Copyright: Copyright (c) 2019</p>
 * =====================================================================================
 */
public class TwoNumberToTarget {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numberMap = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            int j = target - nums[i];
            Integer index = numberMap.get(j);
            if (index != null) {
                return new int[]{index, i};
            } else {
                numberMap.put(nums[i], i);
            }
        }

        return null;
    }
}

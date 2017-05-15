package com.bidanet.bdcms.common;

import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public interface TreeNode<K,T> {
    K getTreeId();
    K getTreeParentId();
    void setChildren(List<T> list);
    List<T> getChildren();
    boolean isRoot();
}

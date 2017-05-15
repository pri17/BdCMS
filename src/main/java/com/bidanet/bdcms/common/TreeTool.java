package com.bidanet.bdcms.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/7/14.
 */
public class TreeTool {

    public static<K,T> List<TreeNode> buildTree(List<? extends TreeNode<K,T>> list){
        ArrayList<TreeNode> tree = new ArrayList<>();
        HashMap<Object, TreeNode> map = new HashMap<>();

        if(list!=null) {
            for (TreeNode menu:list) {
                menu.setChildren(new ArrayList<>());
                map.put(menu.getTreeId(),menu);
            }
            for (TreeNode m : list) {
                if (m.isRoot()) {
                    tree.add(m);
                } else {
                    TreeNode menu = map.get(m.getTreeParentId());
                    if (menu != null) {
                        menu.getChildren().add(m);
                    } else {
//                    tree.add(m);
                    }

                }
            }
        }
        return tree;
    }
}

package com.tomtaw.lib_badge;

import android.text.TextUtils;
import android.util.SparseArray;

import com.tomtaw.lib_badge.BadgeNumber.BadgeMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BadgeHelper {
    private static BadgeHelper instance;

    private BadgeNumberNode root;
    private SparseArray<BadgeNumberNode> badgeArray;
    private SparseArray<Map<String, OnChangeListener>> listeners;

    private Collection<IBadgeGraph.Badge> badgeGraph;

    private BadgeHelper() {
        root = new BadgeNumberNode();
        root.setSelfId(IBadgeGraph.root);
        BadgeNumber rootNumber = new BadgeNumber();
        rootNumber.setCount(0);
        rootNumber.setBadgeId(IBadgeGraph.root);
        rootNumber.setDisplayMode(BadgeMode.NUMBER);
        root.setObj(rootNumber);
        badgeArray = new SparseArray<>();
        badgeArray.put(root.getSelfId(), root);
        listeners = new SparseArray<>();
    }

    public static BadgeHelper I() {
        if (instance == null) {
            instance = new BadgeHelper();
        }
        return instance;
    }

    public void setBadgeGraph(IBadgeGraph graph) {
        if (this.badgeGraph != null) {
            clear(IBadgeGraph.root, true);
        }
        this.badgeGraph = graph.badges();
    }

    public void addBadgeGraph(IBadgeGraph graph) {
        if (badgeGraph == null) {
            badgeGraph = new ArrayList<>();
        }
        badgeGraph.addAll(graph.badges());
    }

    public int plus(int id, int num, int mode) {
        BadgeNumberNode node = badgeArray.get(id);
        //如果之前没有这个node，则新增
        if (node == null) {
            insertBadge(id, num, mode);
            return num;
        }

        BadgeNumber number = node.getObj();
        int oldCount = number.getCount();
        int newCount = oldCount + num;

        //计数为0
        if (newCount == 0) {
            delBadge(node);
        } else {
            updateBadge(id, newCount, mode);
        }
        return number.getCount();
    }

    public int plus(int id, int mode) {
        return plus(id, 1, mode);
    }

    public int reduce(int id, int num, int mode) {
        return plus(id, -num, mode);
    }

    public int reduce(int id, int mode) {
        return plus(id, -1, mode);
    }

    public void clear(int id) {
        clear(id, true);
    }

    public void clear(int id, boolean notifyChange) {
        BadgeNumberNode node = badgeArray.get(id);
        if (node != null)
            delBadge(node, notifyChange);
    }

    /**
     * 设置某个节点
     */
    public void set(int id, int num, int mode) {
        BadgeNumber badgeNumber = getBadge(id);
        int diffNum = num;
        if (badgeNumber != null) {
            int oldNum = badgeNumber.getCount();
            int oldMode = badgeNumber.getDisplayMode();
            if (oldNum == num && oldMode == mode) return;
            diffNum = num - oldNum;
        }
//    clear(id, false);
        plus(id, diffNum, mode);
    }

    private BadgeNumber getBadge(int id) {
        BadgeNumberNode node = badgeArray.get(id);
        if (node != null && node.getObj() != null) {
            return node.getObj();
        } else {
            return null;
        }
    }

    private void insertBadge(int id, int count, int mode) {
        BadgeNumberNode node = badgeArray.get(id);
        if (node != null) {
            updateBadge(id, count, mode);
            throw new RuntimeException("cannot insert already exist node!");
        } else {
            BadgeNumberNode insertNode = generateNode(id, count, mode);
            badgeArray.put(id, insertNode);
            notifyChange(insertNode.getObj());

            int parentId = getParentId(id);
            BadgeNumberNode parentNode = badgeArray.get(parentId);
            BadgeNumberNode childNode = insertNode;

            //父节点已存在，重新计算下mode和count
            if (parentNode != null) {
                childNode.setParentId(parentId);
                childNode.setParentNode(parentNode);
                parentNode.addChildNode(childNode);
                reCalculate(parentNode, true);
                return;
            }
            //父节点不存在，需要创建并添加
            while (parentNode == null) {
                parentNode = generateNode(parentId, count, mode);
                childNode.setParentId(parentId);
                childNode.setParentNode(parentNode);
                parentNode.addChildNode(childNode);
                badgeArray.put(parentId, parentNode);
                notifyChange(parentNode.getObj());

                childNode = parentNode;
                parentId = getParentId(parentId);
                parentNode = badgeArray.get(parentId);

                //最后到达root节点的时候，需要再绑定一次关系
                if (parentNode != null) {
                    childNode.setParentId(parentId);
                    childNode.setParentNode(parentNode);
                    parentNode.addChildNode(childNode);
                    reCalculate(parentNode, true);
                }
            }
        }
    }

    private BadgeNumberNode generateNode(int id, int count, int mode) {
        BadgeNumberNode node = new BadgeNumberNode();
        BadgeNumber number = new BadgeNumber(id);
        number.setCount(count);
        number.setDisplayMode(mode);
        number.setBadgeId(id);
        node.setObj(number);
        node.setSelfId(id);
        return node;
    }

    private void updateBadge(int id, int newCount, int newMode) {
        BadgeNumberNode node = badgeArray.get(id);
        if (node == null) {
            return;
        }
        if (node.getObj().getCount() == newCount && node.getObj().getDisplayMode() == newMode) {
            return;
        }
        if (newCount <= 0) {
            delBadge(node);
            return;
        }
        BadgeNumber childNum = node.getObj();
        childNum.setCount(newCount);
        childNum.setDisplayMode(newMode);
        notifyChange(childNum);

        TreeNode<BadgeNumber> parentNode = node.getParentNode();
        while (parentNode != null) {
            BadgeNumber parentNum = parentNode.getObj();
            int mode = calMode(parentNode);
            int count = calCount(parentNode, mode);
            parentNum.setCount(count);
            parentNum.setDisplayMode(mode);
            parentNode = parentNode.getParentNode();
            notifyChange(parentNum);

            if(parentNode == null || parentNode.getSelfId() == IBadgeGraph.invalid){
                break;
            }
        }
    }

    private void reCalculate(TreeNode<BadgeNumber> node, boolean notifyChange) {
        BadgeNumber number = node.getObj();
        int oldCount = number.getCount();
        int oldMode = number.getDisplayMode();
        int newMode = calMode(node);
        int newCount = calCount(node, newMode);
        if (oldCount == newCount && oldMode == newMode) {
            return;
        }
        number.setCount(newCount);
        number.setDisplayMode(newMode);
        if (notifyChange) notifyChange(number);

        TreeNode<BadgeNumber> parentNode = node.getParentNode();
        while (parentNode != null) {
            BadgeNumber parentNum = parentNode.getObj();
            int mode = calMode(parentNode);
            int count = calCount(parentNode, mode);
            parentNum.setCount(count);
            parentNum.setDisplayMode(mode);
            parentNode = parentNode.getParentNode();
            notifyChange(parentNum);
            if(parentNode == null || parentNode.getSelfId() == IBadgeGraph.invalid) {
                //无效id
                break;
            }
        }
    }

    private void delBadge(BadgeNumberNode node) {
        delBadge(node, true);
    }

    private void delBadge(BadgeNumberNode node, boolean notifyChange) {
        if (node == null) {
            return;
        }
        TreeNode<BadgeNumber> singleLineTop = findLineTopParent(node);
        delChild(singleLineTop, notifyChange);

        TreeNode<BadgeNumber> parentNode = singleLineTop.getParentNode();
        if (parentNode != null) {
            parentNode.deleteChildNode(singleLineTop.getSelfId());
            badgeArray.remove(singleLineTop.getSelfId());
            if (notifyChange)
                notifyDisplay(singleLineTop.getObj(), false);
        }
        while (parentNode != null) {
            BadgeNumber parentNum = parentNode.getObj();
            int mode = calMode(parentNode);
            int count = calCount(parentNode, mode);
            parentNum.setCount(count);
            parentNum.setDisplayMode(mode);
            parentNode = parentNode.getParentNode();
            if (notifyChange)
                notifyChange(parentNum);
            if(parentNode == null || parentNode.getSelfId() == IBadgeGraph.invalid){
                break;
            }
        }
    }

    private void delChild(TreeNode<BadgeNumber> node, boolean notifyChange) {
        List<TreeNode<BadgeNumber>> children = node.getChildList();
        if (children == null || children.size() <= 0) {
            return;
        }

        while (!children.isEmpty()) {
            TreeNode<BadgeNumber> n = children.get(0);
            children.remove(0);
            badgeArray.remove(n.getSelfId());
            if (notifyChange)
                notifyDisplay(n.getObj(), false);
            if (n.getChildList() != null)
                children.addAll(n.getChildList());
        }
        node.setChildList(null);
    }

    private TreeNode<BadgeNumber> findLineTopParent(TreeNode<BadgeNumber> node) {
        TreeNode<BadgeNumber> parent = node.getParentNode();
        TreeNode<BadgeNumber> result = node;
        while (parent != null && parent.getChildList() != null && parent.getChildList().size() == 1) {
            if (parent.getSelfId() == IBadgeGraph.root) {
                return parent;
            }
            result = parent;
            parent = parent.getParentNode();
        }
        return result;
    }

    /**
     * 计算node的count
     */
    private int calCount(TreeNode<BadgeNumber> node, @BadgeMode int mode) {
        List<TreeNode<BadgeNumber>> children = node.getChildList();
        BadgeNumber number = node.getObj();
        int count = 0;
        if (children != null && children.size() > 0) {
            for (TreeNode<BadgeNumber> child : children) {
                if (mode == child.getObj().getDisplayMode()) {
                    count += child.getObj().getCount();
                }
            }
        }
        return count;
    }

    private
    @BadgeMode
    int calMode(TreeNode<BadgeNumber> node) {
        List<TreeNode<BadgeNumber>> children = node.getChildList();
        if (children != null && children.size() > 0) {
            for (TreeNode<BadgeNumber> child : children) {
                if (BadgeMode.NUMBER == child.getObj().getDisplayMode()) {
                    return BadgeMode.NUMBER;
                }
            }
        }
        return BadgeMode.DOT;
    }

    private int getParentId(int id) {
        if(isEffective(badgeGraph)){
            for(IBadgeGraph.Badge badge: badgeGraph){
                if(badge.self() == id) {
                    return badge.parent();
                }
            }
            return IBadgeGraph.invalid;
        }else{
            return IBadgeGraph.invalid;
        }
    }

    private void notifyChange(BadgeNumber badge) {
        Map<String, OnChangeListener> listenerMap = listeners.get(badge.getBadgeId());
        if (!isEffectiveMap(listenerMap)) return;
        Collection<OnChangeListener> values = listenerMap.values();
        if (isEffective(values)) {
            for (OnChangeListener listener : values) {
                if (listener == null) continue;
                listener.onChange(badge);
            }
        }
    }

    private void notifyDisplay(BadgeNumber badge, boolean show) {
        Map<String, OnChangeListener> listenerMap = listeners.get(badge.getBadgeId());
        if (!isEffectiveMap(listenerMap)) return;
        Collection<OnChangeListener> values = listenerMap.values();
        if (isEffective(values)) {
            for (OnChangeListener listener : values) {
                if (listener == null) continue;
                listener.onDisplay(badge, show);
            }
        }
    }

    public void addListener(int id, OnChangeListener listener) {
        addListener(id, listener, String.valueOf(id));
    }

    public void addListener(int id, OnChangeListener listener, String tag) {
        BadgeNumber number = getBadge(id);
        if (number == null) {
            number = new BadgeNumber();
            number.setBadgeId(id);
            number.setCount(0);
            number.setDisplayMode(BadgeMode.DOT);
        }
        listener.onInit(number);
        Map<String, OnChangeListener> listenerMap = listeners.get(id);
        if (listenerMap == null) {
            listenerMap = new HashMap<>();
            listeners.put(id, listenerMap);
        }
        listenerMap.put(tag, listener);
    }

    public void removeListener(int id) {
        removeListener(id, null);
    }

    public void removeListener(int id, String tag) {
        if (TextUtils.isEmpty(tag)) {
            listeners.remove(id);
        } else {
            Map<String, OnChangeListener> listenerMap = listeners.get(id);
            if (listenerMap != null) {
                listenerMap.remove(tag);
            }
        }
    }

    public interface OnChangeListener {
        void onInit(BadgeNumber badge);

        void onChange(BadgeNumber badge);

        void onDisplay(BadgeNumber badge, boolean show);
    }

    private static boolean isEffective(Collection<?> list) {
        return list != null && list.size() > 0;
    }

    private static boolean isEffectiveMap(Map<?, ?> map) {
        return map != null && map.size() > 0;
    }

}
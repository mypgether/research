package com.gether.research.test.stream.kafka.case4.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by myp won 2017/9/1.
 */
public class TopCategory {
    private String category;
    private List<ItemInfo> itemInfoList;

    public TopCategory() {
    }

    public TopCategory(String category, List<ItemInfo> itemInfoList) {
        this.category = category;
        this.itemInfoList = itemInfoList;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ItemInfo> getItemInfoList() {
        return itemInfoList;
    }

    public void setItemInfoList(List<ItemInfo> itemInfoList) {
        this.itemInfoList = itemInfoList;
    }

    /**
     * 根据金额汇总倒序
     *
     * @param allItems
     * @return
     */
    private List<ItemInfo> sortBySumDesc(Collection<ItemInfo> allItems) {
        List<ItemInfo> result = new ArrayList<ItemInfo>();
        result.addAll(allItems);
        Collections.sort(result, new Comparator<ItemInfo>() {
            @Override
            public int compare(ItemInfo o1, ItemInfo o2) {
                if (o1.getTotalPrice() == o2.getTotalPrice()) {
                    return 0;
                } else if (o1.getTotalPrice() > o2.getTotalPrice()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });
        return result;
    }

    public String printTop10(long startDate, long endDate) {
        double allAmount = 0.0;
        Map<String, ItemInfo> groupMap = new HashMap<String, ItemInfo>();
        for (ItemInfo item : itemInfoList) {
            String key = item.getItemName();
            allAmount += item.getQuantity();
            if (groupMap.containsKey(key)) {
                ItemInfo oldItem = groupMap.get(key);
                oldItem.setQuantity(oldItem.getQuantity() + item.getQuantity());
                oldItem.setTotalPrice(oldItem.getTotalPrice() + item.getTotalPrice());
            } else {
                groupMap.put(key, item);
            }
        }
        List<ItemInfo> sortedResult = sortBySumDesc(groupMap.values());
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss");
        for (int i = 1; i <= 10; i++) {
            if (sortedResult.size() >= i && sortedResult.get(i - 1) != null) {
                ItemInfo oneItem = sortedResult.get(i - 1);
                sb.append(simpleDateFormat.format(new Date(startDate))).append(",").append(simpleDateFormat.format(new Date(endDate))).append(",").append(oneItem.getCategory()).append(",").append(oneItem.getItemName()).append(",")
                        .append(oneItem.getQuantity()).append(",").append(oneItem.getItemPrice()).append(",").append(oneItem.getTotalPrice()).append(",").append(allAmount)
                        .append(",").append(i).append("\n");
            } else {
                break;
            }
        }
        return sb.toString();
    }
}
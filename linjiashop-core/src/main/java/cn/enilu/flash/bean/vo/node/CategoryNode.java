package cn.enilu.flash.bean.vo.node;

import cn.enilu.flash.bean.entity.shop.Category;
import cn.enilu.flash.bean.entity.shop.Goods;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryNode extends Category {
    private List<CategoryNode> children= null;
    private List<Goods> goodsList = new ArrayList<Goods>();
    public String getLabel(){
        return getName();
    }

}

package cn.enilu.flash.service.shop;


import cn.enilu.flash.bean.entity.shop.Category;
import cn.enilu.flash.bean.entity.shop.Goods;
import cn.enilu.flash.bean.vo.node.CategoryNode;
import cn.enilu.flash.bean.vo.query.SearchFilter;
import cn.enilu.flash.dao.shop.CategoryRepository;
import cn.enilu.flash.dao.shop.GoodsRepository;
import cn.enilu.flash.service.BaseService;
import cn.enilu.flash.utils.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends BaseService<Category, Long, CategoryRepository> {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private GoodsRepository goodsRepository;

    public List<CategoryNode> getCategoriesAndGoods(List<SearchFilter> filters) {
        List<Category> list = queryAll(Sort.by(Sort.Direction.ASC, "sort"));

        List<Goods> goodsList = goodsRepository.findAll();
        if (filters != null) {
            list = queryAll(filters, Sort.by(Sort.Direction.ASC, "sort"));
        } else {
            list = queryAll(Sort.by(Sort.Direction.ASC, "sort"));
        }

        List<CategoryNode> nodes = Lists.newArrayList();
        for (Category category : list) {
            if (category.getPid() == null) {
                CategoryNode node = new CategoryNode();
                BeanUtils.copyProperties(category, node);
                nodes.add(node);
            }

        }
        for (CategoryNode node : nodes) {
            for (Goods goods : goodsList) {
                if (goods.getIdCategory() != null && goods.getIdCategory().intValue() == node.getId().intValue() && goods.getIsOnSale()) {
                    node.getGoodsList().add(goods);
                }
            }
        }
        return nodes;
    }

    public List<CategoryNode> getCategories(List<SearchFilter> filters) {
        List<Category> list = queryAll(Sort.by(Sort.Direction.ASC, "sort"));
        if (filters != null) {
            list = queryAll(filters, Sort.by(Sort.Direction.ASC, "sort"));
        } else {
            list = queryAll(Sort.by(Sort.Direction.ASC, "sort"));
        }

        List<CategoryNode> Categorys = Lists.newArrayList();
        for (Category category : list) {
            if (category.getPid() == null) {
                CategoryNode node = new CategoryNode();
                BeanUtils.copyProperties(category, node);
                Categorys.add(node);
            }

        }
        for (CategoryNode node : Categorys) {
            for (Category category : list) {
                if (category.getPid() != null && category.getPid().intValue() == node.getId().intValue()) {
                    CategoryNode child = new CategoryNode();
                    BeanUtils.copyProperties(category, child);
                    if (node.getChildren() == null) {
                        node.setChildren(Lists.newArrayList());
                    }
                    node.getChildren().add(child);
                }
            }
        }
        return Categorys;
    }

    public List<CategoryNode> getCategories() {
        return getCategories(null);
    }
}


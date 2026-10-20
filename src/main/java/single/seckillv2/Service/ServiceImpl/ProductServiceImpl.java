package single.seckillv2.Service.ServiceImpl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import single.seckillv2.Commen.BusinessException;
import single.seckillv2.Commen.PageVO;
import single.seckillv2.Entity.Product;
import single.seckillv2.Entity.VO.ProductVO;
import single.seckillv2.Mapper.ProductMapper;
import single.seckillv2.Service.ProductService;

import java.util.List;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    //商品列表
    public PageVO<ProductVO> ProductList(int pageNum,int pageSize){
        //分页查询，只查询上架按创建时间倒序
        Page<Product> page = lambdaQuery()
                .eq(Product::getStatus,1)
                .orderByDesc(Product::getStartTime)
                .page(new Page<>(pageNum, pageSize));

        //转为VO
        List<ProductVO> voList = page.getRecords().stream()
                .map(p ->{
                    ProductVO vo = new ProductVO();
                    BeanUtils.copyProperties(p,vo);
                    return vo;
                })
                .toList();
        //封装分页结果
        PageVO<ProductVO> result = new PageVO<>();
        result.setTotal(page.getTotal());
        result.setList(voList);
        return result;
    }
    //商品详情
    public ProductVO SelectProductByID(Integer productId){
        Product product = getById(productId);
        if(product == null){
            throw new BusinessException("商品不存在");
        }
        //转为VO
        ProductVO vo = new ProductVO();
        BeanUtils.copyProperties(product,vo);
        return vo;
    }
}

package single.seckillv2.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import single.seckillv2.Commen.PageVO;
import single.seckillv2.Entity.Product;
import single.seckillv2.Entity.VO.ProductVO;

public interface ProductService extends IService<Product> {
    PageVO<ProductVO> ProductList(int pageNum, int pageSize);
    ProductVO SelectProductByID(Integer productId);
}

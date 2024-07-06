public class ProductDatabase {
    private Product[] products;

    public ProductDatabase() {
        products = new Product[9]; 

        // 각 상품을 생성하고 배열에 추가
		products[0] = new Product("냉동삼겹살", 9900, 8800, 3, "C:/Users/채리/바탕 화면/OOPTeamProject/images/냉동삼겹살.jpg");
        products[1] = new Product("쌀", 26900, 15900, 5, "C:/Users/채리/바탕 화면/OOPTeamProject/images/쌀.jpg");
        products[2] = new Product("생수", 7600, 5880, 6, "C:/Users/채리/바탕 화면/OOPTeamProject/images/생수.jpg");
        products[3] = new Product("고등어", 13900, 8340, 2, "C:/Users/채리/바탕 화면/OOPTeamProject/images/고등어.jpg");
        products[4] = new Product("수박", 22900, 17990, 1, "C:/Users/채리/바탕 화면/OOPTeamProject/images/수박.jpg");
        products[5] = new Product("두부", 4000, 3590, 8, "C:/Users/채리/바탕 화면/OOPTeamProject/images/두부.jpg");
        products[6] = new Product("달걀", 8490, 6790, 9, "C:/Users/채리/바탕 화면/OOPTeamProject/images/달걀.jpg");
        products[7] = new Product("맥주", 2000, 1390, 7, "C:/Users/채리/바탕 화면/OOPTeamProject/images/맥주.jpg");
        products[8] = new Product("복숭아", 24980, 18990, 4, "C:/Users/채리/바탕 화면/OOPTeamProject/images/복숭아.jpg");
    }
	

    public Product[] getProducts() {
        return products;
    }
	
	public Product searchProductByName(String name) {
        for (Product product : products) {
            if (product.getName().equals(name)) {
                return product;
            }
        }
        return null;
    }
	
	public Product searchProductByFilePath(String filePath) {
        for (Product product : products) {
            if (product.getImagePath().equals(filePath)) {
                return product;
            }
        }
        return null;
    }
}

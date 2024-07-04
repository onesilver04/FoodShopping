public class ProductDatabase {
    private Product[] products;

    public ProductDatabase() {
        products = new Product[9]; 

        // 각 상품을 생성하고 배열에 추가
		products[0] = new Product("헤드셋", 10000, 8000, 3, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[1] = new Product("상품 2", 20000, 15000, 5, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[2] = new Product("상품 3", 15000, 12000, 6, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[3] = new Product("상품 4", 30000, 25000, 2, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[4] = new Product("상품 5", 25000, 20000, 1, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[5] = new Product("상품 6", 18000, 15000, 8, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[6] = new Product("상품 7", 22000, 18000, 9, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[7] = new Product("상품 8", 28000, 23000, 3, "C:/Users/채리/바탕 화면/OOPTeamProject/images/headphone.jpg");
        products[8] = new Product("상품 9", 32000, 28000, 4, "C:/Users/채리/바탕 화면/OOPTeamProject/images/search.png");
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

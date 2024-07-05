public class  Product 
{
	private String name;
	private int price;
	private int salePrice;
	private int stock;
	private String imagePath;
	
	public Product(String name, int price, int salePrice,int stock, String imagePath){
		this.name = name;
		this.price = price;
		this.salePrice = salePrice;
		this.stock = stock;
		this.imagePath = imagePath;
		
	}
	public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
	
	public int getSalePrice() {
		return salePrice;
	}
	
	public int getStock() {
		return stock;
	}

    public String getImagePath() {
        return imagePath.replace("/", "\\");
    }
	
}

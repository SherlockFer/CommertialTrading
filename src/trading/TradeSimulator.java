package trading;
//Java 1.8

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TradeSimulator {

	private Integer MIN_NATIVE_PRODUCT_QUANTITY = 15;
	private Integer MAX_NATIVE_PRODUCT_QUANTITY = 50;
	private Integer MIN_EXTERNAL_PRODUCT_QUANTITY = 3;
	private Integer MAX_EXTERNAL_PRODUCT_QUANTITY = 40;
	private Double MIN_ALLOWANCE_FOR_DEPOT = 50.00;
	private Double MAX_ALLOWANCE_FOR_DEPOT = 100.00;
	private Double MIN_PRODUCT_PRICE = 1.00;
	private Double MAX_PRODUCT_PRICE = 10.00;

	
	private List<Company> companies = new ArrayList<Company>();
	static private List<Transaction> transactions =  new ArrayList<Transaction>();
	private static List<Transaction> transactionCompanyA =  new ArrayList<Transaction>();
	private List<Transaction> transactionCompanyB =  new ArrayList<Transaction>();
	static int maxNumberTransactions=100;
	static int numberTransaction=1;
//	HashMap<> transactionCompanyA = new HashMap<Integer, Transaction>();
	
	public static void main(String[] args) {
		TradeSimulator tradeSimulator = new TradeSimulator();
		
		waitForKeyPress("1. Initialize data.");
		tradeSimulator.initData();
		
		waitForKeyPress("2. Show generated data.");
		tradeSimulator.showData();
		
		waitForKeyPress("3. Simulate transactions.");
		for(;numberTransaction<=maxNumberTransactions;numberTransaction++) {
		tradeSimulator.simulateTransactions();
		}

		System.out.println("End");

	}

	

	private void initData() {

		// Companies
		Company companyA = new Company("Company-A");
		Company companyB = new Company("Company-B");
		companies.add(companyA);
		companies.add(companyB);

		// Products
		Product productA = new Product("Product-A");
		companyA.addProduct(productA);

		Product productB = new Product("Product-B");
		companyB.addProduct(productB);

		// Depots Company A
		for (int i = 1; i <= companyA.getMaxNroDepots(); i++) {
			//Create a random variable for money of depot
			Double allowanceA = getRandomDouble(MIN_ALLOWANCE_FOR_DEPOT, MAX_ALLOWANCE_FOR_DEPOT);
			//Create a Depot
			Depot depot = new Depot(companyA, "Depot-" + i, allowanceA);
			// Stock for Depot
			//random number for quantity of products
			Integer quantity = getRandomInteger(MIN_NATIVE_PRODUCT_QUANTITY, MAX_NATIVE_PRODUCT_QUANTITY);
			//random number for external quantity of external products
			Integer externalquantity = getRandomInteger(MIN_EXTERNAL_PRODUCT_QUANTITY, MAX_EXTERNAL_PRODUCT_QUANTITY);
			//random number for price of product
			Double productPrice = getRandomDouble(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE);
			//random number for price of delivery
			Double deliveryPrice = getRandomDouble(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE);
			//create a new Stock and save it on Company A (ArrayList)
			depot.addStock(new Stock(productA, Type.NATIVE,allowanceA,0.0, quantity,externalquantity,productPrice,deliveryPrice
					                ,productB, Type.EXTERNAL,0.0,0.0, 0,0, productPrice, deliveryPrice));
			companyA.addDepot(depot);
		}
				
		// Depots Company B
		for (int i = 1; i <= companyB.getMaxNroDepots(); i++) {
			//Create a random variable for money of depot
			Double allowanceB = getRandomDouble(MIN_ALLOWANCE_FOR_DEPOT, MAX_ALLOWANCE_FOR_DEPOT);
			//Create a Depot
			Depot depot = new Depot(companyB, "Depot-" + i, allowanceB);
			// Stock for Depot
			//random number for quantity of products
			Integer quantity = getRandomInteger(MIN_NATIVE_PRODUCT_QUANTITY, MAX_NATIVE_PRODUCT_QUANTITY);
			//random number for external quantity of external products
			Integer externalquantity = getRandomInteger(MIN_EXTERNAL_PRODUCT_QUANTITY, MAX_EXTERNAL_PRODUCT_QUANTITY);
			//random number for price of product
			Double productPrice = getRandomDouble(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE);
			//random number for price of delivery
			Double deliveryPrice = getRandomDouble(MIN_PRODUCT_PRICE, MAX_PRODUCT_PRICE);
			//create a new Stock and save it on Company A (ArrayList)
			depot.addStock(new Stock(productB, Type.NATIVE,allowanceB,0.0, quantity,externalquantity, productPrice, deliveryPrice
					                ,productA, Type.EXTERNAL,0.0,0.0,0,0, productPrice, deliveryPrice));
				
			companyB.addDepot(depot);
		}

	}

	private void simulateTransactions() {
		//TODO create around 100 transactions...

		Transaction transaction = getRandomTransaction();
		processTransaction(transaction);

	}
	
	private void showTransactions() {
//		for (Transaction i : transactionCompanyA) {
			  System.out.println(transactionCompanyA.size());
			  System.out.println(transactionCompanyA.get(0).toString());
			
	}
//


	private void processTransaction(Transaction transaction) {
		Depot buyerDepot = transaction.getBuyerDepot();
		Depot sellerDepot = transaction.getSellerDepot();
        transactions.add(transaction);
	}

	private Transaction getRandomTransaction(){
		//set a Random quantity to trade
		int CuantityToTrade=getRandomInteger(3,40);
		//set Random buyer and seller Depots
 		List<Integer> pairRandomIntegers = getPairRandomIntegers(2);
		Company sellerCompany = companies.get(pairRandomIntegers.get(0));
		Company buyerCompany = companies.get(pairRandomIntegers.get(1));
		//initializing values to validate transaction
		
		//Declaring variables from Depots to do transaction
		Depot buyerDepot = buyerCompany.getDepots().get(getRandomInteger(buyerCompany.getDepots().size()));
		Depot sellerDepot = sellerCompany.getDepots().get(getRandomInteger(sellerCompany.getDepots().size()));
		Product sellerProduct=sellerDepot.getStock().get(0).getProduct();
		Product buyerProduct=sellerDepot.getStock().get(0).getProduct();
		Double productSellerPrice=sellerDepot.getStock().get(0).getProductPrice();
		Double deliverySellerPrice=sellerDepot.getStock().get(0).getDeliveryPrice();
		Double initialCashBuyerDepot=buyerDepot.getStock().get(0).getInitialCashNativeDepot();
		Double initialCashSellerDepot=sellerDepot.getStock().get(0).getInitialCashNativeDepot();
		Double totalCost=CuantityToTrade*(productSellerPrice+deliverySellerPrice);
		int externalQuantity=buyerDepot.getStock().get(0).getNativeDepotExternalQuantity()+CuantityToTrade;
		int buyerQuantity=buyerDepot.getStock().get(0).getQuantity();
		int sellerQuantity=sellerDepot.getStock().get(0).getQuantity();
		int externalDepotNativeQuantity=sellerDepot.getStock().get(0).getNativeDepotExternalQuantity();
		int nativeDepotExternalQuantity=buyerDepot.getStock().get(0).getNativeDepotExternalQuantity();
		
		//Check declaration to check min cash , max and min internal products min and max external products
		if (CheckLimit( CuantityToTrade,buyerQuantity,sellerQuantity)==true && 
			TransactionSellertoBuyer(CuantityToTrade,productSellerPrice,deliverySellerPrice,initialCashBuyerDepot,totalCost,nativeDepotExternalQuantity)==true) {
			
			//Declaring variables to do cash transaction 
			Double finalCashBuyerDepot=buyerDepot.getStock().get(0).getInitialCashNativeDepot()-totalCost;
			Double finalCashSellerDepot=sellerDepot.getStock().get(0).getInitialCashNativeDepot()+totalCost;
			
			//DO TRANSACTION
			Transaction transaction = new Transaction(sellerProduct,buyerDepot,sellerDepot,CuantityToTrade,externalDepotNativeQuantity,
					productSellerPrice,deliverySellerPrice,totalCost,finalCashBuyerDepot,finalCashSellerDepot);
			
			System.out.println("\n Number transaction : "+numberTransaction + "\n");
			System.out.println(transaction);

			if(transaction.getBuyerDepot().getCompany().getName()=="Company-A") {
				transactionCompanyA.add(transaction);

			}else {
				transactionCompanyB.add(transaction);
			}
			buyerDepot.getStock().get(0).setInitialCashNativeDepot(finalCashBuyerDepot);
			sellerDepot.getStock().get(0).setInitialCashNativeDepot(finalCashSellerDepot);
 			sellerDepot.getStock().get(0).setExternalDepotNativeQuantity(externalDepotNativeQuantity);
 			buyerDepot.getStock().get(0).setNativeDepotExternalQuantity(nativeDepotExternalQuantity+CuantityToTrade);
 			
		}
		else {
			if(maxNumberTransactions>=numberTransaction) {
		    getRandomTransaction();
			}
		}
		Transaction transaction = new Transaction(sellerProduct,buyerDepot,sellerDepot,CuantityToTrade,externalQuantity,
				productSellerPrice,deliverySellerPrice,totalCost,0.0,0.0);
	return transaction;
	}

	
	private boolean CheckLimit(int CuantityToTrade,int buyerQuantity,int sellerQuantity) {
		boolean bool=false;
		//native quantity for buyer must be within 15 and 50
		if((buyerQuantity>=15 && buyerQuantity<=50 
			//native quantity for seller must be within 15 and 50
		    && sellerQuantity>=15 && sellerQuantity<=50
		    //external quantity for buyer must be within 3 and 40
		    && CuantityToTrade>=3 && CuantityToTrade<=40 
		    //The min native products must be over 15  
		    && (CuantityToTrade+15)<=sellerQuantity)
													==true){
													bool=true;
		}
		else{
			bool=false;
			};
	return bool;
	}
	
	private boolean TransactionSellertoBuyer(int CuantityToTrade,Double productSellerPrice,Double deliverySellerPrice,Double NativeDepotCash,Double totalCost, int nativeDepotExternalQuantity) {
		boolean bool=false;
		//cash depot native > cost of transaction
		if(NativeDepotCash>totalCost && 
		    //Native depot must be bigger than 0
		    NativeDepotCash>0 && 
		    //Cash depot native less cost must be bigger than 0
		    NativeDepotCash-totalCost>=0 &&
		    //Quantity of external product  plus quantity to trade must be less than 40
		    nativeDepotExternalQuantity+CuantityToTrade<=40) {
			bool=true;
		}
		else {
			bool=false;
		}
		return bool;
	}

	private Double getRandomDouble(Double min, Double max) {
		Random random1 = new Random();
		return min + (max - min) * random1.nextDouble();
	}

	private Integer getRandomInteger(Integer max) {
		return getRandomInteger(0, max);
	}

	private Integer getRandomInteger(Integer min, Integer max) {
		Random random = new Random();
		return min + random.nextInt(max - min);
	}

	public List<Integer> getPairRandomIntegers(Integer max) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i < max; i++) {
			list.add(i);
		}
		Collections.shuffle(list);
		return list;
	}

	private void showData() {
		for (Company company : companies) {
			showCompany(company);
		}
	}

	private void showCompany(Company company) {
		System.out.println(company);
		for (Product product : company.getProducts()) {
			showProduct(product);
		}
		for (Depot depot : company.getDepots()) {
			showDepot(depot);
		}

	}

	private void showProduct(Product product) {
		System.out.println(product);

	}

	private void showDepot(Depot depot) {
		System.out.println(depot);
		for (Stock stock : depot.getStock()) {
			showStock(stock);
		}
	}

	private void showStock(Stock stock) {
		System.out.println(stock);
	}

	private static void waitForKeyPress(String msg) {
		System.out.print(msg + " Press ENTER key to continue...\n");
		try {
			System.in.read();
		} catch (Exception e) {
		}
	}
	


}


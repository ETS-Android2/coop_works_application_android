package com.example.coopapp20.Products;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.coopapp20.Data.ConnectionCommandObject;
import com.example.coopapp20.Data.Dao.DatabaseChangeDao;
import com.example.coopapp20.Data.Dao.ProductChangeDao;
import com.example.coopapp20.Data.Dao.ProductDao;
import com.example.coopapp20.Data.Objects.DatabaseChangeObject;
import com.example.coopapp20.Data.Objects.ProductChangeObject;
import com.example.coopapp20.Data.Objects.ProductObject;
import com.example.coopapp20.Data.RoomDatabase;

import java.util.List;

class ProductRepository {

    private DatabaseChangeDao databaseChangeDao;
    private ProductDao productDao;
    private ProductChangeDao productChangeDao;

    private LiveData<List<ProductObject>> AllProducts;
    private LiveData<List<ProductChangeObject>> AllProductChanges;

    ProductRepository(Application application){
        RoomDatabase db = RoomDatabase.getDatabase(application);

        databaseChangeDao = db.databaseChangeDao();
        productDao = db.productDao();
        productChangeDao = db.productChangeDao();

        AllProducts = productDao.getAll();
        AllProductChanges = productChangeDao.getAll();
    }

    LiveData<List<ProductObject>> getAllProducts(){return AllProducts;}
    LiveData<List<ProductChangeObject>> getUserProductChanges(){return AllProductChanges;}

    void updateProduct(ProductObject product){
        RoomDatabase.databaseWriteExecutor.execute(() -> productDao.update(product));
        RoomDatabase.databaseWriteExecutor.execute(()->databaseChangeDao.insert(new DatabaseChangeObject(new ConnectionCommandObject().Product(ConnectionCommandObject.Edit,product).getCommand())));
    }
}

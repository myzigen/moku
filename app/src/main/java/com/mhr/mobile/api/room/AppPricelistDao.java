package com.mhr.mobile.api.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import java.util.List;

@Dao
public interface AppPricelistDao {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insertAll(List<AppPricelist> pricelist);
	
	@Query("SELECT * FROM pricelist")
	LiveData<List<AppPricelist>> getAllPricelist();
}
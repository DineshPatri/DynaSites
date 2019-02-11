package com.cerner.shipit.DynaSites.dao;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.cerner.shipit.DynaSites.model.DynaSitesModel;

@Repository
public interface DynaSitesRepository extends MongoRepository<DynaSitesModel, String> {
	/*@Autowired
	public MongoTemplate mongoTemplate;*/
}
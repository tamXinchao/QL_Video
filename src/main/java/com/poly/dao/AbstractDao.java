package com.poly.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.poly.util.JpaUtil;

public class AbstractDao<T> {
	public static final EntityManager entityManager = JpaUtil.getEntityManager();
	
	@SuppressWarnings("deprecation")
	@Override
	protected void finalize() throws Throwable {
		entityManager.close();
		super.finalize();
	}
	
	public T findById(Class<T> clazz , Integer id) {
		return entityManager.find(clazz, id);
	}
	
	public List<T> findAll(Class<T> clazz, boolean existIsActive){
		String entityName = clazz.getSimpleName();
		StringBuilder sql  = new StringBuilder();
		sql.append("select o FROM ").append(entityName).append(" o");
		
		if(existIsActive == true) {
			sql.append(" WHERE isActive = 1");
		}
		TypedQuery<T> query = entityManager.createQuery(sql.toString(),clazz);
		return query.getResultList();
		
		// select o FROM History o WHERE isActive = 1
	}
	
	public List<T> findAll(Class<T> clazz, boolean existIsActive, int PageNumber, int PageSize){
		String entityName = clazz.getSimpleName();
		StringBuilder sql  = new StringBuilder();
		sql.append("select o FROM ").append(entityName).append(" o");
		
		if(existIsActive == true) {
			sql.append(" WHERE isActive = 1");
		}
		TypedQuery<T> query = entityManager.createQuery(sql.toString(),clazz);
		query.setFirstResult((PageNumber - 1) * PageSize);
		query.setMaxResults(PageSize);
		return query.getResultList();
		
		// select o FROM History o WHERE isActive = 1
	}
	
//	select o FROM user o WHERE username = ?0 and o.password = ?1
	public T findOne(Class<T> clazz, String sql , Object ... params) {
		TypedQuery<T> query = entityManager.createQuery(sql, clazz);
		for(int i = 0; i< params.length; i++) {
			query.setParameter(i, params[i]);
		}
		List<T> result = query.getResultList();
		if(result.isEmpty()) {
			return null;
		}
		return result.get(0);
	}
	
	public List<T> findMany(Class<T> clazz, String sql , Object ... params) {
		TypedQuery<T> query = entityManager.createQuery(sql, clazz);
		for(int i = 0; i< params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Object[]> findManyByNativeQuery (Class<T> clazz, String sql , Object ... params) {
		Query query = entityManager.createNativeQuery(sql, clazz);
		for(int i = 0; i< params.length; i++) {
			query.setParameter(i, params[i]);
		}
		return query.getResultList();
	}
	
	public T create(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.persist(entity);
			entityManager.getTransaction().commit();
			System.out.println("Create success");
			return entity;
		}catch(Exception e){
			entityManager.getTransaction().rollback();
			System.out.println("Cannot insert entity " + entity.getClass().getSimpleName()+ " to DB");
			throw new RuntimeException(e);
		}
	}
	
	public T update(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.merge(entity);
			entityManager.getTransaction().commit();
			System.out.println("Update success");
			return entity;
		}catch(Exception e){
			entityManager.getTransaction().rollback();
			System.out.println("Cannot update entity " + entity.getClass().getSimpleName());
			throw new RuntimeException(e);
		}
	}
	
	public T delete(T entity) {
		try {
			entityManager.getTransaction().begin();
			entityManager.remove(entity);
			entityManager.getTransaction().commit();
			System.out.println("Delete success");
			return entity;
		}catch(Exception e){
			entityManager.getTransaction().rollback();
			System.out.println("Cannot delete entity " + entity.getClass().getSimpleName());
			throw new RuntimeException(e);
		}
	}
}

![jpersis](logo.png)
=======

Light weighted persistence framework for Java.


[![Build Status](https://travis-ci.org/MyRealityCoding/jpersis.svg?branch=master)](https://travis-ci.org/MyRealityCoding/jpersis)

Getting started
===

This framework provides easy-to-write data mapping. You can simply create a new jpersis:

```java
JPersis persis = new JPersis(new PostgresConnector("localhost", "1234", "root", "mypassword"));
```

Afterwards you can use the framework properly:

```java
Customer customer = persis.get(CustomerMapper.class).findByID(123);
```

Case study: An own customer mapper
===

A sample customer mapper could look like this:

```java
@DataMapper("de.myreality.test.models.Customer")
public interface CustomerMapper {

    // ==============================================================
    // Select Statements
    // ==============================================================
    
    
    @Select
    Collection<Customer> findAll() throws DatabaseException;

    @Select(condition = "customer_id = $1")
    Customer findById(int id) throws DatabaseException;
    
    @Select(condition = "first_name = $1")
    Customer findByFirstName(String firstName) throws DatabaseException;
    
    
    // ==============================================================
    // Insert Statements
    // ==============================================================
    
    @Insert
    void insert(Customer customer) throws DatabaseException;
    
    @Insert
    void insert(Collection<Customer> customer) throws DatabaseException;
    
    //==============================================
    // Count Statement
    //==============================================
    
    @Count
    int CountAll() throws DatabaseException;
    
    
    //==============================================
    // Update Statements
    //==============================================
    
    @Update
    void update(Customer customer) throws DatabaseException;
    
    
    @Update
    void update(Collection<Customer> customer) throws DatabaseException;
    
    
    //==============================================
    // Delete Statements
    //==============================================
    
    @Delete
    void delete(Customer customer) throws DatabaseException;
       
    @Delete
    void delete(Collection<Customer> customer) throws DatabaseException;
    
}
```

You only need to provide the model ```Customer``` for that.

### How to design a proper model

Jpersis needs to know how to build the model. Therefore we need to define it in Java first:

```java
public class Customer {

	@PrimaryKey
	private int id;

	private String name;

	private String adress;

	/* A default constructor is needed */
	public Customer() { 

	}

	public String getName() {
		return name;
	}
}
```
Jpersis will create a table for you and maps the given values properly.
